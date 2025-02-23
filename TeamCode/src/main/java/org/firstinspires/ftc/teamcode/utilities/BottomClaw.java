package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class BottomClaw {

    public static final double OPEN_CLAW_POSITION = 0.1;
    public static final double OPEN_CLAW_HALF_POSITION = 0.2;
    public static final double CLOSE_CLAW_POSITION = 0.4;

    public static double TESTING_THETA = 0.0;

    public static final double ORTHOGONAL_CLAW_ROTATOR_POSITION = 0.3;
    public static final double NEUTRAL_CLAW_ROTATOR_POSITION = 0.68;
    public static final double PICKUP_CLAW_ROTATOR_POSITION = 0.0;

    public static final double RIGHT_WRIST_UP_POSITION = 0.0;
    public static final double RIGHT_WRIST_DOWN_POSITION = 0.75;
    public static final double RIGHT_WRIST_HALFUP_POSITION = 0.7;
    public static final double RIGHT_WRIST_INIT_POSITION = 0.2;


    Servo clawOpener;
    Servo clawRotator;
    Servo rightWristServo;

    public BottomClaw(HardwareMap hmap) {
        this.clawOpener = hmap.servo.get(CONFIG.clawServoBottom);
        this.clawRotator = hmap.servo.get(CONFIG.clawRotator);
        this.rightWristServo = hmap.servo.get(CONFIG.rightWristServo);
    }

    // rotates claw to position at theta radians
    public void rotate(double theta) {
        this.clawRotator.setPosition(theta * 0.68 / (Math.PI));
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
        clawOpener.setPosition(OPEN_CLAW_POSITION);
    }

    public void openClawHalf() {
        clawOpener.setPosition(OPEN_CLAW_HALF_POSITION);
    }

    public void closeClaw() {
        clawOpener.setPosition(CLOSE_CLAW_POSITION);
    }

    public void calibrateZeroClawRotator(double pos) {
        clawRotator.setPosition(pos);
    }

    public void bottomClawUpPositionPreset() {
        rightWristUpPosition();
        neutralClawRotatorPosition();
    }

    public boolean isLoweredAndClosed() {
        return getRightWristServoPosition() == RIGHT_WRIST_DOWN_POSITION
                && getClawOpenerPosition() == CLOSE_CLAW_POSITION;
    }
    public boolean inOrthoPos() {
        return getClawOpenerPosition() == ORTHOGONAL_CLAW_ROTATOR_POSITION;
    }

    public boolean inTransferPosition() {
        return getRightWristServoPosition() == RIGHT_WRIST_UP_POSITION;
    }

    public boolean isClosed() {
        return getClawOpenerPosition() == CLOSE_CLAW_POSITION;
    }

    public void orthogonalClawRotatorPosition() {
        clawRotator.setPosition(ORTHOGONAL_CLAW_ROTATOR_POSITION);
    }

    public void neutralClawRotatorPosition() {
        clawRotator.setPosition(NEUTRAL_CLAW_ROTATOR_POSITION);
    }

    public void pickUpClawRotatorPosition() {
        clawRotator.setPosition(PICKUP_CLAW_ROTATOR_POSITION);
    }

    public void rightWristUpPosition() {
        rightWristServo.setPosition(RIGHT_WRIST_UP_POSITION);
    }

    public void rightWristDownPosition() {
        rightWristServo.setPosition(RIGHT_WRIST_DOWN_POSITION);
    }
    public void rightWristHalfUpPosition() {rightWristServo.setPosition(RIGHT_WRIST_HALFUP_POSITION);}
    public void rightWristInitPosition() {rightWristServo.setPosition(RIGHT_WRIST_INIT_POSITION);}
}