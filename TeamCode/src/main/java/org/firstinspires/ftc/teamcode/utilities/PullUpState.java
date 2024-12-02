package org.firstinspires.ftc.teamcode.utilities;

public enum PullUpState {
    REACH_UP(2000),
    PULL_DOWN(0);
    private final int encoderValue;

    PullUpState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}
