package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm {
    private Servo armServo;

    private static final double INIT_POSITION = 0.3;
    private static final double RECEIVING_POSITION = 0.1;
    private static final double SCORE_BUCKET_POSITION = 0.65;
    private static final double ARM_SPEED = 0.005;

    public Arm(HardwareMap hmap) {
        this.armServo = hmap.get(Servo.class, CONFIG.armServo);
    }

    public void toReceivingPos() {
        armServo.setPosition(RECEIVING_POSITION);
    }

    public void toInitPos() {
        armServo.setPosition(INIT_POSITION);
    }

    public void toScoreBucketPos() {
        armServo.setPosition(SCORE_BUCKET_POSITION);
    }

    public void manualUp(double power) {
        // Assume power is from 0.0 to 1.0
        double newPosition = armServo.getPosition() + (power * ARM_SPEED);
        // Ensure the new position is within [0.0, 1.0]
        newPosition = Math.min(newPosition, 1.0);
        armServo.setPosition(newPosition);
    }

    public void manualDown(double power) {
        // Assume power is from 0.0 to 1.0
        double newPosition = armServo.getPosition() - (power * ARM_SPEED);
        // Ensure the new position is within [0.0, 1.0]
        newPosition = Math.max(newPosition, 0.0);
        armServo.setPosition(newPosition);
    }

    public double getPosition() {
        return armServo.getPosition();
    }
}
