package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Scissors {

    private Servo scissorsServo;
    private Servo secondScissorsServo; // New servo

    public static double fullyExtendedPosition = 1.0; // fully extended position
    public static double loadingPosition = 0.0; // transferring/loading position
    public static double idlePosition = 0.5; // not doing anything position

    public Scissors(HardwareMap hardwareMap) {
        this.scissorsServo = hardwareMap.servo.get(CONFIG.scissorsServo);
        this.secondScissorsServo = hardwareMap.servo.get(CONFIG.secondScissorsServo); // Initialize new servo
    }

    public void moveToFullyExtended() {
        scissorsServo.setPosition(fullyExtendedPosition);
        secondScissorsServo.setPosition(fullyExtendedPosition); // Move the second servo to the same position
    }

    public void moveToLoadingPosition() {
        scissorsServo.setPosition(loadingPosition);
        secondScissorsServo.setPosition(loadingPosition); // Move the second servo to the loading position
    }

    public void moveToIdlePosition() {
        scissorsServo.setPosition(idlePosition);
        secondScissorsServo.setPosition(idlePosition); // Move the second servo to idle position
    }
}