package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.acmerobotics.dashboard.config.Config;

public class specimenSweatshopFactory {
    Servo servoThatKeepsTheHookInPlace;
    Servo servoThatAppliesForceToHook;
    public specimenSweatshopFactory(HardwareMap hmap) {
        this.servoThatKeepsTheHookInPlace = hmap.servo.get(CONFIG.servoThatKeepsTheHookInPlace);
        this.servoThatAppliesForceToHook = hmap.servo.get(CONFIG.servoThatAppliesForceToHook);
    }
    public void holdingHookInPlace() {
        servoThatKeepsTheHookInPlace.setPosition(0.3);
    }
    public void notHoldingHookInPlace() {
        servoThatKeepsTheHookInPlace.setPosition(0.0);
    }
    public void applyForce() {
        servoThatAppliesForceToHook.setPosition(0.5);
    }
    public void notApplyingForce() {
        servoThatAppliesForceToHook.setPosition(0.0);
    }
}
