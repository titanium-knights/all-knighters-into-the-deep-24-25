package org.firstinspires.ftc.teamcode.utilities;

public enum SlideState {
    MANUAL_UP(0),
    MANUAL_DOWN(0),
    TOP(-2000),
    MEDIUM_SCORE(-500),
    BOTTOM(0),
    BOTTOM_FULL(50);

    private final int encoderValue;

    SlideState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}