package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Scissors;
//import org.firstinspires.ftc.teamcode.utilities.ActiveIntake;
import org.firstinspires.ftc.teamcode.utilities.topClaw;
import org.firstinspires.ftc.teamcode.utilities.bottomClaw;
import org.firstinspires.ftc.teamcode.utilities.Arm;


@TeleOp(name="Testing Teleop", group="default")
public class TestingTeleop extends OpMode {
    Scissors scissors;
    //ActiveIntake activeIntake;
    topClaw topclaw;
    bottomClaw bottomclaw;
    Arm arm;
    @Override
    public void init() {
        scissors = new Scissors(hardwareMap);
        //activeIntake = new ActiveIntake(hardwareMap);
        topclaw = new topClaw(hardwareMap);
        bottomclaw = new bottomClaw(hardwareMap);
        arm = new Arm(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.x) {
            bottomclaw.open();
        } else if (gamepad1.y) {
            bottomclaw.close();
        }

        if (gamepad1.a) {

        } else if (gamepad1.b) {

        }

        if (gamepad1.dpad_left) {
            double ogPos = bottomclaw.getRotatorPosition();
            double newPos = ogPos + 0.005;
            bottomclaw.setClawRotator(newPos);
        } else if (gamepad1.dpad_right) {
            double ogPos = bottomclaw.getRotatorPosition();
            double newPos = ogPos - 0.005;
            bottomclaw.setClawRotator(newPos);
        }
        telemetry.addData("Claw Position2", bottomclaw.getRotatorPosition());

      
//        if (gamepad1.dpad_up) {
//            activeIntake.intake();
//        } else if (gamepad1.dpad_down) {
//            activeIntake.outtake();
//        } else if (gamepad1.dpad_right) {
//            activeIntake.stop();
//        }

//        else if (gamepad1.dpad_left) {
//            activeIntake.bringOverBar();
//        }

        if(gamepad1.left_bumper) {
            bottomclaw.bringUp();
        } else if (gamepad1.right_bumper) {
            bottomclaw.bringDown();
        }
        else if (gamepad1.back) {
            bottomclaw.neutralPosition();
        }

    }
}