package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Arm;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Slides;

@TeleOp(name="testing Teleop", group="default")
public class TestingTeleop extends OpMode {
    private Slides slides;
    private Arm arm;

    @Override
    public void init() {
        slides = new Slides(hardwareMap);
        arm = new Arm(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("arm position", arm.getPosition());
        telemetry.update();
    }
}