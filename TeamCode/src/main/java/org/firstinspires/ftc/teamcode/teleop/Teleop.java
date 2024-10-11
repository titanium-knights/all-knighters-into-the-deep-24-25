package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.Slides;

@TeleOp(name="Driver Teleop", group="default")
public class Teleop extends OpMode {

    private SimpleMecanumDrive drive;
    private Claw claw;
    private Slides slides;

    final float STICK_MARGIN = 0.5f;
    final double normalPower = 1;

    @Override
    public void init() {
        drive = new SimpleMecanumDrive(hardwareMap);
        claw = new Claw(hardwareMap); // Ensure claw is initialized
        slides = new Slides(hardwareMap); // Ensure slides are initialized
    }

    @Override
    public void loop() {
        move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // Claw controls
        if (gamepad1.left_bumper || gamepad2.left_bumper) {
            claw.open();
        } else if (gamepad1.right_bumper || gamepad2.right_bumper) {
            claw.close();
        }

        // Slides controls
        if (gamepad1.a || gamepad2.a) {
            slides.high();
        } else if (gamepad1.y || gamepad2.y) {
            slides.low();
        }
    }

    public void move(float x, float y, float turn) {
        // If the stick movement is negligible, set STICK_MARGIN to 0
        if (Math.abs(x) <= STICK_MARGIN) x = .0f;
        if (Math.abs(y) <= STICK_MARGIN) y = .0f;
        if (Math.abs(turn) <= STICK_MARGIN) turn = .0f;


        double multiplier = normalPower;
        drive.move(x * multiplier, y * multiplier, turn * multiplier);
    }
}