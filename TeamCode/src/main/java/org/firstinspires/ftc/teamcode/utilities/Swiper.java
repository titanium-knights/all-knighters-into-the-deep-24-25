package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Swiper {

    private static final double MIN_POSITION = 0.25;
    private static final double MAX_POSITION = 0.75;

    private final Servo swiperServo;

    public Swiper(HardwareMap hmap) {
        this.swiperServo = hmap.get(Servo.class, "swiperServo");
    }

    public void stop() {
        swiperServo.setPosition(MIN_POSITION);
    }

    public void retract() {
        swiperServo.setPosition(MIN_POSITION);
    }

    public void extend() {
        swiperServo.setPosition(MAX_POSITION);
    }

    public boolean isAtPosition(double position) {
        return Math.abs(swiperServo.getPosition() - position) < 0.01;
    }
}