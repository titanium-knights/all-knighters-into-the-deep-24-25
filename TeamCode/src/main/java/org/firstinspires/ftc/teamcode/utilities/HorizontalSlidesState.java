package org.firstinspires.ftc.teamcode.utilities;

public enum HorizontalSlidesState {
    IN(50),
    TRANSFER(-150),
    OUT(-2400),
    TRANSFER_OUT_OF_SUBMERSIBLE(-800);

    private final int encoderValue;

    HorizontalSlidesState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}