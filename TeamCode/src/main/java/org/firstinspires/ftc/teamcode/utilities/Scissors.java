package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Scissors {

    Servo scissorsController;
    Servo scissorsControllerL;
    
    public static double extendPosition = 1.0;
    public static double transferPosition = 0.0;
    public static double neutralPosition = 0.5;

    public Scissors(HardwareMap hmap) {
        this.scissorsController = hmap.servo.get(CONFIG.scissorsServo);
    }

    public void extend() {
        scissorsController.setPosition(extendPosition);
        scissorsControllerL.setPosition(0.0);
    }

    public void transfer() {
        scissorsController.setPosition(transferPosition);
        scissorsControllerL.setPosition(0.0);
    }
    public void neutral() {
        scissorsController.setPosition(neutralPosition);
        scissorsControllerL.setPosition(0.0);
    }
}
