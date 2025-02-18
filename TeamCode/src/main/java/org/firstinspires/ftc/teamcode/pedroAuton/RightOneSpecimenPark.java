package org.firstinspires.ftc.teamcode.pedroAuton;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;
import java.util.List;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonConfig;
import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonConfig.AutonStepDescriptor;
import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonConfig.StepType;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

/**
 * RightOneSpecimenPark automatically builds its execution logic from AutonConfig.ROUTINE.
 *
 * Instead of a long switch statement, it builds a list of steps (each either a motion,
 * an action, or a pause) and executes them sequentially.
 */
@Autonomous(name = "Right Park", group = "Pedro Autons")
public class RightOneSpecimenPark extends OpMode {

    private Follower follower;
    private SubsystemManager subsystemManager;
    private Timer opmodeTimer;

    // The list of autonomous steps built from the config.
    private List<IAutonStep> steps;
    private int currentStepIndex = 0;

    @Override
    public void init() {
        opmodeTimer = new Timer();

        // Initialize hardware and subsystems.
        subsystemManager = new SubsystemManager(hardwareMap, telemetry);
        subsystemManager.arm.toInitPos();
        subsystemManager.topClaw.close();

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        // Set starting pose from configuration.
        follower.setStartingPose(AutonConfig.START_POSE);

        // Build the autonomous steps from the configuration routine.
        buildSteps();
    }

    /**
     * Reads the AutonConfig.ROUTINE list and creates an ordered list of executable steps.
     */
    private void buildSteps() {
        steps = new ArrayList<>();
        for (AutonStepDescriptor desc : AutonConfig.ROUTINE) {
            switch (desc.type) {
                case PATH:
                    steps.add(new PathStep(desc.startPose, desc.endPose));
                    break;
                case ACTION:
                    steps.add(new ActionStep(desc.actionCode));
                    break;
                case SLEEP:
                    steps.add(new SleepStep(desc.duration));
                    break;
            }
        }
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        currentStepIndex = 0;
        if (!steps.isEmpty()) {
            steps.get(0).init(follower, subsystemManager);
        }
    }

    @Override
    public void loop() {
        // Always update follower.
        follower.update();

        // Process the current step.
        if (currentStepIndex < steps.size()) {
            IAutonStep currentStep = steps.get(currentStepIndex);
            if (currentStep.update()) {  // Step completed.
                currentStepIndex++;
                if (currentStepIndex < steps.size()) {
                    steps.get(currentStepIndex).init(follower, subsystemManager);
                }
            }
        }

        telemetry.addData("Current Step", currentStepIndex);
        telemetry.update();
    }

    @Override
    public void stop() {}


    /**
     * IAutonStep is a common interface for all autonomous steps.
     * Each step must be able to initialize (with access to subsystems)
     * and then be updated each loop until it signals completion.
     */
    private interface IAutonStep {
        void init(Follower follower, SubsystemManager subsystemManager);
        /**
         * @return true if the step is complete and the next step can begin.
         */
        boolean update();
    }

    /**
     * PathStep represents a motion step.
     * It builds a path from a starting pose to an ending pose using linear heading interpolation.
     */
    private class PathStep implements IAutonStep {
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
            // Build a simple BezierLine path from start to end.
            pathChain = follower.pathBuilder()
                    .addPath(new BezierLine(new Point(startPose), new Point(endPose)))
                    .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                    .build();
            follower.followPath(pathChain, true);
        }

        @Override
        public boolean update() {
            // Step is complete when the follower is no longer busy.
            return !follower.isBusy();
        }
    }

    /**
     * ActionStep represents a mechanism action.
     * It interprets an action code (defined in AutonConfig) and executes the corresponding command.
     *
     * The following action codes are supported:
     * - "CLOSE_CLAW": Closes the top claw.
     * - "OPEN_CLAW": Opens the top claw.
     * - "SLIDE_MEDIUM": Moves the slides to the medium position.
     * - "SLIDE_MEDIUM_SCORE": Moves the slides to the scoring position.
     * - "SLIDE_BOTTOM": Retracts the slides.
     */
    private class ActionStep implements IAutonStep {
        private String actionCode;
        private SubsystemManager subsystemManager;

        public ActionStep(String code) {
            this.actionCode = code;
        }

        @Override
        public void init(Follower follower, SubsystemManager subsystemManager) {
            this.subsystemManager = subsystemManager;
        }

        @Override
        public boolean update() {
            // Execute the action. For slide commands, repeatedly call the method until it returns true.
            switch (actionCode) {
                case "CLOSE_CLAW":
                    subsystemManager.topClaw.close();
                    return true; // Instant action.
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
                    telemetry.addData("ActionStep", "Unknown action: " + actionCode);
                    return true;
            }
        }
    }

    /**
     * SleepStep simply waits for a specified duration (in seconds) before completing.
     */
    private class SleepStep implements IAutonStep {
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
            long elapsed = System.currentTimeMillis() - startTimeMs;
            return elapsed >= durationSeconds * 1000;
        }
    }
}