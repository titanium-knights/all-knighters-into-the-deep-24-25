package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm {
    private final Servo armServo;

    private static final double INIT_POSITION = 0.3;
    private static final double RECEIVING_POSITION = 0.03;
    private static final double SCORE_BUCKET_POSITION = 0.65;
    private static final double RAISING_SLIDES_POSITION = 0.3;
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
    public void raisinPos() {armServo.setPosition(RAISING_SLIDES_POSITION);}

    public boolean inScoredPosition() {
        return armServo.getPosition() == SCORE_BUCKET_POSITION;
    }

    /**
     * Slowly move the arm up
     * @param power double in the range [0.0, 1.0]
     */
    public void manualUp(double power) {
        double newPosition = armServo.getPosition() + (power * ARM_SPEED);

        // Ensure the new position does not exceed 1.0
        newPosition = Math.min(newPosition, 1.0);
        armServo.setPosition(newPosition);
    }

    /**
     * Slowly move the arm down
     * @param power double in the range [0.0, 1.0]
     */
    public void manualDown(double power) {
        double newPosition = armServo.getPosition() - (power * ARM_SPEED);

        // Ensure the new position is not less than 0.0
        newPosition = Math.max(newPosition, 0.0);
        armServo.setPosition(newPosition);
    }

    public double getPosition() {
        return armServo.getPosition();
    }
}
