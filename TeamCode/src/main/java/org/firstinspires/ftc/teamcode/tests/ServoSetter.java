package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TEST-servo limits", group = "Tests")
public class ServoSetter extends OpMode {

    private Servo testingServo;
    public static String servoName = "testServo";

    @Override
    public void init() {
        testingServo = hardwareMap.servo.get(servoName);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            testingServo.setPosition(0);
        } else if (gamepad1.b) {
            testingServo.setPosition(1);
        }

        telemetry.addData("Servo Position", testingServo.getPosition());
        telemetry.update();
    }
}
