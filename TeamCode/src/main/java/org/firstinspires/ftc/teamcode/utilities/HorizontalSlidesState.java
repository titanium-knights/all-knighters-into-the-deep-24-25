package org.firstinspires.ftc.teamcode.utilities;

public enum HorizontalSlidesState {
    IN(0),
    TRANSFER(-150),
    OUT(-2350),
    A_LITTLE_OUT(-1480),
    ALMOST_OUT(-2000),
    MIDDLE(-1200),
    MID_OUT(-1483),
    TRANSFER_OUT_OF_SUBMERSIBLE(-800),
    STOP(200);

    private final int encoderValue;

    HorizontalSlidesState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}