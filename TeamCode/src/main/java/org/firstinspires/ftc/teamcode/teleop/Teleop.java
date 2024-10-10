package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Claw;

@TeleOp(name="Driver Teleop", group="default")
public class Teleop extends OpMode {

    private SimpleMecanumDrive drive;
    Claw claw;

    // in case of joystick drift, ignore very small values
    final float STICK_MARGIN = 0.5f;
    // Use full power by default, lower this value to slow down the robot
    final double normalPower = 1;

    @Override
    public void init() {
        drive = new SimpleMecanumDrive(hardwareMap);
        claw = new Claw(hardwareMap);
    }

    @Override
    public void loop() {
        move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        //open and close the claw
        if (gamepad1.left_bumper || gamepad2.left_bumper) {
            telemetry.addLine("DEBUG: claw should be open");
            claw.open();
        } else if (gamepad1.right_bumper || gamepad2.right_bumper) {
            claw.close();
        }
    }

    /**
     * Move the robot based on the joystick input
     * @param x side to side movement
     * @param y front and back movement
     * @param turn turning clockwise or counterclockwise
     */
    public void move(float x, float y, float turn) {
        // if the stick movement is negligible, don't move in that axis
        if (Math.abs(x) <= STICK_MARGIN) x = .0f;
        if (Math.abs(y) <= STICK_MARGIN) y = .0f;
        if (Math.abs(turn) <= STICK_MARGIN) turn = .0f;

        double multiplier = normalPower;
        drive.move(x * multiplier, y * multiplier, turn * multiplier);
    }
}
