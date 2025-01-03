package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.ScissorsState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class SpecimenScore extends TeleopState{
    public SpecimenScore(SubsystemManager subsystemManager, TeleopState[] dependencies) {
        super(subsystemManager, dependencies);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        subsystemManager.slides.slideToPosition(SlideState.MEDIUM_SCORE); // slides move down to score specimen
        subsystemManager.arm.toReceivingPos();
        subsystemManager.scissors.scissorsToPosition(ScissorsState.IN);
        subsystemManager.bottomClaw.neutralClawRotatorPosition();
        subsystemManager.bottomClaw.rightWristUpPosition();
    }
}
