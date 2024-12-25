package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class SamplePickup extends TeleopState {
    public SamplePickup(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runToState(TeleopState state) {
        // no dependent states
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        subsystemManager.bottomClaw.openClaw();
        subsystemManager.bottomClaw.rightWristDownPosition();
        subsystemManager.bottomClaw.orthogonalClawRotatorPosition();
    }
}
