package org.firstinspires.ftc.teamcode.teleop;

import android.widget.Button;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Scissors;

@TeleOp(name="testing Teleop", group="default")
public class TestingTeleop extends OpMode {
    private Scissors scissors;
    @Override
    public void init() {
        scissors = new Scissors(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.x) {
            scissors.extend();
        } else if (gamepad1.y) {
            scissors.retract();
        }

    }
}