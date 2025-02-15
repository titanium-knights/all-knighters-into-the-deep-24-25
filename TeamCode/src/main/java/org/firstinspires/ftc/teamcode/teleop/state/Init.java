package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class Init extends TeleopState {
    public Init(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) { // everything in init position

        subsystemManager.arm.toInitPos();

        subsystemManager.bottomClaw.neutralClawRotatorPosition();
        subsystemManager.bottomClaw.rightWristInitPosition();
        subsystemManager.bottomClaw.openClaw();
        subsystemManager.topClaw.open();

        if (gamepad1.right_stick_y > 0.1) { // Stick pushed down
            subsystemManager.slides.manualDown(0.5);
            subsystemManager.horizontalSlides.manualForward(0.5);
        }
        if (gamepad2.start) {
            subsystemManager.slides.resetSlideEncoder();
            subsystemManager.horizontalSlides.resetSlideEncoder();
        }
    }
}
