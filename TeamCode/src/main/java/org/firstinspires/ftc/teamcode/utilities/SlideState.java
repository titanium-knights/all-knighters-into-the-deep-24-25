package org.firstinspires.ftc.teamcode.utilities;

public enum SlideState {
    MANUAL_UP(0),
    MANUAL_DOWN(0),
    TOP(-2100),
    MEDIUM_SCORE(-450),
    BOTTOM(0);

    private final int encoderValue;

    SlideState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}