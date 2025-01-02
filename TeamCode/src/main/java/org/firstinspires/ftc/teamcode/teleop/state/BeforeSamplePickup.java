package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.Teleop;
import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class BeforeSamplePickup extends TeleopState {
    public BeforeSamplePickup(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        Teleop.setSlowMode(true);
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.arm.toReceivingPos();
        //subsystemManager.scissors.moveToFullyExtended(); // scissors extend
        subsystemManager.bottomClaw.pickUpClawRotatorPosition();
        subsystemManager.bottomClaw.rightWristHalfUpPosition(); // claw is rotated down
        subsystemManager.bottomClaw.openClaw();
    }
}
