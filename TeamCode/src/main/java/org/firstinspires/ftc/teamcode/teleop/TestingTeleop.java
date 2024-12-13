package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Scissors;
import org.firstinspires.ftc.teamcode.utilities.ActiveIntake;

@TeleOp(name="A Testing Teleop", group="default")
public class TestingTeleop extends OpMode {
    Scissors scissors;
    ActiveIntake activeIntake;
    @Override
    public void init() {
        scissors = new Scissors(hardwareMap);
        activeIntake = new ActiveIntake(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.x) {
            scissors.extend();
        } else if (gamepad1.y) {
            scissors.retract();
        }
      
        if (gamepad1.dpad_up) {
            activeIntake.intake();
        } else if (gamepad1.dpad_down) {
            activeIntake.outtake();
        } else if (gamepad1.dpad_right) {
            activeIntake.stop();
        }
    }
}