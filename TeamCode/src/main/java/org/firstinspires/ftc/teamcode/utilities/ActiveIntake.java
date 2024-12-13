package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ActiveIntake {
    //right servo
    public static double downPosition = 0.0;
    public static double overBar = 0.5;
    public static double upPosition = 1.0;

    CRServo rightGrabberServo;
    CRServo leftGrabberServo;
    Servo leftWristServo;
    Servo rightWristServo;

    public ActiveIntake(HardwareMap hmap) {
        rightGrabberServo = hmap.get(CRServo.class, "rightGrabberIntake");
        leftGrabberServo = hmap.get(CRServo.class, "leftGrabberIntake");
        rightWristServo = hmap.get(Servo.class, "rightWristIntake");
    }

    public void intake() {
        rightGrabberServo.setPower(1.0);
        leftGrabberServo.setPower(-1.0);
    }

    public void outtake() {
        rightGrabberServo.setPower(-1.0);
        leftGrabberServo.setPower(1.0);
    }

    public void stop() {
        rightGrabberServo.setPower(0.0);
        leftGrabberServo.setPower(0.0);
    }

    public void wristToPosition() {
        rightWristServo.setPosition(0.2);
    }

     public void bringUp() {
        //moving up is increasing right servo and decreasing left servo
        rightWristServo.setPosition(upPosition);
     }

    public void bringDown() {
        //moving up is increasing right servo and decreasing left servo
        rightWristServo.setPosition(downPosition);
    }

    public void bringOverBar() {
        rightWristServo.setPosition(overBar);
    }


}
