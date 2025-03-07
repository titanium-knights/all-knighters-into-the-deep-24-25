package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class HorizontalMidOutOpen extends AutonState {
    public HorizontalMidOutOpen(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.ALMOST_OUT);
        subsystemManager.bottomClaw.openClaw();
        subsystemManager.bottomClaw.rightWristDownPosition();

        if (subsystemManager.slides.getSlidesState() == SlideState.BOTTOM
                && subsystemManager.slides.isIdle()
                && subsystemManager.horizontalSlides.getSlidesState() == HorizontalSlidesState.ALMOST_OUT
                && subsystemManager.horizontalSlides.isIdle()
                && !subsystemManager.bottomClaw.isLoweredAndClosed()) {
            return true;
        } else {
            return false;
        }
    }
}
