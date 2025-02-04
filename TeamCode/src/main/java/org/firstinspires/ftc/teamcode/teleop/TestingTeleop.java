package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.ScissorsState;
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

        if (gamepad1.x) {
            manager.bottomClaw.openClaw();
        } else if (gamepad1.y) {
            manager.bottomClaw.closeClaw();
        }

        if (gamepad1.dpad_left) {
            manager.bottomClaw.neutralClawRotatorPosition();
        } else if (gamepad1.dpad_right) {
            manager.bottomClaw.orthogonalClawRotatorPosition();
        }
//
//        if (gamepad1.a) {
//            manager.bottomClaw.rightWristUpPosition();
//        } else if (gamepad1.b) {
//            manager.bottomClaw.rightWristDownPosition();
//        }

        telemetry.addData("scissors encoder value", manager.scissors.getEncoder());
    }
}
