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
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;


@Autonomous(name = "maxwell", group = "Examples")
public class Authentic extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    private SubsystemManager subsystemManager;// = new SubsystemManager(hardwareMap);
    private final Pose startP_HUMAN = new Pose(8, 64, Math.toRadians(0));

    private final Pose endP_Middle = new Pose(8, 20, Math.toRadians(0));

    private final Pose specimenScore = new Pose(36, 64, Math.toRadians(0));

    PathChain pathOne, pathTwo;

    public void buildPaths() {

        pathOne =
                follower.pathBuilder()
                        .addPath(new BezierLine(new Point(startP_HUMAN), new Point(specimenScore)))
                        .setLinearHeadingInterpolation(startP_HUMAN.getHeading(), specimenScore.getHeading())
                        .build();

        pathTwo = follower.pathBuilder()
                        .addPath(new BezierLine(new Point(specimenScore), new Point(endP_Middle)))
                        .setLinearHeadingInterpolation(
                                specimenScore.getHeading(), endP_Middle.getHeading())
                        .build();
    }


    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                telemetry.addLine("case 0");
                follower.followPath(pathOne);
                setPathState(1);
                break;
            case 1:
                telemetry.addLine("case 1");
                if (Math.abs(follower.getPose().getX() - (startP_HUMAN.getX())) < 1 && Math.abs(follower.getPose().getY() - startP_HUMAN.getY()) < 1) {
                    telemetry.addLine("wooo");
                    telemetry.update();
                    setPathState(2);
                }
                break;
            case 2:
                if (!follower.isBusy()) {
                    subsystemManager.topClaw.open();
                    setPathState(3);
                }
                break;
            case 3:
                telemetry.addLine("case 3");
                follower.followPath(pathTwo);
                setPathState(4);
                break;
            case 4:
                telemetry.addLine("case 4");
                if (Math.abs(follower.getPose().getX() - (endP_Middle.getX())) < 1 && Math.abs(follower.getPose().getY() - endP_Middle.getY()) < 1) {
                    telemetry.addLine("wooo");
                    telemetry.update();
                    setPathState(-1);
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
        subsystemManager = new SubsystemManager(hardwareMap);
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