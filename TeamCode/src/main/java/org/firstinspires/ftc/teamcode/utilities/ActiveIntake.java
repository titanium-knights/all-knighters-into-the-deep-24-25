package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ActiveIntake {


    CRServo rightServo;
    CRServo leftServo;

    public ActiveIntake(HardwareMap hmap) {
        rightServo = hmap.get(CRServo.class, "rightIntake");
        leftServo = hmap.get(CRServo.class, "leftIntake");
    }

    public void intake() {
        rightServo.setPower(1.0);
        leftServo.setPower(-1.0);
    }

    public void outtake() {
        rightServo.setPower(-1.0);
        leftServo.setPower(1.0);
    }
}
