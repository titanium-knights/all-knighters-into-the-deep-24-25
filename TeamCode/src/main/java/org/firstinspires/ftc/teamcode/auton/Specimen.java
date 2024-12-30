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
    private AutonState state = AutonState.START_DRIVE_TO_CHAMBER;
    private AutonState previousState = AutonState.START_DRIVE_TO_CHAMBER;
    private double timeOfLastAction = 0;
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
                                        new Point(15.165, 80.740, Point.CARTESIAN)))
                        .setTangentHeadingInterpolation()
                        .build();

        parkRobot =
                follower.pathBuilder()
                        .addPath(
                                // Line 2
                                new BezierCurve(
                                        new Point(36.123, 71.590, Point.CARTESIAN),
                                        new Point(43.123, 71.590, Point.CARTESIAN)))
                        .setTangentHeadingInterpolation()
//                        .addPath(
//                                // Line 3
//                                new BezierCurve(
//                                        new Point(77.665, 40.721, Point.CARTESIAN),
//                                        new Point(77.993, 18.554, Point.CARTESIAN),
//                                        new Point(8.374, 20.196, Point.CARTESIAN)))
//                        .setTangentHeadingInterpolation()
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
        telemetry.update();

        switch (state) {
            case RAISING_SLIDES:
                boolean slidesRaised = slides.slideToPosition(SlideState.MEDIUM);
                if (slidesRaised) {
                    state = AutonState.START_DRIVE_TO_CHAMBER;
                }
                break;
            case START_DRIVE_TO_CHAMBER:
                follower.followPath(driveToChamber);
                state = AutonState.DRIVING;
                previousState = AutonState.START_DRIVE_TO_CHAMBER;
                break;
            case LOWERING_SLIDES:
                boolean slidesLowered = slides.slideToPosition(SlideState.BOTTOM);

                if (timeOfLastAction!=0 && slidesLowered && (runtime.milliseconds() - timeOfLastAction > 1500)) {
                    state = AutonState.SCORING_SPECIMEN;
                    timeOfLastAction = 0; // IMPORTANT: reset the timeOfLastAction to zero when not in use
                } else {
                    if (timeOfLastAction==0) timeOfLastAction = runtime.milliseconds();
                }
                break;
            case SCORING_SPECIMEN:
                if (timeOfLastAction == 0) {
                    timeOfLastAction = runtime.milliseconds();
                    specimenClaw.open();
                } else if (runtime.milliseconds() - timeOfLastAction > 1500) {
                    state = AutonState.START_PARKING_ROBOT;
                    timeOfLastAction = 0; // IMPORTANT: reset the timeOfLastAction to zero when not in use
                }
                break;
            case START_PARKING_ROBOT:
                follower.followPath(parkRobot);
                state = AutonState.DRIVING;
                previousState = AutonState.START_PARKING_ROBOT;
                break;
            case DRIVING:
                if (follower.atParametricEnd()) {
                    if (previousState == AutonState.START_DRIVE_TO_CHAMBER) {
                        state = AutonState.LOWERING_SLIDES;
                    } else {
                        telemetry.addLine("Auton should be complete by now. Did it do everything it was supposed to?");
                    }
                } else {
                    telemetry.addData("Path Progress (%)", follower.getCurrentTValue() * 100.0);
                    telemetry.update();
                }
        }
    }

    // these will not necessarily be the same for all auton paths
    private enum AutonState {
        DRIVING,
        START_DRIVE_TO_CHAMBER,
        RAISING_SLIDES,
        LOWERING_SLIDES,
        SCORING_SPECIMEN,
        START_PARKING_ROBOT
    }
}
