package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class HorizontalOutOpenTwist extends AutonState {
    public HorizontalOutOpenTwist(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM); // slides move to top
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.OUT);
        subsystemManager.bottomClaw.openClaw();
        subsystemManager.bottomClaw.rightWristDownPosition();
        subsystemManager.bottomClaw.orthogonalClawRotatorPosition();
        // subsystemManager.bottomClaw.rightWristInitPosition(); // move claw down so it's not in the way of bucket
        if (subsystemManager.slides.getSlidesState() == SlideState.BOTTOM
                && subsystemManager.slides.isIdle()
                && subsystemManager.horizontalSlides.getSlidesState() == HorizontalSlidesState.OUT
                && subsystemManager.horizontalSlides.isIdle()
                && !subsystemManager.bottomClaw.isLoweredAndClosed()
                && subsystemManager.bottomClaw.inOrthoPos()) {
            return true;
        } else {
            return false;
        }
    }
}
