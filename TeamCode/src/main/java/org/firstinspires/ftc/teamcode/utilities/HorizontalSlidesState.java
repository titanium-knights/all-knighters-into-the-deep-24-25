package org.firstinspires.ftc.teamcode.utilities;

public enum HorizontalSlidesState {
    IN(0),
    NEUTRAL(-300),
    OUT(-2250);

    private final int encoderValue;

    HorizontalSlidesState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}