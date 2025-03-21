package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class BeforeSpecimenScore extends TeleopState {
    public BeforeSpecimenScore(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.IN);
        subsystemManager.bottomClaw.neutralClawRotatorPosition();
        subsystemManager.bottomClaw.rightWristUpPosition();
        subsystemManager.bottomClaw.openClaw();

        if (!subsystemManager.bottomClaw.isClosed()) {
            subsystemManager.slides.slideToPosition(SlideState.MEDIUM_SCORE); // slides extend to medium height
            subsystemManager.arm.toScoreSpecimenPos();
        }
    }
}
