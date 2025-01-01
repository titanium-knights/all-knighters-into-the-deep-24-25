package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Scissors {

    private final Servo scissorsR;
    private final Servo scissorsL;

    public static double fullyExtendedPosition = 0.34; // fully extended position
    public static double fullyRetractedPosition = 0.0011; // transferring/loading position
    public static double idlePosition = 0.151; // not doing anything position

    public Scissors(HardwareMap hardwareMap) {
        this.scissorsR = hardwareMap.servo.get(CONFIG.scissorsR);
        this.scissorsL = hardwareMap.servo.get(CONFIG.scissorsL);
    }

    public void moveToFullyExtended() {
        scissorsR.setPosition(fullyExtendedPosition);
        scissorsL.setPosition(1.01 - fullyExtendedPosition);
    }

    public void moveToFullyRetracted() {
        scissorsR.setPosition(fullyRetractedPosition);
        scissorsL.setPosition(1 - fullyRetractedPosition);
    }

    public void moveToIdlePosition() {
        scissorsR.setPosition(idlePosition);
        scissorsL.setPosition(1 - idlePosition);
    }

    public double getPosition() {
        return scissorsR.getPosition();
    }
}