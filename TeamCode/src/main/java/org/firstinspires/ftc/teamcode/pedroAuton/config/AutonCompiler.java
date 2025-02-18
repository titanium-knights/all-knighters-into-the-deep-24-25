package org.firstinspires.ftc.teamcode.pedroAuton.config;

import com.pedropathing.localization.Pose;
import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;
import com.pedropathing.util.Constants;

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
        for (RightOneSpecimenParkConfig.AutonStepDescriptor desc : config.getRoutine()) {
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
    private static IAutonStep createStepFromDescriptor(RightOneSpecimenParkConfig.AutonStepDescriptor desc) {
        switch (desc.type) {
            case PATH:
                return new PathStep(desc.startPose, desc.endPose);
            case ACTION:
                return new ActionStep(desc.actionCode, desc.timeout);
            case SLEEP:
                return new SleepStep(desc.duration);
            case PARALLEL:
                return new ParallelStep(desc.subSteps);
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
            follower.followPath(pathChain, true);
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
            long elapsedMs = System.currentTimeMillis() - startTimeMs;
            if (timeout > 0 && elapsedMs >= timeout * 1000) {
                return true;
            }

            switch (actionCode) {
                case "CLOSE_CLAW":
                    subsystemManager.topClaw.close();
                    return true;
                case "OPEN_CLAW":
                    subsystemManager.topClaw.open();
                    return true;
                case "SLIDE_MEDIUM":
                    return subsystemManager.slides.slideToPosition(SlideState.MEDIUM);
                case "SLIDE_MEDIUM_SCORE":
                    return subsystemManager.slides.slideToPosition(SlideState.MEDIUM_SCORE);
                case "SLIDE_BOTTOM":
                    return subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
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
     * ParallelStep executes a list of sub-steps concurrently.
     * It completes only when all sub-steps have finished.
     */
    private static class ParallelStep implements IAutonStep {
        private List<IAutonStep> parallelSteps = new ArrayList<>();

        public ParallelStep(List<RightOneSpecimenParkConfig.AutonStepDescriptor> subDescriptors) {
            for (RightOneSpecimenParkConfig.AutonStepDescriptor desc : subDescriptors) {
                IAutonStep step = createStepFromDescriptor(desc);
                if (step != null) {
                    parallelSteps.add(step);
                }
            }
        }

        @Override
        public void init(Follower follower, SubsystemManager subsystemManager) {
            for (IAutonStep step : parallelSteps) {
                step.init(follower, subsystemManager);
            }
        }

        @Override
        public boolean update() {
            boolean allComplete = true;
            for (IAutonStep step : parallelSteps) {
                if (!step.update()) {
                    allComplete = false;
                }
            }
            return allComplete;
        }
    }
}
