package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum ArmState {
    INIT(40),
    PICKUP(10500),
    DROP(2000),
    SPECIMEN(6800);

    private final int encoderValue;

    ArmState(int encoderValue) {
        this.encoderValue = encoderValue;
    }

    public int getEncoderValue() {
        return encoderValue;
    }
}
//15055.6/360