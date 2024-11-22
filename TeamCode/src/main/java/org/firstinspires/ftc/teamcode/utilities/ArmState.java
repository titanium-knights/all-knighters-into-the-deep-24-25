package org.firstinspires.ftc.teamcode.utilities;

public enum ArmState {
    INIT(0),
    PICKUP(9000),
    DROP(2000),
    SPECIMEN(6000),
    MANUALINIT(0),
    MANUALPICKUP(0);

    private final int encoderValue;

    ArmState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}
