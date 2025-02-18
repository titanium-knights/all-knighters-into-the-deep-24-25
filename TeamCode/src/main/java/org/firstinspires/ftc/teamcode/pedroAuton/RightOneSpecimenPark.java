
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

import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

@Autonomous(name = "Right Park", group = "Pedro Autons")
public class RightOneSpecimenPark extends OpMode {

    // Sleep time between segments (reduced from 1500ms to 500ms)
    private static final int SLEEP_TIME_MS = 500;

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private SubsystemManager subsystemManager;

    /** Current state of the autonomous state machine */
    private int pathState;

    /* Poses for the robot's key positions on the field */
    private final Pose startPose = new Pose(10, 62, Math.toRadians(0));
    private final Pose scorePreloadedSpecimenPose = new Pose(39, 62, Math.toRadians(0));
    private final Pose alignToPrepareForRetrieval1 = new Pose(20, 36, Math.toRadians(0));
    private final Pose retrieveSpecimenPose1_1 = new Pose(60, 36, Math.toRadians(0));
    private final Pose retrieveSpecimenPose1_2 = new Pose(60, 27, Math.toRadians(0));
    private final Pose endingPoint1 = new Pose(20, 27, Math.toRadians(0));
    private final Pose retrieveSpecimenPose2_1 = new Pose(60, 27, Math.toRadians(0));
    private final Pose retrieveSpecimenPose2_2 = new Pose(60, 20, Math.toRadians(0));
    private final Pose endingPoint2 = new Pose(20, 20, Math.toRadians(0));
    private final Pose retrieveSpecimenPose3_1 = new Pose(60, 20, Math.toRadians(0));
    private final Pose retrieveSpecimenPose3_2 = new Pose(60, 13, Math.toRadians(0));
    private final Pose endingPoint3 = new Pose(20, 13, Math.toRadians(0));

    /* PathChain objects for each motion segment.
     * Note: Multi-segment motions have been split into separate path chains.
     */
    private PathChain scorePreloadedSpecimen;
    private PathChain goToPickupPositionAfterScoring;
    private PathChain retrieveSpecimenMotion1_1;
    private PathChain retrieveSpecimenMotion1_2A;
    private PathChain retrieveSpecimenMotion1_2B;
    private PathChain retrieveSpecimenMotion2_1;
    private PathChain retrieveSpecimenMotion2_2A;
    private PathChain retrieveSpecimenMotion2_2B;
    private PathChain retrieveSpecimenMotion3_1;
    private PathChain retrieveSpecimenMotion3_2;
    private PathChain retrieveSpecimenMotion3_3;

    /**
     * Constructs all motion paths for the autonomous routine.
     * Each path chain is built using a BezierLine with linear heading interpolation.
     */
    public void buildPaths() {
        // Path from start to scoring position.
        scorePreloadedSpecimen = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scorePreloadedSpecimenPose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePreloadedSpecimenPose.getHeading())
                .build();

