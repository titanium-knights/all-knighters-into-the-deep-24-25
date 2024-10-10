package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    Servo clawOpener;
    double servoAngleModifier = (double) 360 / 300;

    public Claw(HardwareMap hmap) {
        this.clawOpener = hmap.servo.get(CONFIG.clawServo);
    }

    public void open() {
        clawOpener.setPosition(0.0);
    }

    public void close() {
        clawOpener.setPosition(1.0);
    }

    public double getPosition() {
        return clawOpener.getPosition() / servoAngleModifier;
    }
}