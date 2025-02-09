package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.ScissorsState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

@TeleOp(name = "Testing Teleop", group = "User Control")
public class TestingTeleop extends OpMode {


    private SubsystemManager manager;
    private boolean driveToggle = false;
    private boolean driveFast = false;

    @Override
    public void init() {

        manager = new SubsystemManager(hardwareMap);
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
    }
}
