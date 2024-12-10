package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.utilities.topClaw;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Config
@Autonomous(name="SlideOverAndAround", group="Auton")
public class SlideOver extends LinearOpMode {
    public static int crossTime = 1000;
    public static int crossBackTime = 1000;
    public static int straightenTime = 1000;

    private topClaw claw;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Initialized: ", "Hopefully");
        telemetry.update();

        final double POWER = 0.85;
        ElapsedTime runtime = new ElapsedTime();
        SimpleMecanumDrive drivetrain = new SimpleMecanumDrive(hardwareMap);


        waitForStart();
        runtime.reset();

        claw = new topClaw(hardwareMap);
        claw.close();
        sleep(1000);

        drivetrain.move(-POWER, POWER, 0);
        sleep(crossTime);
        telemetry.addLine("crossed robot now");
        telemetry.update();
        drivetrain.move(-POWER, -POWER, 0);
        sleep(crossBackTime);
        telemetry.addLine("crossed back now");
        telemetry.update();
        drivetrain.move(-POWER, 0, 0);
        sleep(straightenTime);
        telemetry.addLine("parked now :pray:");
        telemetry.update();
        drivetrain.move(0, 0, 0);

        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.update();
    }
}
