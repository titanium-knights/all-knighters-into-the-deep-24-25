package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ActiveIntake {


    HardwareDevice rightServo;
    HardwareDevice leftServo;

    public ActiveIntake(HardwareMap hmap) {
        rightServo = hmap.get("rightIntake");
        leftServo = hmap.get("leftIntake");

    }

    public void intakePositional() {
        ((Servo)rightServo).setPosition(1.0);
        ((Servo)leftServo).setPosition(0.0);
    }

    public void outtakePositional() {
        ((Servo)rightServo).setPosition(0.0);
        ((Servo)leftServo).setPosition(1.0);
    }

    public void intake() {
        ((CRServo)rightServo).setPower(1.0);
        ((CRServo)leftServo).setPower(-1.0);
    }

    public void outtake() {
        ((CRServo)rightServo).setPower(-1.0);
        ((CRServo)leftServo).setPower(1.0);
    }
}
