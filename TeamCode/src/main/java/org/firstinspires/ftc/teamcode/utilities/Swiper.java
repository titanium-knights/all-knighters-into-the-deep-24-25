package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Swiper {
    private final Servo swiper;
    private final static double servoAngleModifier = (double) 360 / 300;
    private boolean open = true;

    public static double upPos = .75;
    public static double downPos = 0.25;

    public Swiper(HardwareMap hmap) {
        this.swiper = hmap.servo.get(CONFIG.swiperServo);
    }

    public void up() {
        swiper.setPosition(upPos);
        open = true;
    }

    public void down() {
        swiper.setPosition(downPos);
        open = false;
    }

    public boolean getUpStatus() {
        return open;
    }

    public double getPosition() {
        return swiper.getPosition() / Swiper.servoAngleModifier;
    }
}