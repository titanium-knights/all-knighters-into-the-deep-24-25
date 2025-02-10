package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TEST-servo analog", group = "Tests")
public class ServoAnalog extends OpMode {

    private Servo testingServo;
    public static String servoName = "testServo";
    private double servoPosition = 0;

    @Override
    public void init() {
        testingServo = hardwareMap.servo.get(servoName);
    }

    @Override
    public void loop() {
        testingServo.setPosition(servoPosition);

        if (gamepad1.left_trigger > 0) {
            servoPosition = Math.max(0, servoPosition - 0.005);
        } else if (gamepad1.right_trigger > 0) {
            servoPosition = Math.min(1, servoPosition + 0.005);
        }

        telemetry.addData("Servo Position", servoPosition);
        telemetry.update();
    }
}
