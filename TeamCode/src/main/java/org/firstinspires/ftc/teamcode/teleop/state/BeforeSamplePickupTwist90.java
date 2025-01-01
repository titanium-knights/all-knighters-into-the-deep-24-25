package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class BeforeSamplePickupTwist90 extends TeleopState {
    public BeforeSamplePickupTwist90(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.arm.toReceivingPos();
        subsystemManager.scissors.moveToFullyExtended();
        subsystemManager.bottomClaw.rightWristDownPosition();
        subsystemManager.bottomClaw.orthogonalClawRotatorPosition(); // twist wrist 90 degrees
        subsystemManager.bottomClaw.openClaw();
    }
}