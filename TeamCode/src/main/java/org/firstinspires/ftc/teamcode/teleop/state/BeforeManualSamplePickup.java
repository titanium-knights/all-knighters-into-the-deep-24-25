package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.Teleop;
import org.firstinspires.ftc.teamcode.teleop.TeleopManual;
import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class BeforeManualSamplePickup extends TeleopState {
    public BeforeManualSamplePickup(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        Teleop.setSlowMode(true);
        TeleopManual.setSlowMode(true);
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.arm.toGetOutOfWay();
        if (gamepad1.dpad_right) {
            subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.OUT);
        } else if (gamepad1.dpad_down) {
            subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.IN);
        } else {
            subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.STOP);
        }
        subsystemManager.topClaw.open();
        subsystemManager.bottomClaw.rightWristDownPosition(); // claw is rotated down
    }
}