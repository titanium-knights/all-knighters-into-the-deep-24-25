package org.firstinspires.ftc.teamcode.utilities;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class bottomClaw {
    Servo clawOpener;
    Servo clawRotator;
    Servo rightWristServo;
    double servoAngleModifier = (double) 360 / 300;
    public static double downPosition = 0.75;
    public static double neutral = 0.1;
    public static double upPosition = 0.0;


    public bottomClaw(HardwareMap hmap) {
        this.clawOpener = hmap.servo.get(CONFIG.clawServoBottom);
        this.clawRotator = hmap.servo.get(CONFIG.clawRotator);
        this.rightWristServo = hmap.get(Servo.class, "rightWrist");
    }

    public void open() {
        clawOpener.setPosition(0.6);
    }
    public void close() {
        clawOpener.setPosition(0.9);
    }
    public void openHalf() {clawOpener.setPosition(0.75);}
    public double getRotatorPosition() {
        return clawRotator.getPosition();
    }

    public void setClawRotator (double pos) {
        clawRotator.setPosition(pos);
    }
    public void bringUp() {
        rightWristServo.setPosition(upPosition);
        clawRotator.setPosition(0.7);
     }

    public void bringDown() {
        rightWristServo.setPosition(downPosition);
    }

    public void neutralPosition() {
        rightWristServo.setPosition(neutral);
    }

    public void turn90 () {
        clawRotator.setPosition(0.3);
    }
    public void neutralPos () {
        clawRotator.setPosition(0.7);
    }

//    public class OpenClaw implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            open();
//            return false;
//        }
//    }
//
//    public Action openAction() {
//        return new OpenClaw();
//    }
//
//
//    public class CloseClaw implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            close();
//            return false;
//        }
//    }
//    public Action closeAction() { return new CloseClaw(); }

    public double getPosition() {
        return clawOpener.getPosition() / servoAngleModifier;
    }
}