package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Autonomous(name="SlideOver", group="Auton")
public class SlideOver extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Initialized: ", "Hopefully");
        telemetry.update();

        final double POWER = 0.85;
        ElapsedTime runtime = new ElapsedTime();
        SimpleMecanumDrive drivetrain = new SimpleMecanumDrive(hardwareMap);

        waitForStart();
        runtime.reset();

        drivetrain.move(-POWER, -POWER, 0);

        sleep(1200);

        drivetrain.move(POWER, -POWER, 0);
        sleep(300);

        drivetrain.move(0, 0, 0);
        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.update();
    }
}
