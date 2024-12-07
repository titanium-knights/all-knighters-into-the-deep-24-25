package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Scissors {

    Servo scissorsController;
    
    public static double extendPosition = 1.0;
    public static double retractPosition = 0.15;

    public Scissors(HardwareMap hmap) {
        this.scissorsController = hmap.servo.get(CONFIG.scissorsServo);
    }

    public void extend() {
        scissorsController.setPosition(extendPosition);
    }

    public void retract() {
        scissorsController.setPosition(retractPosition);
    }
}
