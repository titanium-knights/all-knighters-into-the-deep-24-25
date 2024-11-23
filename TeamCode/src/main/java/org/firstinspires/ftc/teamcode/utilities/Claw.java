package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Claw {
    Servo clawOpener;
    double servoAngleModifier = (double) 360 / 300;

    public Claw(HardwareMap hmap) {
        this.clawOpener = hmap.servo.get(CONFIG.clawServo);
        close();
    }

    public void open() {
        clawOpener.setPosition(1);
    }

    public void close() {
        clawOpener.setPosition(0.6);
    }

    public double getPosition() {
        return clawOpener.getPosition() / servoAngleModifier;
    }
}