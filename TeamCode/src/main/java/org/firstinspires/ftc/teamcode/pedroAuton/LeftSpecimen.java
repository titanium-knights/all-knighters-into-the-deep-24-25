package org.firstinspires.ftc.teamcode.pedroAuton;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;

@Autonomous(name = "Left Specimen", group = "Pedro Autons")
public class LeftSpecimen {
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
    private final Pose startPose = new Pose(10, 74, Math.toRadians(0));

    private final Pose scorePreloadedSpeimenPose = new Pose(36, 74, Math.toRadians(0));

    private final Pose parkAfterScoringSpecimenPose = new Pose(10, 16, Math.toRadians(0));


    /* These are our Paths and PathChains that we will define in buildPaths() */
    private PathChain scorePreloadedSpecimen, 
                      backOffAfterScoring,
                      parkAfterScoringSpecimen;

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
        scorePreloadedSpecimen = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scorePreloadedSpecimenPose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePreloadedSpecimenPose.getHeading())
                .build();

        backOffAfterScoring = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePreloadedSpecimenPose), new Point(startPose)))
                .setLinearHeadingInterpolation(scorePreloadedSpecimenPose.getHeading(), startPose.getHeading())
                .build();

        parkAfterScoringSpecimen = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(parkAfterScoringSpecimen)))
                .setLinearHeadingInterpolation(startPose.getHeading(), parkAfterScoringSpecimen.getHeading())
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
                    if (actionTimer.getElapsedTimeSeconds() <= 1) {
                        subsystemManager.slides.slideToPosition(SlideState.MEDIUM);
                    }
                    if (actionTimer.getElapsedTimeSeconds() > 1) {
                        subsystemManager.slides.slideToPosition(SlideState.MEDIUM_SCORE);
                    }
                    if (actionTimer.getElapsedTimeSeconds() > 1.5) {
                        subsystemManager.topClaw.open();
                    }
                    if (actionTimer.getElapsedTimeSeconds() > 2) {
                        setPathState(10);
                    }
                }
                break;
            case 10:
                if (!follower.isBusy()) {
                    follower.followPath(goToPickupPositionAfterScoring, true);
                    setPathState(20);
                }
                break;
            case 20:
                if (!follower.isBusy()) {
                    follower.followPath(backOffAfterScoring, true);
                    setPathState(30);
                }
                break;
            case 30:
                if (!follower.isBusy()) {
                    follower.followPath(parkAfterScoringSpecimen, true);
                    setPathState(40);
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
        opmodeTimer.resetTimer();

        subsystemManager = new SubsystemManager(hardwareMap, telemetry);

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
