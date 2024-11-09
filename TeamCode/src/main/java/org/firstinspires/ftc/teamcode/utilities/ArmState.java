package org.firstinspires.ftc.teamcode.utilities;

public enum ArmState {
    INIT(40),
    PICKUP(10200),
    DROP(2000),
    SPECIMEN(6300);

    private final int encoderValue;

    ArmState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}
