package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Swiper {
    private final Servo swiper;
    private final static double servoAngleModifier = (double) 360 / 300;
    private boolean open = true;

    public static double openPos = 1.0;
    public static double closePos = 0.65;

    public Swiper(HardwareMap hmap) {
        this.swiper = hmap.servo.get(CONFIG.swiper);
    }

    public void up() {
        swiper.setPosition(openPos);
        open = true;
    }

    public void down() {
        swiper.setPosition(closePos);
        open = false;
    }

    public boolean getUpStatus() {
        return open;
    }

    public double getPosition() {
        return swiper.getPosition() / Swiper.servoAngleModifier;
    }
}
