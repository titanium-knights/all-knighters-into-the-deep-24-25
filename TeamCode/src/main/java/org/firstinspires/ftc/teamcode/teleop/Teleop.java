package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Arm;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.Slides;

@Config
@TeleOp(name="Driver Teleop", group="default")
public class Teleop extends OpMode {

    private SimpleMecanumDrive drive;
    private Claw claw;
    private Slides slides;
    private Arm arm;

    final float STICK_MARGIN = 0.5f;
    final double normalPower = 0.85;
    final double slowPower = 0.25;

    final int slidesEncoderSlowModeBreakpoint = 1500;

    int ticks = 0;
    @Override
    public void init() {
        // initialize util classes for hardware
        drive = new SimpleMecanumDrive(hardwareMap);
        claw = new Claw(hardwareMap);
        slides = new Slides(hardwareMap);
        arm = new Arm(hardwareMap);

        claw.goToFoldedPosition();
        slides.stop();
    }

    @Override
    public void loop() {
        move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // Claw controls
        if (gamepad1.left_bumper) {
            claw.open();
            telemetry.addData("open", claw.getPosition());
        } else if (gamepad1.right_bumper) {
            claw.close();
            telemetry.addData("close", claw.getPosition());
        }

        if (gamepad1.left_trigger > .1) {
            claw.goToPickUpPosition();
        } else if (gamepad1.right_trigger > .1) {
            claw.goToFoldedPosition();
        }

        // Slides controls
        if (gamepad1.dpad_up) {
            slides.changeToUpState();
            telemetry.addData("up", slides.getEncoder());
        } else if (gamepad1.dpad_down) {
            slides.changeToDownState();
            telemetry.addData("down", slides.getEncoder());
        } else {
            slides.stop();
        }
        
        // Arm controls (presets)
        if (gamepad1.b) {
            arm.toFoldedPosition();
            claw.goToFoldedPosition();
        } else if (gamepad1.x) {
            arm.toPickUpSamples();
            claw.goToPickUpPosition();
        } else if (gamepad1.y) {
            arm.inlineWithSlides();
            claw.goToDropPosition();
        } else if (gamepad1.a) {
            arm.toDropSamples();
            claw.goToPickUpPosition();
        }

        telemetry.addData("arm position", arm.getPosition());
        telemetry.addData("slides pos", slides.getEncoder());
        telemetry.update();
    }

    public void move(float x, float y, float turn) {
        // If the stick movement is negligible, ignore it
        if (Math.abs(x) <= STICK_MARGIN) x = .0f;
        if (Math.abs(y) <= STICK_MARGIN) y = .0f;
        if (Math.abs(turn) <= STICK_MARGIN) turn = .0f;

//        if (x != .0f || y != .0f) {
//            ticks = Math.max(ticks + 1, 1000)
//        } else {
//            ticks = 0;
//        }

        double multiplier;
        if (slides.getEncoder() > slidesEncoderSlowModeBreakpoint) {
            multiplier = slowPower;
        } else {
            multiplier = normalPower;
        }

        drive.move(x * multiplier, y * multiplier, turn * multiplier);
    }
}