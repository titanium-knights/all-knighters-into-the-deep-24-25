package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class SamplePickupTwist90 extends TeleopState {
    public SamplePickupTwist90(SubsystemManager subsystemManager, TeleopState[] dependencies) {
        super(subsystemManager, dependencies);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.arm.toReceivingPos();
        subsystemManager.scissors.moveToFullyExtended();
        subsystemManager.bottomClaw.rightWristDownPosition();
        subsystemManager.bottomClaw.orthogonalClawRotatorPosition(); // twist wrist 90 degrees
    }
}