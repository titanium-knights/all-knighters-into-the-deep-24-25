package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.Teleop;
import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.ScissorsState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class SamplePickup extends TeleopState {
    public SamplePickup(SubsystemManager subsystemManager, TeleopState[] dependencies) {
        super(subsystemManager, dependencies);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        Teleop.setSlowMode(true);
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.arm.toReceivingPos();
        subsystemManager.scissors.scissorsToPosition(ScissorsState.OUT);
        subsystemManager.bottomClaw.rightWristDownPosition(); // wrists go down
        subsystemManager.bottomClaw.closeClaw(); // claw closes
    }
}
