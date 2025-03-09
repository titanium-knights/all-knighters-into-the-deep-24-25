package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm {
    private final Servo armServo;

    private static final double INIT_POSITION = 0.3;
    private static final double RECEIVING_POSITION = 0.07;
    private static final double PRE_SCORE_SAMPLE_POS = 0.4;
    private static final double SCORE_SAMPLE_POS = 0.75;
    private static final double SCORE_SPECIMEN_POS = 0.80;
    private static final double LOW_HANG_POSITION = 0.73;
    private static final double GET_OUT_OF_WAY_POS = 0.8;
    private static final double RAISING_SLIDES_POSITION = 0.2;
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

    public void toPreScoreSamplePos() {armServo.setPosition(PRE_SCORE_SAMPLE_POS);}
    public void toScoreSamplePos() {
        armServo.setPosition(SCORE_SAMPLE_POS);
    }
    public void toScoreSpecimenPos() {armServo.setPosition(SCORE_SPECIMEN_POS);}
    public void toGetOutOfWay() {armServo.setPosition(GET_OUT_OF_WAY_POS);}

    public void toHangPosition() {
        armServo.setPosition(LOW_HANG_POSITION);
    }
    public void raisinPos() {armServo.setPosition(RAISING_SLIDES_POSITION);}

    public boolean inScoredSamplePosition() {
        return armServo.getPosition() == SCORE_SAMPLE_POS;
    }
    public boolean inScoredSpecimenPosition() {
        return armServo.getPosition() == SCORE_SPECIMEN_POS;
    }
    public boolean inPreScoreSamplePosition() {
        return armServo.getPosition() == PRE_SCORE_SAMPLE_POS;
    }
    public boolean inGetOutOfWayPosition() {
        return armServo.getPosition() == GET_OUT_OF_WAY_POS;
    }
    public boolean inReceivingPosition() {
        return armServo.getPosition() == RECEIVING_POSITION;
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
