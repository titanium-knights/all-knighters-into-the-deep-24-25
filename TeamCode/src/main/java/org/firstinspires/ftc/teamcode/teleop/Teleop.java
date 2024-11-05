package org.firstinspires.ftc.teamcode.teleop;

import android.widget.Button;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Arm;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.ArmState;

@Config
@TeleOp(name="Driver Teleop", group="default")
public class Teleop extends OpMode {

    enum ButtonPressState {
       PRESSED_GOOD,
       DEPRESSED,
       UNPRESSED,
    }

    private ButtonPressState yButtonState = ButtonPressState.UNPRESSED;
    private TeleopState teleopState = TeleopState.INIT;

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
    }

    @Override
    public void loop() {
        move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // Manual Claw controls
        if (gamepad1.left_bumper) {
            claw.open();
        } else if (gamepad1.right_bumper) {
            claw.close();
        }

        // Manual Slide controls
        if (gamepad1.dpad_up && teleopState != TeleopState.MANUAL_SLIDE_UP) {
            teleopState = TeleopState.MANUAL_SLIDE_UP;
        } else if (gamepad1.dpad_down && teleopState != TeleopState.MANUAL_SLIDE_DOWN) {
            teleopState = TeleopState.MANUAL_SLIDE_DOWN;
        }

        // Init Position (Start)
        if (gamepad1.start && teleopState != TeleopState.INIT) {
            teleopState = TeleopState.INIT;
        }

        // Pickup Position (A)
        if (gamepad1.a && teleopState != TeleopState.PICKUP) {
            teleopState = TeleopState.PICKUP;
        }

        // Drop Position (B)
        if (gamepad1.b && teleopState != TeleopState.DROP) {
            teleopState = TeleopState.DROP;
        }

        // Specimen Position (X)
        if (gamepad1.x && teleopState != TeleopState.SPECIMEN) {
            teleopState = TeleopState.SPECIMEN;
        }

        // Slow Mode Toggle (Y)
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

        // move to necessary positions
        goToPosition(teleopState);

        telemetry.addData("arm pos", arm.getEncoderValue());
        telemetry.addData("slides pos", slides.getEncoder());
        telemetry.addData("claw pos", claw.getPosition());
        telemetry.update();
    }

    private void goToPosition(TeleopState state) {
        if (state == TeleopState.INIT) {
            slides.slideToPosition(SlideState.BOTTOM);
            arm.runToPosition(ArmState.INIT);
            claw.toFoldedPosition();
        } else if (state == TeleopState.PICKUP) {
            slides.slideToPosition(SlideState.BOTTOM);
            arm.runToPosition(ArmState.PICKUP);
            claw.toPickUpPosition();
        } else if (state == TeleopState.DROP) {
            slides.slideToPosition(SlideState.TOP);
            arm.runToPosition(ArmState.DROP);
            claw.toDropPosition();
        } else if (state == TeleopState.SPECIMEN) {
            slides.slideToPosition(SlideState.MEDIUM);
            arm.runToPosition(ArmState.SPECIMEN);
            claw.toSpecimenPosition();
        } else if (state == TeleopState.MANUAL_SLIDE_UP) {
            slides.slideToPosition(SlideState.MANUALUP);
        } else if (state == TeleopState.MANUAL_SLIDE_DOWN) {
            slides.slideToPosition(SlideState.MANUALDOWN);
        }
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
        if (slides.getEncoder() < slidesEncoderSlowModeBreakpoint || slowMode) {
            multiplier = slowPower;
        } else {
            multiplier = normalPower;
        }

        drive.move(x * multiplier * tickMultiplier, y * multiplier * tickMultiplier, turn * multiplier * tickMultiplier);
    }
}