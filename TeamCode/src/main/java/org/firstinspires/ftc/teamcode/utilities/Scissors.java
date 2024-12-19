package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Scissors {

    private Servo scissorsServo;

    public static double fullyExtendedPosition = 1.0; // fully extended position
    public static double loadingPosition = 0.0; // transferring/loading position
    public static double idlePosition = 0.5; //not doing anything position

    public Scissors(HardwareMap hardwareMap) {
        this.scissorsServo = hardwareMap.servo.get(CONFIG.scissorsServo);
    }

    public void moveToFullyExtended() {
        scissorsServo.setPosition(fullyExtendedPosition);
    }

    public void moveToLoadingPosition() {
        scissorsServo.setPosition(loadingPosition);
    }

    public void moveToIdlePosition() {
        scissorsServo.setPosition(idlePosition);
    }
}
