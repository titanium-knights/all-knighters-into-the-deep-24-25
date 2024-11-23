package org.firstinspires.ftc.teamcode.utilities;

public enum SlideState {
    MANUALUP(0),
    MANUALDOWN(0),
    TOP(-2200),
    MEDIUM(-450),
    MEDIUMSCORE(-100),
    BOTTOM(0);

    private final int encoderValue;

    SlideState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}