        // Path from scoring position to the alignment pose for retrieval.
        goToPickupPositionAfterScoring = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePreloadedSpecimenPose), new Point(alignToPrepareForRetrieval1)))
                .setLinearHeadingInterpolation(scorePreloadedSpecimenPose.getHeading(), alignToPrepareForRetrieval1.getHeading())
                .build();

        // First retrieval motion: move from alignment to the first pickup position.
        retrieveSpecimenMotion1_1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(alignToPrepareForRetrieval1), new Point(retrieveSpecimenPose1_1)))
                .setLinearHeadingInterpolation(alignToPrepareForRetrieval1.getHeading(), retrieveSpecimenPose1_1.getHeading())
                .build();

        // Second retrieval motion: split into two segments.
        retrieveSpecimenMotion1_2A = follower.pathBuilder()
                .addPath(new BezierLine(new Point(retrieveSpecimenPose1_1), new Point(retrieveSpecimenPose1_2)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose1_1.getHeading(), retrieveSpecimenPose1_2.getHeading())
                .build();

        retrieveSpecimenMotion1_2B = follower.pathBuilder()
                .addPath(new BezierLine(new Point(retrieveSpecimenPose1_2), new Point(endingPoint1)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose1_2.getHeading(), endingPoint1.getHeading())
                .build();

        // Third retrieval motion: move from the first ending point to the second pickup position.
        retrieveSpecimenMotion2_1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(endingPoint1), new Point(retrieveSpecimenPose2_1)))
                .setLinearHeadingInterpolation(endingPoint1.getHeading(), retrieveSpecimenPose2_1.getHeading())
                .build();

        // Fourth retrieval motion: split into two segments.
        retrieveSpecimenMotion2_2A = follower.pathBuilder()
                .addPath(new BezierLine(new Point(retrieveSpecimenPose2_1), new Point(retrieveSpecimenPose2_2)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose2_1.getHeading(), retrieveSpecimenPose2_2.getHeading())
                .build();

        retrieveSpecimenMotion2_2B = follower.pathBuilder()
                .addPath(new BezierLine(new Point(retrieveSpecimenPose2_2), new Point(endingPoint2)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose2_2.getHeading(), endingPoint2.getHeading())
                .build();

        // Fifth retrieval motion: split into three segments.
        retrieveSpecimenMotion3_1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(endingPoint2), new Point(retrieveSpecimenPose3_1)))
                .setLinearHeadingInterpolation(endingPoint2.getHeading(), retrieveSpecimenPose3_1.getHeading())
                .build();

        retrieveSpecimenMotion3_2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(retrieveSpecimenPose3_1), new Point(retrieveSpecimenPose3_2)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose3_1.getHeading(), retrieveSpecimenPose3_2.getHeading())
                .build();

        retrieveSpecimenMotion3_3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(retrieveSpecimenPose3_2), new Point(endingPoint3)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose3_2.getHeading(), endingPoint3.getHeading())
                .build();
    }

    /**
     * Autonomous state machine update.
     * Each case corresponds to a motion segment or mechanism action.
     */
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                // Begin scoring: follow the path and set scoring mechanism positions.
                if (!follower.isBusy()) {
                    follower.followPath(scorePreloadedSpecimen, true);
                    subsystemManager.topClaw.close();
                    subsystemManager.slides.slideToPosition(SlideState.MEDIUM);
                    waitForSlidePosition(SlideState.MEDIUM);
                }
                if (actionTimer.getElapsedTimeSeconds() > 5) {
                    setPathState(1);
                }
                telemetry.addLine("State 0: Scoring Preloaded Specimen");
                break;

            case 1:
                // Score actions: adjust slides, operate claw, then retract slides.
                if (!follower.isBusy()) {
                    if (actionTimer.getElapsedTimeSeconds() > 2) {
                        subsystemManager.slides.slideToPosition(SlideState.MEDIUM_SCORE);
                        waitForSlidePosition(SlideState.MEDIUM_SCORE);
                    }
                    if (actionTimer.getElapsedTimeSeconds() > 3) {
                        subsystemManager.topClaw.open();
                    }
                    if (actionTimer.getElapsedTimeSeconds() > 5) {
                        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
                        waitForSlidePosition(SlideState.BOTTOM);
                        setPathState(10);
                    }
                }
                telemetry.addLine("State 1: Scoring Actions");
                break;

            case 10:
                // Move from scoring to pickup alignment.
                if (!follower.isBusy()) {
                    follower.followPath(goToPickupPositionAfterScoring, true);
                    setPathState(20);
                }
                telemetry.addLine("State 10: Moving to Pickup Position");
                break;

            case 20:
                // Brief pause before starting retrieval motion 1.
                if (!follower.isBusy()) {
                    sleepSegment();
                    setPathState(30);
                }
                telemetry.addLine("State 20: Pause");
                break;

            case 30:
                // Move to the first retrieval pickup position.
                if (!follower.isBusy()) {
                    follower.followPath(retrieveSpecimenMotion1_1, true);
                    setPathState(33);
                }
                telemetry.addLine("State 30: Moving to Retrieval Position 1");
                break;

            case 33:
                // Pause before next segment.
                if (!follower.isBusy()) {
                    sleepSegment();
                    setPathState(37);
                }
                telemetry.addLine("State 33: Pause");
                break;

            case 37:
                // Follow first segment of the split retrieval motion.
                if (!follower.isBusy()) {
                    follower.followPath(retrieveSpecimenMotion1_2A, true);
                    setPathState(38);
                }
                telemetry.addLine("State 37: Retrieval Motion 1_2 Part A");
                break;

            case 38:
                // Short pause between segments.
                if (!follower.isBusy()) {
                    sleepSegment();
                    setPathState(39);
                }
                telemetry.addLine("State 38: Pause");
                break;

            case 39:
                // Follow the second segment of the split retrieval motion.
                if (!follower.isBusy()) {
                    follower.followPath(retrieveSpecimenMotion1_2B, true);
                    setPathState(40);
                }
                telemetry.addLine("State 39: Retrieval Motion 1_2 Part B");
                break;

            case 40:
                // Pause before starting the next retrieval motion.
                if (!follower.isBusy()) {
                    sleepSegment();
                    setPathState(50);
                }
                telemetry.addLine("State 40: Pause");
                break;

            case 50:
                // Move to the second retrieval pickup position.
                if (!follower.isBusy()) {
                    follower.followPath(retrieveSpecimenMotion2_1, true);
                    setPathState(53);
                }
                telemetry.addLine("State 50: Retrieval Motion 2_1");
                break;

            case 53:
                // Pause before the next split segment.
                if (!follower.isBusy()) {
                    sleepSegment();
                    setPathState(57);
                }
                telemetry.addLine("State 53: Pause");
                break;

            case 57:
                // Follow the first segment of the second split retrieval motion.
                if (!follower.isBusy()) {
                    follower.followPath(retrieveSpecimenMotion2_2A, true);
                    setPathState(58);
                }
                telemetry.addLine("State 57: Retrieval Motion 2_2 Part A");
                break;

            case 58:
                // Short pause between segments.
                if (!follower.isBusy()) {
                    sleepSegment();
                    setPathState(59);
                }
                telemetry.addLine("State 58: Pause");
                break;

            case 59:
                // Follow the second segment of the second split retrieval motion.
                if (!follower.isBusy()) {
                    follower.followPath(retrieveSpecimenMotion2_2B, true);
                    setPathState(60);
                }
                telemetry.addLine("State 59: Retrieval Motion 2_2 Part B");
                break;

            case 60:
                // Pause before beginning the final retrieval motion.
                if (!follower.isBusy()) {
                    sleepSegment();
                    setPathState(70);
                }
                telemetry.addLine("State 60: Pause");
                break;

            case 70:
                // Follow the first segment of the third (three-part) retrieval motion.
                if (!follower.isBusy()) {
                    follower.followPath(retrieveSpecimenMotion3_1, true);
                    setPathState(71);
                }
                telemetry.addLine("State 70: Retrieval Motion 3_1");
                break;

            case 71:
                // Pause before the next segment.
                if (!follower.isBusy()) {
                    sleepSegment();
                    setPathState(72);
                }
                telemetry.addLine("State 71: Pause");
                break;

            case 72:
                // Follow the second segment of the third retrieval motion.
                if (!follower.isBusy()) {
                    follower.followPath(retrieveSpecimenMotion3_2, true);
                    setPathState(73);
                }
                telemetry.addLine("State 72: Retrieval Motion 3_2");
                break;

            case 73:
                // Pause before the final segment.
                if (!follower.isBusy()) {
                    sleepSegment();
                    setPathState(74);
                }
                telemetry.addLine("State 73: Pause");
                break;

            case 74:
                // Follow the final segment of the third retrieval motion.
                if (!follower.isBusy()) {
                    follower.followPath(retrieveSpecimenMotion3_3, true);
                    setPathState(75);
                }
                telemetry.addLine("State 74: Retrieval Motion 3_3");
                break;

            case 75:
                // Final pause to conclude the routine.
                if (!follower.isBusy()) {
                    sleepSegment();
                    setPathState(77);
                }
                telemetry.addLine("State 75: Final Pause");
                break;

            case 77:
                // Autonomous routine complete.
                telemetry.addLine("State 77: Routine Completed");
                break;

            default:
                telemetry.addLine("Unknown State");
                break;
        }
    }

    /**
     * Updates the state and resets timers.
     *
     * @param newState The new state to transition into.
     */
    public void setPathState(int newState) {
        pathState = newState;
        pathTimer.resetTimer();
        actionTimer.resetTimer();
    }

    /**
     * Blocks until the slide mechanism reaches the desired position.
     *
     * @param target The target slide state.
     */
    private void waitForSlidePosition(SlideState target) {
        while (!subsystemManager.slides.slideToPosition(target)) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Sleeps for a short duration between motion segments.
     */
    private void sleepSegment() {
        try {
            Thread.sleep(SLEEP_TIME_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Main loop: updates path following and state machine, then provides telemetry.
     */
    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading", follower.getPose().getHeading());
        telemetry.update();
    }

    /**
     * Initializes timers, subsystems, starting pose, and builds all paths.
     */
    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        actionTimer = new Timer();
        opmodeTimer.resetTimer();

        subsystemManager = new SubsystemManager(hardwareMap, telemetry);
        subsystemManager.arm.toInitPos();
        subsystemManager.topClaw.close();

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    @Override
    public void init_loop() {}

    /**
     * Resets the opmode timer and state when starting.
     */
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {}
}
