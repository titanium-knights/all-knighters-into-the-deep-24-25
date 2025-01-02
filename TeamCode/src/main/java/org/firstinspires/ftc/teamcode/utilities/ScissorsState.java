package org.firstinspires.ftc.teamcode.utilities;

public enum ScissorsState {
    IN(0),
    OUT(400);

    private final int encoderValue;

    ScissorsState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}