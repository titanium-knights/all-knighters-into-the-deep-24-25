package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.TopClaw;

/**
 * Move forward and score a specimen on the high chamber. Then, park on the right.
 *
 * @author Mudasir Ali
 * @version 12/27/2024
 */
@Autonomous(name = "SpecimenP", group = "Pedro Auton")
public class Specimen extends OpMode {

    ElapsedTime runtime;
    private Follower follower;
    private PathChain driveToChamber;
    private PathChain parkRobot;
    private Slides slides;
    private TopClaw specimenClaw;
    private AutonState state = AutonState.DRIVE_TO_CHAMBER;

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        runtime = new ElapsedTime();
        slides = new Slides(hardwareMap);
        specimenClaw = new TopClaw(hardwareMap);

        follower = new Follower(hardwareMap);

        // todo: tune these points based on the imprecision of the robot

        driveToChamber =
                follower.pathBuilder()
                        .addPath(
                                // Line 1
                                new BezierCurve(
                                        new Point(11.165, 83.740, Point.CARTESIAN),
                                        new Point(29.227, 70.276, Point.CARTESIAN),
                                        new Point(36.123, 71.590, Point.CARTESIAN)))
                        .setTangentHeadingInterpolation()
                        .build();

        parkRobot =
                follower.pathBuilder()
                        .addPath(
                                // Line 2
                                new BezierCurve(
                                        new Point(36.123, 71.590, Point.CARTESIAN),
                                        new Point(48.274, 58.290, Point.CARTESIAN),
                                        new Point(32.511, 8.702, Point.CARTESIAN),
                                        new Point(77.665, 40.721, Point.CARTESIAN)))
                        .setTangentHeadingInterpolation()
                        .addPath(
                                // Line 3
                                new BezierCurve(
                                        new Point(77.665, 40.721, Point.CARTESIAN),
                                        new Point(77.993, 18.554, Point.CARTESIAN),
                                        new Point(8.374, 20.196, Point.CARTESIAN)))
                        .setTangentHeadingInterpolation()
                        .build();
    }

    @Override
    public void start() {
        state = AutonState.RAISING_SLIDES;
        runtime.reset();
    }

    @Override
    public void loop() {
        follower.update();
        telemetry.addData("State", state);
        switch (state) {
            case RAISING_SLIDES:
                boolean slidesRaised = slides.slideToPosition(SlideState.MEDIUM);
                if (slidesRaised) {
                    state = AutonState.DRIVE_TO_CHAMBER;
                }
                break;
            case DRIVE_TO_CHAMBER:
                /* note: the reason for the while loop is so that this isn't called multiple times
                 * theoretically the while loop isn't a problem because the auton is short. However,
                 * if the auton does not stop when the stop button is pressed, this may be the cause
                 * an easy fix is to add another state (first time we START driving) and once
                 * that is run once, you would switch to this state that just waits until the path
                 * finishes
                 */
                follower.followPath(driveToChamber);
                while (!follower
                        .atParametricEnd()) { // FIXME: this is blocking and MAY cause problems
                    telemetry.clear();
                    telemetry.addData("Path Progress (%)", follower.getCurrentTValue() * 100.0);
                    telemetry.update();
                }
                state = AutonState.LOWERING_SLIDES;
                break;
            case LOWERING_SLIDES:
                boolean slidesLowered = slides.slideToPosition(SlideState.BOTTOM);
                if (slidesLowered) {
                    state = AutonState.SCORING_SPECIMEN;
                }
                break;
            case SCORING_SPECIMEN:
                specimenClaw.open();
                sleep(1000);
                state = AutonState.PARKING_ROBOT;
                break;
            case PARKING_ROBOT:
                // see previous note
                follower.followPath(parkRobot);
                while (!follower
                        .atParametricEnd()) { // FIXME: this is blocking and MAY cause problems
                    telemetry.clear();
                    telemetry.addData("Path Progress (%)", follower.getCurrentTValue() * 100.0);
                    telemetry.update();
                }
                break;
        }
    }

    private enum AutonState {
        DRIVE_TO_CHAMBER,
        RAISING_SLIDES,
        LOWERING_SLIDES,
        SCORING_SPECIMEN,
        PARKING_ROBOT
    }
}
