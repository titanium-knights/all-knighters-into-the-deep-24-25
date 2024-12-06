package org.firstinspires.ftc.teamcode.utilities;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


@Config
public class Arm {
    Servo arm;
    public Arm(HardwareMap hmap) {
        this.arm = hmap.servo.get(CONFIG.armServo);
    }
    public void beforePickUp() {
        arm.setPosition(0.60);
    }

    public static double specimenPickupPosition = 0.45;
    public static double specimenScorePosition = 0.3;

    public void pickingUpSpecimen() {
        arm.setPosition(specimenPickupPosition);
    }

    public void pickingUp() {
        arm.setPosition(0.68);
    }

    public void toInitPos() {
        arm.setPosition(0.01);
    }

    public void toScoreBucketPos() {
        arm.setPosition(0.25);
    }

    public void toLowScoreBucketPos() { arm.setPosition(0.20); }

    public void toScoreSpecimenPos() {
        arm.setPosition(specimenScorePosition);
    }

    public double getPosition() {
        return arm.getPosition();
    }

    public class toScoreSpecimenPosAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            toScoreSpecimenPos();
            return false;
        }
    }

    public class toInitPosAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            toInitPos();
            return false;
        }
    }

    public Action toInitPosAction() {
        return new toInitPosAction();
    }

    public Action toScoreSpecimenPosAction() { return new toScoreSpecimenPosAction(); }
}
