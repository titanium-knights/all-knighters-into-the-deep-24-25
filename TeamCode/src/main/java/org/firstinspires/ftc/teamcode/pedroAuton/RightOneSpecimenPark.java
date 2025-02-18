package org.firstinspires.ftc.teamcode.pedroAuton;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

@Autonomous(name = "Right Park", group = "Pedro Autons")
public class RightOneSpecimenPark extends OpMode {
    private Follower follower;

    private Timer pathTimer, actionTimer, opmodeTimer;

    private SubsystemManager subsystemManager;

    /** This is the variable where we store the state of our auto.
     * It is used by the pathUpdate method. */
    private int pathState;

    /* Create and Define Poses + Paths
     * Poses are built with three constructors: x, y, and heading (in Radians).
     * Pedro uses 0 - 144 for x and y, with 0, 0 being on the bottom left.
     * (For Into the Deep, this would be Blue Observation Zone (0,0) to Red Observation Zone (144,144).)
     * Even though Pedro uses a different coordinate system than RR, you can convert any roadrunner pose by adding +72 both the x and y.
     * This visualizer is very easy to use to find and create paths/pathchains/poses: <https://pedro-path-generator.vercel.app/>
     * Lets assume our robot is 18 by 18 inches
     * Lets assume the Robot is facing the human player and we want to score a specimen */

    /** Start Pose of our robot */
    private final Pose startPose = new Pose(10, 62, Math.toRadians(0));

    private final Pose moveToPrepToScorePreloadedSpecimenPose = new Pose(36, 62, Math.toRadians(0));

    private final Pose scorePreloadedSpecimenPose = new Pose(40, 62, Math.toRadians(0));

    private final Pose alignToPrepareForRetrieval1 = new Pose(10, 33, Math.toRadians(0));

    private final Pose retrieveSpecimenPose1_1 = new Pose(62, 33, Math.toRadians(0));

    private final Pose retrieveSpecimenPose1_2 = new Pose(62, 27, Math.toRadians(0));

    private final Pose retrieveSpecimenPose1_3 = new Pose(10, 27, Math.toRadians(0));

    private final Pose alignToPrepareForRetrieval2 = new Pose(10, 20, Math.toRadians(0));

    private final Pose retrieveSpecimenPose2_1 = new Pose(62, 20, Math.toRadians(0));

    private final Pose retrieveSpecimenPose2_2 = new Pose(62, 14, Math.toRadians(0));

    private final Pose retrieveSpecimenPose2_3 = new Pose(10, 14, Math.toRadians(0));

    private final Pose alignToPrepareForRetrieval3 = new Pose(10, 10, Math.toRadians(0));

    private final Pose retrieveSpecimenPose3_1 = new Pose(62, 10, Math.toRadians(0));

    private final Pose retrieveSpecimenPose3_2 = new Pose(62, 8, Math.toRadians(0));

    private final Pose retrieveSpecimenPose3_3 = new Pose(10, 8, Math.toRadians(0));

    /* These are our Paths and PathChains that we will define in buildPaths() */
    private PathChain moveToPrepToScorePreloadedSpecimen, scorePreloadedSpecimen,
                      goToPickupPositionAfterScoring, 
                      retrieveSpecimenMotion1, 
                      offsetPositionRight1, 
                      retrieveSpecimenMotion2, 
                      offsetPositionRight2, 
                      retrieveSpecimenMotion3;

    /** Build the paths for the auto (adds, for example, constant/linear headings while doing paths)
     * It is necessary to do this so that all the paths are built before the auto starts. **/
    public void buildPaths() {

        /* There are two major types of paths components: BezierCurves and BezierLines.
         *    * BezierCurves are curved, and require >= 3 points. There are the start and end points, and the control points.
         *    - Control points manipulate the curve between the start and end points.
         *    - A good visualizer for this is [this](https://pedro-path-generator.vercel.app/).
         *    * BezierLines are straight, and require 2 points. There are the start and end points.
         * Paths have can have heading interpolation: Constant, Linear, or Tangential
         *    * Linear heading interpolation:
         *    - Pedro will slowly change the heading of the robot from the startHeading to the endHeading over the course of the entire path.
         *    * Constant Heading Interpolation:
         *    - Pedro will maintain one heading throughout the entire path.
         *    * Tangential Heading Interpolation:
         *    - Pedro will follows the angle of the path such that the robot is always driving forward when it follows the path.
         * PathChains hold Path(s) within it and are able to hold their end point, meaning that they will holdPoint until another path is followed.
         * Here is a explanation of the difference between Paths and PathChains <https://pedropathing.com/commonissues/pathtopathchain.html> */

        /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        moveToPrepToScorePreloadedSpecimen = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(moveToPrepToScorePreloadedSpecimenPose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), moveToPrepToScorePreloadedSpecimenPose.getHeading())
                .build();

