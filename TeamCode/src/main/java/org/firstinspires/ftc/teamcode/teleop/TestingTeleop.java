package org.firstinspires.ftc.teamcode.teleop;

import android.widget.Button;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Arm;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.specimenSweatshopFactory;

@TeleOp(name="testing Teleop", group="default")
public class TestingTeleop extends OpMode {
    private Slides slides;
    private specimenSweatshopFactory SpecimenSweatshopFactory;

    @Override
    public void init() {
        slides = new Slides(hardwareMap);
        SpecimenSweatshopFactory = new specimenSweatshopFactory(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("slides position", slides.getEncoder());
        telemetry.update();

        if (gamepad1.left_bumper) {
            SpecimenSweatshopFactory.applyForce();
        } else if (gamepad1.right_bumper) {
            SpecimenSweatshopFactory.notApplyingForce();
        }

        if (gamepad1.y) {
            SpecimenSweatshopFactory.holdingHookInPlace();
        } else if (gamepad1.x) {
            SpecimenSweatshopFactory.notHoldingHookInPlace();
        }


    }
}