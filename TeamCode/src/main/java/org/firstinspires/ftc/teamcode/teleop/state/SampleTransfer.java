package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.ScissorsState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class SampleTransfer extends TeleopState {
    public SampleTransfer(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) { // everything in neutral position
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.arm.toReceivingPos();
        subsystemManager.scissors.scissorsToPosition(ScissorsState.NEUTRAL);
        subsystemManager.bottomClaw.neutralClawRotatorPosition();
        subsystemManager.bottomClaw.rightWristUpPosition();
    }
}
