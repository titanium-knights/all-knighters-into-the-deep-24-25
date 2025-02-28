package org.firstinspires.ftc.teamcode.pedroAuton.config;

import com.pedropathing.localization.Pose;
import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

import org.firstinspires.ftc.teamcode.pedroAuton.config.states.BucketUpPostDunk;
import org.firstinspires.ftc.teamcode.pedroAuton.config.states.BucketUpPreDunk;
import org.firstinspires.ftc.teamcode.pedroAuton.config.states.HorizontalOutClosed;
import org.firstinspires.ftc.teamcode.pedroAuton.config.states.HorizontalOutClosedTwist;
import org.firstinspires.ftc.teamcode.pedroAuton.config.states.HorizontalOutOpen;
import org.firstinspires.ftc.teamcode.pedroAuton.config.states.HorizontalOutOpenTwist;
import org.firstinspires.ftc.teamcode.pedroAuton.config.states.HorizontalTransferClosed;
import org.firstinspires.ftc.teamcode.pedroAuton.config.states.HorizontalTransferOpen;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;
import com.pedropathing.util.Constants;
import org.firstinspires.ftc.teamcode.pedroAuton.config.states.SlidesMediumScoreClawClosed;
import org.firstinspires.ftc.teamcode.pedroAuton.config.states.SlidesMediumClawClosed;
import org.firstinspires.ftc.teamcode.pedroAuton.config.states.SlidesBottomClawClosed;
import org.firstinspires.ftc.teamcode.pedroAuton.config.states.SlidesBottomClawOpen;
import org.firstinspires.ftc.teamcode.pedroAuton.config.states.LowHang;

import java.util.ArrayList;
import java.util.List;

/**
 * AutonCompiler compiles an IAutonConfig into a list of executable steps.
 * It contains the definitions of IAutonStep and its concrete implementations.
 *
 * This file is part of the config layer. The execution layer will simply import
 * this compiler along with a unique configuration.
 */
public class AutonCompiler {

    /**
     * IAutonStep defines the interface for an executable autonomous step.
     */
    public interface IAutonStep {
        /**
         * Initialize the step with references to the follower and subsystem manager.
         */
        void init(Follower follower, SubsystemManager subsystemManager);
        /**
         * Update the step. @return true when the step is complete.
         */
        boolean update();
    }

    /**
     * Compiles the given configuration into a list of executable steps.
     */
    public static List<IAutonStep> compile(IAutonConfig config) {
        List<IAutonStep> steps = new ArrayList<>();
        for (AutonStepDescriptor desc : config.getRoutine()) {
            IAutonStep step = createStepFromDescriptor(desc);
            if (step != null) {
                steps.add(step);
            }
        }
        return steps;
    }

    /**
     * Recursively creates an IAutonStep from a descriptor.
     */
    private static IAutonStep createStepFromDescriptor(AutonStepDescriptor desc) {
        switch (desc.type) {
            case PATH:
                return new PathStep(desc.startPose, desc.endPose);
            case STATE:
                return new ActionStep(desc.autonStateCode, desc.timeout);
            case SLEEP:
                return new SleepStep(desc.duration);
            case PARALLEL:
                return new ParallelStep(desc.steps);
            default:
                return null;
        }
    }

    // ----- Step Implementations -----

    /**
     * PathStep builds and executes a path from a starting pose to an ending pose.
     */
    private static class PathStep implements IAutonStep {
        private Pose startPose, endPose;
        private PathChain pathChain;
        private Follower follower;

        public PathStep(Pose start, Pose end) {
            this.startPose = start;
            this.endPose = end;
        }

        @Override
        public void init(Follower follower, SubsystemManager subsystemManager) {
            this.follower = follower;
            // Ensure constants are set.
            Constants.setConstants(FConstants.class, LConstants.class);
            // Build a simple BezierLine path.
            pathChain = follower.pathBuilder()
                    .addPath(new BezierLine(new Point(startPose), new Point(endPose)))
                    .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                    .build();
            follower.followPath(pathChain, false);
        }

