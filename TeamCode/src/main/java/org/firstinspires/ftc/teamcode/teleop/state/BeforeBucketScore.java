package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.Teleop;
import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.ScissorsState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class BeforeBucketScore extends TeleopState {
    public BeforeBucketScore(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        Teleop.setSlowMode(true);
        subsystemManager.slides.slideToPosition(SlideState.TOP); // slides move to top
        subsystemManager.arm.toReceivingPos();
        subsystemManager.scissors.scissorsToPosition(ScissorsState.IN);
        subsystemManager.bottomClaw.neutralClawRotatorPosition();
        // subsystemManager.bottomClaw.rightWristInitPosition(); // move claw down so it's not in the way of bucket
    }
}
