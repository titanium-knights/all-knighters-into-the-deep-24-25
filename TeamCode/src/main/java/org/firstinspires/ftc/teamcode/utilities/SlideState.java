package org.firstinspires.ftc.teamcode.utilities;

public enum SlideState {
    MANUALUP(0),
    MANUALDOWN(0),
    TOP(-2000),
    MEDIUM(-1100),
    MEDIUMSCORE(-850),
    BOTTOM(0);

    private final int encoderValue;

    SlideState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}
