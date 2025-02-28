package org.firstinspires.ftc.teamcode.utilities;

public enum SlideState {
    MANUAL_UP(0),
    MANUAL_DOWN(0),
    TOP(-1900),
    MEDIUM(-1100),
    MEDIUM_SCORE(-800),
    MEDIUM_SCORE_TELEOP(-850),
    BOTTOM(0);

    private final int encoderValue;

    SlideState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}