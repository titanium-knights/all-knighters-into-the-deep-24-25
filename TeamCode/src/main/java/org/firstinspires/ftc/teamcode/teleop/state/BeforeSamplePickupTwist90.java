package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.Teleop;
import org.firstinspires.ftc.teamcode.teleop.TeleopManual;
import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class BeforeSamplePickupTwist90 extends TeleopState {
    public BeforeSamplePickupTwist90(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        Teleop.setSlowMode(true);
        TeleopManual.setSlowMode(true);
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.arm.toReceivingPos();
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.OUT);
        subsystemManager.bottomClaw.rightWristDownPosition();
        subsystemManager.bottomClaw.openClaw();
        subsystemManager.bottomClaw.orthogonalClawRotatorPosition(); // twist wrist 90 degrees
    }
}