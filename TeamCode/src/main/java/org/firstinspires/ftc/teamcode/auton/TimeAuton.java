package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.utilities.Claw;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Config
@Autonomous(name="SlidetoRight", group="Auton")
public class TimeAuton extends LinearOpMode {
    public static int slideAmount = 1000;

    private Claw claw;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Initialized: ", "Hopefully");
        telemetry.update();

        final double POWER = 0.8;
        ElapsedTime runtime = new ElapsedTime();
        SimpleMecanumDrive drivetrain = new SimpleMecanumDrive(hardwareMap);

        waitForStart();

        // CONTROLS
        // drivetrain.move(POWER, 0, 0); // strafe left
        // drivetrain.move(-POWER, 0, 0); // strafe right
        // drivetrain.move(0, POWER, 0); // move forward
        // drivetrain.move(0, -POWER, 0); // move backward
        // idk how claw, arm, and slides work yet lmao

        // Leftmost block
        drivetrain.move(0, POWER, 0); // f1
        drivetrain.move(-POWER, 0, 0); // r1
        drivetrain.move(0, POWER, 0); // f1
        drivetrain.move(-POWER, 0, 0); // r1
        drivetrain.move(0, -POWER, 0); // b1
        drivetrain.move(POWER, 0, 0); // l1
        // Middle block
        drivetrain.move(0, POWER, 0); // f2
        drivetrain.move(-POWER, 0, 0); // r2
        drivetrain.move(0, POWER, 0); // f2
        drivetrain.move(-POWER, 0, 0); // r2
        drivetrain.move(0, -POWER, 0); // b2
        drivetrain.move(POWER, 0, 0); // l2
        // Rightmost block
        drivetrain.move(0, POWER, 0); // f3
        drivetrain.move(-POWER, 0, 0); // r3
        drivetrain.move(0, POWER, 0); // f3
        drivetrain.move(-POWER, 0, 0); // r3
        drivetrain.move(0, -POWER, 0); // b3
        drivetrain.move(POWER, 0, 0); // l3
        // Parking
        drivetrain.move(0, POWER, 0); // f
        drivetrain.move(-POWER, 0, 0); // r
        drivetrain.move(0, -POWER, 0); // b
    }
}
