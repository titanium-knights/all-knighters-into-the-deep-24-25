package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class BeforeSpecimenScore extends TeleopState {
    public BeforeSpecimenScore(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        subsystemManager.slides.slideToPosition(SlideState.MEDIUM); // slides extend to medium height
        subsystemManager.arm.toReceivingPos();
        //subsystemManager.scissors.moveToIdlePosition();
        subsystemManager.bottomClaw.neutralClawRotatorPosition();
        subsystemManager.bottomClaw.rightWristUpPosition();
        subsystemManager.bottomClaw.closeClaw();
    }
}
