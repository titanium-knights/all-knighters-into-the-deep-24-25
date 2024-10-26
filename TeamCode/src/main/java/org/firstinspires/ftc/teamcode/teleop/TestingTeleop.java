package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Slides;

@TeleOp(name="testing Teleop", group="default")
public class TestingTeleop extends OpMode {
    private Slides slides;

    @Override
    public void init() {
        slides = new Slides(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("slides position", slides.getEncoder());
        telemetry.update();
    }
}