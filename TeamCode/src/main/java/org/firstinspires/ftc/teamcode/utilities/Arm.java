package org.firstinspires.ftc.teamcode.utilities;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Arm {
    private Servo arm;
    private static final double ARM_SPEED = 0.005; // Adjust this value as needed

    // Arm positions
    public static double receivingPosition = 0.1;
    public static double scoreBucketPosition = 0.65;



    public Arm(HardwareMap hmap) {
        this.arm = hmap.get(Servo.class, CONFIG.armServo);
    }

    // Preset positions
    public void receivingPos() {
        arm.setPosition(receivingPosition);
    }

    public void initPos () {
        arm.setPosition(0.3);
    }

    public void toScoreBucketPos() {
        arm.setPosition(scoreBucketPosition);
    }




    public double getPosition() {
        return arm.getPosition();
    }

    // Manual control methods accepting controller values
    public void manualUp(double power) {
        // Assume power is from 0.0 to 1.0
        double newPosition = arm.getPosition() + (power * ARM_SPEED);
        // Ensure the new position is within [0.0, 1.0]
        newPosition = Math.min(newPosition, 1.0);
        arm.setPosition(newPosition);
    }

    public void manualDown(double power) {
        // Assume power is from 0.0 to 1.0
        double newPosition = arm.getPosition() - (power * ARM_SPEED);
        // Ensure the new position is within [0.0, 1.0]
        newPosition = Math.max(newPosition, 0.0);
        arm.setPosition(newPosition);
    }

    public void stop() {
        // For a servo, stopping may not be necessary
    }

    // Action classes (if needed)


    public class toInitPosAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            receivingPos();
            return false;
        }
    }

    public Action toInitPosAction() {
        return new toInitPosAction();
    }


}