        scorePreloadedSpecimen = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scorePreloadedSpecimenPose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePreloadedSpecimenPose.getHeading())
                .build();

        goToPickupPositionAfterScoring = follower.pathBuilder()
                .addPath(new BezierLine(new Point(moveToPrepToScorePreloadedSpecimenPose), new Point(alignToPrepareForRetrieval1)))
                .setLinearHeadingInterpolation(moveToPrepToScorePreloadedSpecimenPose.getHeading(), alignToPrepareForRetrieval1.getHeading())
                .build();

        retrieveSpecimenMotion1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(alignToPrepareForRetrieval1), new Point(retrieveSpecimenPose1_1)))
                .setLinearHeadingInterpolation(alignToPrepareForRetrieval1.getHeading(), retrieveSpecimenPose1_1.getHeading())
                .addPath(new BezierLine(new Point(retrieveSpecimenPose1_1), new Point(retrieveSpecimenPose1_2)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose1_1.getHeading(), retrieveSpecimenPose1_2.getHeading())
                .addPath(new BezierLine(new Point(retrieveSpecimenPose1_2), new Point(retrieveSpecimenPose1_3)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose1_2.getHeading(), retrieveSpecimenPose1_3.getHeading())
                .build();

        offsetPositionRight1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(retrieveSpecimenPose1_3), new Point(alignToPrepareForRetrieval2)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose1_3.getHeading(), alignToPrepareForRetrieval2.getHeading())
                .build();

        retrieveSpecimenMotion2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(alignToPrepareForRetrieval2), new Point(retrieveSpecimenPose2_1)))
                .setLinearHeadingInterpolation(alignToPrepareForRetrieval2.getHeading(), retrieveSpecimenPose2_1.getHeading())
                .addPath(new BezierLine(new Point(retrieveSpecimenPose2_1), new Point(retrieveSpecimenPose2_2)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose2_1.getHeading(), retrieveSpecimenPose2_2.getHeading())
                .addPath(new BezierLine(new Point(retrieveSpecimenPose2_2), new Point(retrieveSpecimenPose2_3)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose2_2.getHeading(), retrieveSpecimenPose2_3.getHeading())
                .build();

        offsetPositionRight2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(retrieveSpecimenPose2_3), new Point(alignToPrepareForRetrieval3)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose2_3.getHeading(), alignToPrepareForRetrieval3.getHeading())
                .build();

        retrieveSpecimenMotion3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(alignToPrepareForRetrieval3), new Point(retrieveSpecimenPose3_1)))
                .setLinearHeadingInterpolation(alignToPrepareForRetrieval3.getHeading(), retrieveSpecimenPose3_1.getHeading())
                .addPath(new BezierLine(new Point(retrieveSpecimenPose3_1), new Point(retrieveSpecimenPose3_2)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose3_1.getHeading(), retrieveSpecimenPose3_2.getHeading())
                .addPath(new BezierLine(new Point(retrieveSpecimenPose3_2), new Point(retrieveSpecimenPose3_3)))
                .setLinearHeadingInterpolation(retrieveSpecimenPose3_2.getHeading(), retrieveSpecimenPose3_3.getHeading())
                .build();
    }

    /** This switch is called continuously and runs the pathing, at certain points, it triggers the action state.
     * Everytime the switch changes case, it will reset the timer. (This is because of the setPathState() method)
     * The followPath() function sets the follower to run the specific path, but does NOT wait for it to finish before moving on. */
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                if (!follower.isBusy()) {
                    follower.followPath(scorePreloadedSpecimen, true);
                    subsystemManager.topClaw.close();
                    subsystemManager.slides.slideToPosition(SlideState.MEDIUM);
                    boolean slidesAtPosition = false;
                    while (!slidesAtPosition) {
                        slidesAtPosition = subsystemManager.slides.slideToPosition(SlideState.MEDIUM);
                        subsystemManager.topClaw.close();
                    }
                }
                if (actionTimer.getElapsedTimeSeconds() > 5) {
                    setPathState(1);
                }
                telemetry.addLine("case 0");
                break;
            case 1:
                if (!follower.isBusy()) {
                    //follower.followPath(scorePreloadedSpecimen, true);
                    subsystemManager.topClaw.close();
                    if (actionTimer.getElapsedTimeSeconds() > 2) {
                        subsystemManager.slides.slideToPosition(SlideState.MEDIUM_SCORE);
                        boolean slidesAtPosition = false;
                        while (!slidesAtPosition) {
                            slidesAtPosition = subsystemManager.slides.slideToPosition(SlideState.MEDIUM_SCORE);
                            subsystemManager.topClaw.close();
                        }
                    }
                    if (actionTimer.getElapsedTimeSeconds() > 3) {
                        subsystemManager.topClaw.open();
                    }
                    if (actionTimer.getElapsedTimeSeconds() > 5) {
                        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
                        boolean slidesAtPosition = false;
                        while (!slidesAtPosition) {
                            slidesAtPosition = subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
                        }
                        setPathState(10);
                    }
                }
                telemetry.addLine("ONEEEEEEEEEEE");
                break;
            case 10:
                if (!follower.isBusy()) {
                    follower.followPath(goToPickupPositionAfterScoring, true);
                    setPathState(30);
                }
                break;
            case 30:
                if (!follower.isBusy()) {
                    follower.followPath(retrieveSpecimenMotion1, true);
                    setPathState(40);
                }
                break;
            case 40:
                if (!follower.isBusy()) {
                    follower.followPath(offsetPositionRight1, true);
                    setPathState(50);
                }
                break;
            case 50:
                if (!follower.isBusy()) {
                    follower.followPath(retrieveSpecimenMotion2, true);
                    setPathState(60);
                }
                break;
            case 60:
                if (!follower.isBusy()) {
                    follower.followPath(offsetPositionRight2, true);
                    setPathState(70);
                }
                break; 
            case 70:
                if (!follower.isBusy()) {
                    follower.followPath(retrieveSpecimenMotion3, true);
                    setPathState(80);
                }
                break;
        }
    }

    /** These change the states of the paths and actions
     * It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {

        // These loop the movements of the robot
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    /** This method is called once at the init of the OpMode. **/
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

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {}

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {
    }
}
