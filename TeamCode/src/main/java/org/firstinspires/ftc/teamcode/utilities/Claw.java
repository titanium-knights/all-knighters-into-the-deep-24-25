package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Claw {
    Servo clawOpener;
    Servo forearm;
    double servoAngleModifier = (double) 360 / 300;

    // 0.0 is init, .55 is close to parallel
    public static double PICKUP_POSITION = .86;
    public static double FOLDED_POSITION = 0.1;
    public static double DROP_POSITION = 0.8;
    public static double SPECIMEN_POSITION = 0.7;

    public Claw(HardwareMap hmap) {
        this.clawOpener = hmap.servo.get(CONFIG.clawServo);
        this.forearm = hmap.servo.get(CONFIG.forearm);

        toFoldedPosition();
        close();
    }

    public void open() {
        clawOpener.setPosition(0.85);
    }

    public void close() {
        clawOpener.setPosition(1.0);
    }

    public void toPickUpPosition() {
        forearm.setPosition(PICKUP_POSITION);
    }

    public void toFoldedPosition() {
        forearm.setPosition(FOLDED_POSITION);
    }

    public void holdUp() {
        forearm.setPosition(1);
    }

    public void holdDown() {
        forearm.setPosition(.86);
    }

    public double getForearmPosition() {
        return forearm.getPosition();
    }

    public void toDropPosition() { forearm.setPosition(DROP_POSITION);}

    public void toSpecimenPosition() {forearm.setPosition(SPECIMEN_POSITION);}

    public double getPosition() {
        return clawOpener.getPosition() / servoAngleModifier;
    }
}