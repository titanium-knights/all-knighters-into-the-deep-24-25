package org.firstinspires.ftc.teamcode.utilities;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class bottomClaw {
    Servo clawOpener;
    Servo clawRotator;
    Servo rightWristServo;

    public bottomClaw(HardwareMap hmap) {
        this.clawOpener = hmap.servo.get(CONFIG.clawServoBottom);
        this.clawRotator = hmap.servo.get(CONFIG.clawRotator);
        this.rightWristServo = hmap.get(Servo.class, "rightWrist");
    }

    public double getClawOpenerPosition() {
        return clawOpener.getPosition();
    }
    public double getClawRotatorPosition() {
        return clawRotator.getPosition();
    }
    public double getRightWristServoPosition() {
        return rightWristServo.getPosition();
    }

    public void openClaw() {
        clawOpener.setPosition(0.6);
    }
    public void openClawHalf() {clawOpener.setPosition(0.75);}
    public void closeClaw() {
        clawOpener.setPosition(0.9);
    }

    public void calibrateZeroClawRotator (double pos) {
        clawRotator.setPosition(pos);
    }
    public void bottomClawUpPositionPreset() {
        rightWristUpPosition();
        neutralClawRotatorPosition();
    }

    public void orthogonalClawRotatorPosition () {
        clawRotator.setPosition(.3);
    }
    public void neutralClawRotatorPosition () {
        clawRotator.setPosition(.7);
    }

    public void rightWristUpPosition() {
        rightWristServo.setPosition(0);
    }
    public void rightWristDownPosition() {
        rightWristServo.setPosition(.1);
    }

    public class OpenClaw implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            openClaw();
            return false;
        }
    }

    public Action openAction() {
        return new OpenClaw();
    }

    public class CloseClaw implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            closeClaw();
            return false;
        }
    }

    public Action closeAction() { return new CloseClaw(); }

}