package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Arm;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.Slides;

@TeleOp(name="Driver Teleop", group="default")
public class Teleop extends OpMode {

    private SimpleMecanumDrive drive;
    private Claw claw;
    private Slides slides;
    private Arm arm;

    final float STICK_MARGIN = 0.5f;
    final double normalPower = 0.85;

    @Override
    public void init() {
        // initialize util classes for hardware
        drive = new SimpleMecanumDrive(hardwareMap);
        claw = new Claw(hardwareMap);
        slides = new Slides(hardwareMap);
        arm = new Arm(hardwareMap);

        claw.goToFoldedPosition();
    }

    @Override
    public void loop() {
        move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // Claw controls
        if (gamepad1.left_bumper || gamepad2.left_bumper) {
            claw.open();
            telemetry.addData("open", claw.getPosition());
        } else if (gamepad1.right_bumper || gamepad2.right_bumper) {
            claw.close();
            telemetry.addData("close", claw.getPosition());
        }
        // Slides controls
        if (gamepad1.dpad_up) {
            slides.upHold();
        } else if (gamepad1.dpad_down) {
            slides.downHold();
        } else {
            slides.stop();
        }

        // Arm controls (manual)
        if (gamepad1.b) {
            telemetry.addData("arm position", arm.getPosition());
            arm.setDirectionTowardsInit();
            claw.goToFoldedPosition();
        } else if (gamepad1.x) {
            telemetry.addData("arm position", arm.getPosition());
            arm.setDirectionAwayFromInit();
            claw.goToPickUpPosition();
        }

        // Arm controls (presets, to be tested)
        if (gamepad1.left_trigger > 0.0f) {
            arm.toPickUpSamples();
            claw.goToPickUpPosition();
        } else if (gamepad1.right_trigger > 0.0f) {
            arm.toFoldedPosition();
            claw.goToFoldedPosition();
        } else if (gamepad1.y) {
            arm.inlineWithSlides();
            claw.goToDropPosition();
        }
    }

    public void move(float x, float y, float turn) {
        // If the stick movement is negligible, ignore it
        if (Math.abs(x) <= STICK_MARGIN) x = .0f;
        if (Math.abs(y) <= STICK_MARGIN) y = .0f;
        if (Math.abs(turn) <= STICK_MARGIN) turn = .0f;

        double multiplier = normalPower;
        drive.move(x * multiplier, y * multiplier, turn * multiplier);
    }
}