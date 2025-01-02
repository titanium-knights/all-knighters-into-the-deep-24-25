package org.firstinspires.ftc.teamcode.utilities;

public enum ScissorsState {
    IN(0),
    OUT(-50);

    private final int encoderValue;

    ScissorsState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}