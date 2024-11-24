package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.acmerobotics.dashboard.config.Config;

public class specimenSweatshopFactory {
    Servo servoThatKeepsTheHookInPlace;
    Servo servoThatAppliesForceToHook;
    Servo servoThatRotatesThePoleAndOtherServo;
    Servo servoThatRotatesThePole;

    public specimenSweatshopFactory(HardwareMap hmap) {
        this.servoThatKeepsTheHookInPlace = hmap.servo.get(CONFIG.servoThatKeepsTheHookInPlace);
        this.servoThatAppliesForceToHook = hmap.servo.get(CONFIG.servoThatAppliesForceToHook);
        this.servoThatRotatesThePole = hmap.servo.get(CONFIG.servoThatRotatesThePole);
        this.servoThatRotatesThePoleAndOtherServo = hmap.servo.get(CONFIG.servoThatMovesThePoleAndOtherServo);
    }
    public void holdingHookInPlace() {
        servoThatKeepsTheHookInPlace.setPosition(0.2);
    }
    public void notHoldingHookInPlace() {
        servoThatKeepsTheHookInPlace.setPosition(0.4);
    }
    public void applyForce() {
        servoThatAppliesForceToHook.setPosition(0.5);
    }
    public void notApplyingForce() {
        servoThatAppliesForceToHook.setPosition(0.0);
    }
    public void gettingHooks() {
        servoThatRotatesThePoleAndOtherServo.setPosition(0.0);
        servoThatRotatesThePole.setPosition(0.0);
    }
    public void droppingHooksOff() {
        servoThatRotatesThePoleAndOtherServo.setPosition(0.5);
        servoThatRotatesThePole.setPosition(0.75);
    }
}
