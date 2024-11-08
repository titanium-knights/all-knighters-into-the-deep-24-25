package org.firstinspires.ftc.teamcode.utilities;

public enum PullUpState {
    TOP(2000),
    BOTTOM(0);

    private final int encoderValue;

    PullUpState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}
