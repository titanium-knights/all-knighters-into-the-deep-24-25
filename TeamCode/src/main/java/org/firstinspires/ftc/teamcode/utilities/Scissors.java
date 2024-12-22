package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Scissors {

    private Servo scissorsR;
    private Servo scissorsL;

    public static double fullyExtendedPosition = 1.0; // fully extended position
    public static double loadingPosition = 0.0; // transferring/loading position
    public static double idlePosition = 0.5; // not doing anything position

    public Scissors(HardwareMap hardwareMap) {
        this.scissorsR = hardwareMap.servo.get(CONFIG.scissorsR);
        this.scissorsL = hardwareMap.servo.get(CONFIG.scissorsL);
    }

    public void moveToFullyExtended() {
        scissorsR.setPosition(fullyExtendedPosition);
        scissorsL.setPosition(1 - fullyExtendedPosition);
    }

    public void moveToLoadingPosition() {
        scissorsR.setPosition(loadingPosition);
        scissorsL.setPosition(1-loadingPosition);
    }

    public void moveToIdlePosition() {
        scissorsR.setPosition(idlePosition);
        scissorsL.setPosition(idlePosition);
    }
}