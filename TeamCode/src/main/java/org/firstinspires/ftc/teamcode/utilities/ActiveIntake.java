package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class ActiveIntake {

    //moving up is increasing right servo and decreasing left servo

    public static double downPosition = 0.8;
    public static double overBar = 0.6;
    public static double upPosition = 0.2;

    CRServo rightGrabberServo;
    CRServo leftGrabberServo;
    Servo rightWristServo;

    public ActiveIntake(HardwareMap hmap) {
        rightGrabberServo = hmap.get(CRServo.class, "rightIntake");
        leftGrabberServo = hmap.get(CRServo.class, "leftIntake");
        rightWristServo = hmap.get(Servo.class, "rightWrist");
    }

    public void outtake() {
        rightGrabberServo.setPower(1.0);
        leftGrabberServo.setPower(-1.0);
    }

    public void intake() {
        rightGrabberServo.setPower(-1.0);
        leftGrabberServo.setPower(1.0);
    }

    public void stop() {
        rightGrabberServo.setPower(0.0);
        leftGrabberServo.setPower(0.0);
    }

     public void bringUp() {
        rightWristServo.setPosition(upPosition);
     }

    public void bringDown() {
        rightWristServo.setPosition(downPosition);
    }

    public void bringOverBar() {
        rightWristServo.setPosition(overBar);
    }
}
