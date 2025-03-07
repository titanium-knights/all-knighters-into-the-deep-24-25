package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

@TeleOp(name = "WebcamTester", group = "Tests")
public class WebcamTester extends OpMode {

    SubsystemManager manager;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        manager = new SubsystemManager(hardwareMap);
    }

    @Override
    public void loop() {
        manager.drive.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        telemetry.addData("Status", "Running");
        telemetry.addData("Angle", manager.webcam2.getAngle());
        telemetry.update();
    }
}
