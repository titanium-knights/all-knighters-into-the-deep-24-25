package org.firstinspires.ftc.teamcode.utilities;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class topClaw {
    Servo clawOpener;
    double servoAngleModifier = (double) 360 / 300;
    boolean open = true;

    public topClaw(HardwareMap hmap) {
        this.clawOpener = hmap.servo.get(CONFIG.clawServo);
    }

    public void open() {
        clawOpener.setPosition(0.6);
        open = true;
    }
    public void close() {
        clawOpener.setPosition(0.83);
        open = false;
    }

    public boolean openStatus() {
        return open;
    }

    public class OpenClaw implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            open();
            return false;
        }
    }

    public Action openAction() {
        return new OpenClaw();
    }



    public class CloseClaw implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            close();
            return false;
        }
    }
    public Action closeAction() { return new CloseClaw(); }

    public double getPosition() {
        return clawOpener.getPosition() / servoAngleModifier;
    }
}