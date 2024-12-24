package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Scissors {

    Servo scissorsController;
    Servo scissorsControllerL;
    
    public static double extendPosition = 0.35;
    public static double transferPosition = 0.0011;
    public static double neutralPosition = 0.15;

    public Scissors(HardwareMap hmap) {
        this.scissorsController = hmap.servo.get(CONFIG.scissorsServo);
        this.scissorsControllerL = hmap.servo.get(CONFIG.scissorsServoL);
    }

    public void extend() {
        scissorsController.setPosition(extendPosition);
        scissorsControllerL.setPosition(1-extendPosition);
    }

    public void init() {
        scissorsController.setPosition(transferPosition);
        scissorsControllerL.setPosition(1-transferPosition);
    }
    public void neutral() {
        scissorsController.setPosition(neutralPosition);
        scissorsControllerL.setPosition(1-neutralPosition);
    }

    public double getPosition() {
        return scissorsController.getPosition();

    }
}
