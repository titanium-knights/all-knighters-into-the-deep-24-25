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
    public static int ANGLE_TEST = 60;
    public static int ANGLE_TEST_TWO = -1500;

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

        // Slides controls
        if (gamepad1.dpad_up) {
            slides.changeToUpState();
            telemetry.addData("up", slides.getEncoder());
        } else if (gamepad1.dpad_down) {
            slides.changeToDownState();
            telemetry.addData("down", slides.getEncoder());
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
        }

        telemetry.addData("arm position", arm.getPosition());
        telemetry.addData("Zero Power Behavior", slides.getZeroPowerBehavior());
        telemetry.addData("slides pos", slides.getEncoder());
        telemetry.update();
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