package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class Arm {
    Servo arm;
    private static final double BUFFER = 20; // buffer for position of arm
    public Arm(HardwareMap hmap) {
        this.arm = hmap.servo.get(CONFIG.armServo);
    }
    public void toPickUp() {
        arm.setPosition(0.0);
    }
    public void toInitPos() { arm.setPosition(1.0); }
    public void toScoreBucketPos() { arm.setPosition(0.5); }
    public void toScoreSpecimenPos() { arm.setPosition(0.4); }
    public double getPosition() { return arm.getPosition();}
}