        @Override
        public boolean update() {
            return !follower.isBusy();
        }
    }

    /**
     * ActionStep executes a mechanism action.
     * Supported action codes include: "CLOSE_CLAW", "OPEN_CLAW",
     * "SLIDE_MEDIUM", "SLIDE_MEDIUM_SCORE", and "SLIDE_BOTTOM".
     * If a timeout is specified and exceeded, the step completes.
     */
    private static class ActionStep implements IAutonStep {
        private String actionCode;
        private double timeout; // in seconds
        private long startTimeMs;
        private SubsystemManager subsystemManager;

        public ActionStep(String code, double timeout) {
            this.actionCode = code;
            this.timeout = timeout;
        }

        @Override
        public void init(Follower follower, SubsystemManager subsystemManager) {
            this.subsystemManager = subsystemManager;
            startTimeMs = System.currentTimeMillis();
        }

        @Override
        public boolean update() {
            switch (actionCode) {
                case "BOTTOM_CLOSED":
                    return new SlidesBottomClawClosed(subsystemManager).update();
                case "MEDIUM_CLOSED":
                    return new SlidesMediumClawClosed(subsystemManager).update();
                case "MEDIUM_SCORE_CLOSED":
                    return new SlidesMediumScoreClawClosed(subsystemManager).update();
                case "BOTTOM_OPEN":
                    return new SlidesBottomClawOpen(subsystemManager).update();
                case "HORI_OUT_CLOSED":
                    return new HorizontalOutClosed(subsystemManager).update();
                case "HORI_OUT_OPEN":
                    return new HorizontalOutOpen(subsystemManager).update();
                case "HORI_TRANSFER_CLOSED":
                    return new HorizontalTransferClosed(subsystemManager).update();
                case "HORI_TRANSFER_OPEN":
                    return new HorizontalTransferOpen(subsystemManager).update();
                case "BUCKET_UP_POST_DUNK":
                    return new BucketUpPostDunk(subsystemManager).update();
                case "BUCKET_UP_PRE_DUNK":
                    return new BucketUpPreDunk(subsystemManager).update();
                case "LOW_HANG":
                    return new LowHang(subsystemManager).update();
                case "HOR_OUT_CLOSED_TWIST":
                    return new HorizontalOutClosedTwist(subsystemManager).update();
                case "HOR_OUT_OPEN_TWIST":
                    return new HorizontalOutOpenTwist(subsystemManager).update();
                default:
                    return true;
            }
        }
    }

    /**
     * SleepStep waits for a specified duration (in seconds) before completing.
     */
    private static class SleepStep implements IAutonStep {
        private double durationSeconds;
        private long startTimeMs;

        public SleepStep(double durationSeconds) {
            this.durationSeconds = durationSeconds;
        }

        @Override
        public void init(Follower follower, SubsystemManager subsystemManager) {
            startTimeMs = System.currentTimeMillis();
        }

        @Override
        public boolean update() {
            return (System.currentTimeMillis() - startTimeMs) >= durationSeconds * 1000;
        }
    }

    /**
     * ParallelStep executes a list of steps in parallel.
     * The step completes when all sub-steps are complete.
     */
    private static class ParallelStep implements IAutonStep {
        private List<IAutonStep> steps;

        public ParallelStep(List<AutonStepDescriptor> descriptors) {
            steps = new ArrayList<>();
            for (AutonStepDescriptor desc : descriptors) {
                IAutonStep step = createStepFromDescriptor(desc);
                if (step != null) {
                    steps.add(step);
                }
            }
        }

        @Override
        public void init(Follower follower, SubsystemManager subsystemManager) {
            for (IAutonStep step : steps) {
                step.init(follower, subsystemManager);
            }
        }

        @Override
        public boolean update() {
            boolean allComplete = true;
            for (IAutonStep step : steps) {
                allComplete &= step.update();
            }
            return allComplete;
        }
    }
}
