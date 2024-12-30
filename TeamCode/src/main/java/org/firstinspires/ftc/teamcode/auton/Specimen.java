package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.TopClaw;

/**
 * Move forward and score a specimen on the high chamber. Then, park on the right.
 *
 * @author Mudasir Ali
 * @version 12/29/2024
 */
@Autonomous(name = "SpecimenP", group = "Pedro Auton")
public class Specimen extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        ElapsedTime runtime = new ElapsedTime();
        Slides slides = new Slides(hardwareMap);
        TopClaw specimenClaw = new TopClaw(hardwareMap);

        Follower follower = new Follower(hardwareMap);

        // todo: tune these points based on the imprecision of the robot

        // Line 1
        PathChain driveToChamber = follower.pathBuilder()
                .addPath(
                        // Line 1
                        new BezierLine(
                                new Point(9.757, 84.983, Point.CARTESIAN),
                                new Point(11.668, 84.983, Point.CARTESIAN)))
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(90))
                .build();

        PathChain parkRobot = follower.pathBuilder()
                .addPath(
                        // Line 2
                        new BezierLine(
                                new Point(36.780, 74.381, Point.CARTESIAN),
                                new Point(36.616, 70.170, Point.CARTESIAN)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(270))
                .build();

        waitForStart();
        runtime.reset();
        follower.setPose(new Pose(9.757, 84.983, (int) Math.toRadians(90)));
        follower.update();
        sleep(600);
        boolean slidesRaised = slides.slideToPosition(SlideState.MEDIUM);
        while (opModeIsActive() && !slidesRaised) {
            slidesRaised = slides.slideToPosition(SlideState.MEDIUM);
            // follower.update(); // maybe wanna uncomment but prob not
        }
        follower.update();
        follower.followPath(driveToChamber);
        while (opModeIsActive() && !follower.atParametricEnd()) {
            follower.update();
        }

        slidesRaised = slides.slideToPosition(SlideState.BOTTOM);
        while (opModeIsActive() && !slidesRaised) {
            slidesRaised = slides.slideToPosition(SlideState.BOTTOM);
            // follower.update(); // maybe wanna uncomment but prob not
        }
        sleep(600);
        specimenClaw.open();
        sleep(1500);

        follower.update();
        follower.followPath(parkRobot);
        while (opModeIsActive() && !follower.atParametricEnd()) {
            follower.update();
        }
    }
}
