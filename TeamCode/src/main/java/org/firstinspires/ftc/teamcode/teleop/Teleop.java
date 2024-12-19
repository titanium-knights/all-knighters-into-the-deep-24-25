package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class Teleop extends OpMode {
    private TeleopState currentState;
    private SubsystemManager subsystemManager;

    @Override
    public void init() {
        // instantiate all hardware util classes
        subsystemManager = new SubsystemManager(hardwareMap);
    }

    @Override
    public void loop() {
        subsystemManager.drive.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
    }
}
