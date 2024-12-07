package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.ActiveIntake;

@TeleOp(name="A Testing Teleop", group="default")
public class TestingTeleop extends OpMode {

    ActiveIntake activeIntake;
    @Override
    public void init() {
        activeIntake = new ActiveIntake(hardwareMap);
    }

    @Override
    public void loop() {

        if (gamepad1.dpad_up) {
            activeIntake.intake();
        } else if (gamepad1.dpad_down) {
            activeIntake.outtake();
        }
    }
}