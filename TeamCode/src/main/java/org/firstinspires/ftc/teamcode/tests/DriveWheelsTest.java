package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.teleop.Teleop;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

@TeleOp(name="TEST-all wheels", group = "Tests")
public class DriveWheelsTest extends OpMode {

    private SubsystemManager manager;
    private boolean driveToggle = false;
    private boolean driveFast = false;

    @Override
    public void init() {

        manager = new SubsystemManager(hardwareMap, ConfidenceOrientationVectorPipeline.Color.RED, Teleop.Strategy.SAMPLE);
    }

    @Override
    public void loop() {

        if (gamepad1.x || gamepad2.x) {
            driveToggle = true;
        } else if (gamepad1.y || gamepad2.y) {
            driveToggle = false;
        }

        if (gamepad1.a || gamepad2.a) {
            driveFast = true;
        } else if (gamepad1.b || gamepad2.b) {
            driveFast = false;
        }

        if (driveToggle) {
            manager.drive.move(driveFast ? 0.8 : 0.3, 0,0);
        } else {
            manager.drive.move(0,0,0);
        }

        telemetry.clearAll();
        telemetry.addData("Drive Toggle", driveToggle ? "ON" : "OFF");
        telemetry.addData("Drive Speed", driveFast ? "FAST" : "SLOW");
        telemetry.update();
    }
}
