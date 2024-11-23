package org.firstinspires.ftc.teamcode.rrauton;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.rr.MecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Arm;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.SlideState;

// start at A2, ykwim
@Config
@Autonomous(name = "ScoreSpecimenPushBlue", group = "Autonomous")
public class ScoreSpecimenPushBlue extends LinearOpMode {
    private Claw claw;
    private Slides slides;
    private SlideState slideState;
    private Arm arm;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d begPose = new Pose2d(-60, 12, -Math.PI / 2.0);

        telemetry.addData("Initialized: ", "");
        telemetry.update();

        MecanumDrive drivetrain = new MecanumDrive(hardwareMap, begPose);

        claw = new Claw(hardwareMap);
        slides = new Slides(hardwareMap);
        arm = new Arm(hardwareMap);

        Actions.runBlocking(claw.closeAction());
        Actions.runBlocking(arm.toScoreSpecimenPosAction());
       // Actions.runBlocking(slideState.SlideUpAction(MEDIUM));

        TrajectoryActionBuilder tab = drivetrain.actionBuilder(begPose)
                        .lineToX(-36)
                        .lineToY(-36)
                        .lineToX(-12)
                        .lineToY(-48)
                        .lineToX(-60)
                        .lineToX(-12)
                        .lineToY(-60)
                        .lineToX(-60)
                        .lineToX(-12)
                        .lineToY(-66)
                        .lineToX(-60);

        TrajectoryActionBuilder specimenTab1 = drivetrain.actionBuilder(begPose)
                        .lineToY(0)
                        .lineToX(-36);
        // move the arm
        // move the slides
        // move forward
        TrajectoryActionBuilder specimenTab2 = drivetrain.actionBuilder(new Pose2d(-36, 0, -Math.PI / 2.0))
                        .lineToX(-30);
        // move the slides down
        // open the claw
        // move back to initial position
        TrajectoryActionBuilder specimenTab3 = drivetrain.actionBuilder(new Pose2d(-30, 0, -Math.PI / 2.0))
                        .lineToX(-60)
                        .lineToY(12);

        waitForStart();

        sleep(5000); // adjust if needed.

        if (isStopRequested()) return;
        Action trajectoryAction = tab.build();
        SequentialAction specimenPlaceAction = new SequentialAction(specimenTab1.build(), arm.toScoreSpecimenPosAction(), slides.getSlideAction(SlideState.MEDIUM), specimenTab2.build(), slides.getSlideAction(SlideState.MEDIUMSCORE), claw.openAction(), specimenTab3.build());

        Actions.runBlocking(new SequentialAction(
                specimenPlaceAction,
                trajectoryAction
        ));
    }
}
