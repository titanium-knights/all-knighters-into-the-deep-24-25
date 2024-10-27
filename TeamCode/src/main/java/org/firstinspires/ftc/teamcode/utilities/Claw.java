package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Claw {
    Servo clawOpener;
    Servo forearm;
    double servoAngleModifier = (double) 360 / 300;

    public static double PICKUP_POSITION = .33;
    public static double FOLDED_POSITION = 0;
    public static double DROP_POSITION = 0.8;
    public static double FOLDED_POSITION2 = .1;

    public Claw(HardwareMap hmap) {
        this.clawOpener = hmap.servo.get(CONFIG.clawServo);
        this.forearm = hmap.servo.get(CONFIG.forearm);
    }

    public void open() {
        clawOpener.setPosition(0.85);
    }

    public void close() {
        clawOpener.setPosition(1.0);
    }

    public void goToPickUpPosition() {
        forearm.setPosition(PICKUP_POSITION);
    }

    public void goToFoldedPosition() {
        forearm.setPosition(FOLDED_POSITION);
    }

    public void goToFoldedPosition2() {
        forearm.setPosition(FOLDED_POSITION2);
    }

    public void goToDropPosition() { forearm.setPosition(DROP_POSITION);}

    public double getPosition() {
        return clawOpener.getPosition() / servoAngleModifier;
    }
}