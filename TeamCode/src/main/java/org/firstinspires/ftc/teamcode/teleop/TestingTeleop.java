package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Scissors;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

@TeleOp(name = "Testing Teleop", group = "User Control")
public class TestingTeleop extends OpMode {


    private SubsystemManager manager;

    @Override
    public void init() {
        manager = new SubsystemManager(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            manager.scissors.moveToIdlePosition();
            manager.bottomClaw.rightWristUpPosition();
            manager.bottomClaw.neutralClawRotatorPosition();
        } else if (gamepad1.b) {
            manager.scissors.moveToFullyExtended();
        } else if (gamepad1.x) {
            manager.scissors.moveToFullyRetracted();
        }
        if (gamepad1.left_bumper) {
            manager.bottomClaw.openClaw();
            manager.topClaw.open();
        } else if (gamepad1.right_bumper) {
            manager.bottomClaw.closeClaw();
            manager.topClaw.close();
        }
    }
}
