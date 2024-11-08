package org.firstinspires.ftc.teamcode.utilities;

public enum ArmState {
    INIT(0),
    PICKUP(10200),
    DROP(2000),
    SPECIMEN(5500);

    private final int encoderValue;

    ArmState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}
