package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ActiveIntake {

    Servo rightServo;
    Servo leftServo;

    public ActiveIntake(HardwareMap hmap) {
        rightServo = hmap.servo.get("rightIntake");
        leftServo = hmap.servo.get("leftIntake");
    }

    public void intake() {
        rightServo.setPosition(1.0);
        leftServo.setPosition(0.0);
    }

    public void outtake() {
        rightServo.setPosition(0.0);
        leftServo.setPosition(1.0);
    }
}
