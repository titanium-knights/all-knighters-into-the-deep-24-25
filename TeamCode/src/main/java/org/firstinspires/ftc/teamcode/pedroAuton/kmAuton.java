package org.firstinspires.ftc.teamcode.pedroAuton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.pedropathing.util.Constants;

import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;


@Autonomous(name = "kmauton", group = "Examples")
public class kmAuton extends OpMode{
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    private final Pose startP_HUMAN = new Pose(8, 64, Math.toRadians(0));

    //the y will need a change during tuning
    private final Pose pickupCloseP_HUMAN = new Pose(60, 28, Math.toRadians(270));
    private final Pose pickupMiddleP_HUMAN = new Pose(60, 18, Math.toRadians(270));
    private final Pose pickupFarP_HUMAN = new Pose(60, 8, Math.toRadians(270));

    //11 cuz 3 inch for sample + 8 inch for robot
    private final Pose placeCloseP_HUMAN = new Pose(11, 28, Math.toRadians(270));
    private final Pose placeMiddleP_HUMAN = new Pose(11, 18, Math.toRadians(270));
    //will also be used as park
    private final Pose placeFarP_HUMAN = new Pose(11, 8, Math.toRadians(270));

    //its hundred(angel) not by mistake, there is a chance the block would end up outside the line so angel it + same reasoning for 124 instead of 120
    private final Pose startControllP_HUMAN = new Pose(8, 36, Math.toRadians(0));
    private final Pose controllBeforeCloseP_HUMAN = new Pose(60, 36, Math.toRadians(0));

    private Path start_PATH, placeFar_PATH;
    private PathChain startControll_PATH, pickUpClose_PATH, placeClose_PATH, pickUpFar_PATH,
            moveToFar_PATH, placeMiddle_PATH, moveToMiddle_PATH, pickUpMiddle_PATH;


    public void buildPaths() {

        start_PATH = new Path(new BezierLine(new Point(startP_HUMAN), new Point(startControllP_HUMAN)));
        start_PATH.setLinearHeadingInterpolation(startP_HUMAN.getHeading(), startControllP_HUMAN.getHeading());

        startControll_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startControllP_HUMAN), new Point( controllBeforeCloseP_HUMAN )))
                .setLinearHeadingInterpolation(startControllP_HUMAN.getHeading(),  controllBeforeCloseP_HUMAN .getHeading())
                .build();

        pickUpClose_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(controllBeforeCloseP_HUMAN), new Point(pickupCloseP_HUMAN)))
                .setLinearHeadingInterpolation(controllBeforeCloseP_HUMAN.getHeading(), pickupCloseP_HUMAN.getHeading())
                .build();

        placeClose_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupCloseP_HUMAN), new Point(placeCloseP_HUMAN)))
                .setLinearHeadingInterpolation(pickupCloseP_HUMAN.getHeading(), placeCloseP_HUMAN.getHeading())
                .build();

        moveToMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeCloseP_HUMAN), new Point(pickupCloseP_HUMAN)))
                .setLinearHeadingInterpolation(placeCloseP_HUMAN.getHeading(), pickupCloseP_HUMAN.getHeading())
                .build();

        pickUpMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupCloseP_HUMAN), new Point(pickupMiddleP_HUMAN)))
                .setLinearHeadingInterpolation(pickupCloseP_HUMAN.getHeading(), pickupMiddleP_HUMAN.getHeading())
                .build();

        placeMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupMiddleP_HUMAN), new Point(placeMiddleP_HUMAN)))
                .setLinearHeadingInterpolation(pickupMiddleP_HUMAN.getHeading(), placeMiddleP_HUMAN.getHeading())
                .build();

        moveToFar_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeMiddleP_HUMAN), new Point(pickupMiddleP_HUMAN)))
                .setLinearHeadingInterpolation(placeMiddleP_HUMAN.getHeading(), pickupMiddleP_HUMAN.getHeading())
                .build();

        pickUpFar_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupMiddleP_HUMAN), new Point(pickupFarP_HUMAN)))
                .setLinearHeadingInterpolation(pickupMiddleP_HUMAN.getHeading(), pickupFarP_HUMAN.getHeading())
                .build();

        //basically park
        placeFar_PATH = new Path(new BezierLine(new Point(pickupFarP_HUMAN), new Point(placeFarP_HUMAN)));
        placeFar_PATH.setLinearHeadingInterpolation(pickupFarP_HUMAN.getHeading(), placeFarP_HUMAN.getHeading());
    }


    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                telemetry.addLine("case 0");
                follower.followPath(start_PATH);
                setPathState(1);
                break;
            case 1:
                telemetry.addLine("case 1");
                if(follower.getPose().getX() > (startControllP_HUMAN.getX() - 1) && follower.getPose().getY() > (startControllP_HUMAN.getY() - 1)) {
                    follower.followPath(startControll_PATH,true);
                    setPathState(2);
                }
                break;
            case -1:
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

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

    @Override
    public void init() {
        pathTimer = new Timer();
        Constants.setConstants(FConstants.class, LConstants.class);
        opmodeTimer = new Timer();

        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap);
        follower.setStartingPose(startP_HUMAN);

        buildPaths();
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {
    }
}