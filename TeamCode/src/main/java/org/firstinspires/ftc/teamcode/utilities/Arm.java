package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class Arm {
    Servo arm;
    public Arm(HardwareMap hmap) {
        this.arm = hmap.servo.get(CONFIG.armServo);
    }
  
    public void toPickUp() {
        arm.setPosition(0.45);
    }
    public void toInitPos() { arm.setPosition(-0.4); }
    public void toScoreBucketPos() { arm.setPosition(0.1); }
    public void toScoreSpecimenPos() { arm.setPosition(0.2); }
    public double getPosition() { return arm.getPosition();}
}
