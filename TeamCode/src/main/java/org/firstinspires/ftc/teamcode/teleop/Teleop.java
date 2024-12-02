package org.firstinspires.ftc.teamcode.teleop;

import android.widget.Button;

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

    enum ButtonPressState {
       PRESSED_GOOD,
       DEPRESSED,
       UNPRESSED,
    }

    private ButtonPressState yButtonState = ButtonPressState.UNPRESSED;

    private SimpleMecanumDrive drive;
    private Claw claw;
    private Slides slides;
    private Arm arm;

    final float STICK_MARGIN = 0.5f;
    final double normalPower = 0.85;
    final double slowPower = 0.20;

    final int tickMax = 12000;

    final int slidesEncoderSlowModeBreakpoint = 800;

    boolean slowMode = false;
    int ticks = 0;
    @Override
    public void init() {
        // initialize util classes for hardware
        drive = new SimpleMecanumDrive(hardwareMap);
        claw = new Claw(hardwareMap);
        slides = new Slides(hardwareMap);
        arm = new Arm(hardwareMap);

        claw.goToFoldedPosition();
        claw.close();
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
            claw.goToFoldedPosition2();
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
        if (!gamepad1.y) {
            yButtonState = ButtonPressState.UNPRESSED;
        }

        if (gamepad1.y) {
            if (yButtonState == ButtonPressState.UNPRESSED) {
                yButtonState = ButtonPressState.PRESSED_GOOD;
                slowMode = !slowMode;
            } else if (yButtonState == ButtonPressState.PRESSED_GOOD) {
                yButtonState = ButtonPressState.DEPRESSED;
            }
        }

        if (gamepad1.b) {
            arm.toDropSpecimen();
            claw.goToFoldedPosition();
        } else if (gamepad1.x) {
            arm.toPickUpSamples();
            claw.goToPickUpPosition();
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

        if (x != .0f || y != .0f || turn != .0f) {
            ticks = Math.max(ticks + 1, tickMax);
        } else {
            ticks = 0;
        }

        double tickMultiplier = (ticks * ticks * 1.0) / tickMax / tickMax;

        double multiplier;
        if (slides.getEncoder() > slidesEncoderSlowModeBreakpoint || slowMode) {
            multiplier = slowPower;
        } else {
            multiplier = normalPower;
        }

        drive.move(x * multiplier * tickMultiplier, y * multiplier * tickMultiplier, turn * multiplier * tickMultiplier);
    }
}