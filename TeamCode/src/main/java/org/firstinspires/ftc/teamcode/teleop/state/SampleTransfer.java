package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class SampleTransfer extends TeleopState {
    public SampleTransfer(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) { // everything in neutral position
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.arm.toHangPosition();
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.TRANSFER);
        subsystemManager.bottomClaw.rightWristDownPosition();
        subsystemManager.bottomClaw.neutralClawRotatorPosition();
    }
}
