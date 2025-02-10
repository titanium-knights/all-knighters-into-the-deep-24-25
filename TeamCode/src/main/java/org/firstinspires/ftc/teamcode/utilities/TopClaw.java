package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class TopClaw {
    private final Servo clawOpener;
    private final static double servoAngleModifier = (double) 360 / 300;
    private boolean open = true;

    public static double openPos = 0.7;
    public static double closePos = 0.9;

    public TopClaw(HardwareMap hmap) {
        this.clawOpener = hmap.servo.get(CONFIG.topClawServo);
    }

    public void open() {
        clawOpener.setPosition(openPos);
        open = true;
    }

    public void close() {
        clawOpener.setPosition(closePos);
        open = false;
    }

    public boolean getOpenStatus() {
        return open;
    }

    public double getPosition() {
        return clawOpener.getPosition() / TopClaw.servoAngleModifier;
    }
}

