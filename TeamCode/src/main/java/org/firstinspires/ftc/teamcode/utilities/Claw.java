package org.firstinspires.ftc.teamcode.utilities;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    Servo clawOpener;
    Servo clawRotator;

    final double MAX_BUFFER = 2.0;

    double zeroLiftAngle = 110;
    double zeroClawAngle = -zeroLiftAngle;
    double servoAngleModifier = (double) 360/300;
    //Angle from bottom when lift is pointing straight up
    //calibrate from motor -> fusion 360

    public Claw(HardwareMap hmap) {
        this.clawOpener = hmap.servo.get(CONFIG.clawServo);
        this.clawRotator = hmap.servo.get(CONFIG.clawSpin);
    }

    public void open() {
        clawOpener.setPosition(0.0);
    }

    public void close() {
        clawOpener.setPosition(1.0);
    }

    public double getPosition() {
        return clawOpener.getPosition()/servoAngleModifier;
    }

    public void setPosition(double destinationAngle){
        double rawAngle = destinationAngle + zeroClawAngle;
        clawRotator.setPosition(1 - rawAngle/servoAngleModifier);
    }

    public void setZero(){
        clawRotator.setPosition(0);
    }

    public void setOne(){
        clawRotator.setPosition(1);
    }
}