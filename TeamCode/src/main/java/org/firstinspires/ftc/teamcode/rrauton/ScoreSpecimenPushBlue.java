package org.firstinspires.ftc.teamcode.rrauton;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.rr.MecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Arm;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.Slides;

// start at A2, ykwim
@Config
@Autonomous(name = "ScoreSpecimenPushBlue", group = "Autonomous")
public class ScoreSpecimenPushBlue extends LinearOpMode {
    private Claw claw;
    private Slides slides;
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
        waitForStart();

        if (isStopRequested()) return;
        Action trajectoryAction = tab.build();

        Actions.runBlocking(trajectoryAction);
    }
}
