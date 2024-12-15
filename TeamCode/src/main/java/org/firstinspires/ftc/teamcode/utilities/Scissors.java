package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Scissors {

    Servo scissorsController;
    
    public static double extendPosition = 0.30;
    public static double transferPosition = 0.01;
    public static double neutralPosition = 0.1;

    public Scissors(HardwareMap hmap) {
        this.scissorsController = hmap.servo.get(CONFIG.scissorsServo);
    }

    public void extend() {
        scissorsController.setPosition(extendPosition);
    }

    public void transfer() {
        scissorsController.setPosition(transferPosition);
    }
    public void neutral() {scissorsController.setPosition(neutralPosition);}
}
