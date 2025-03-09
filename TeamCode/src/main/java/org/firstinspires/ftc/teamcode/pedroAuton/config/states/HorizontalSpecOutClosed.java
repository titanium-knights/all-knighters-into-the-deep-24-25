package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class HorizontalSpecOutClosed extends AutonState {
    public HorizontalSpecOutClosed(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.A_LITTLE_OUT);
        subsystemManager.bottomClaw.closeClaw();
        subsystemManager.bottomClaw.rightWristDownPosition();
        subsystemManager.arm.toGetOutOfWay();
        if (subsystemManager.slides.getSlidesState() == SlideState.BOTTOM
                && subsystemManager.slides.isIdle()
                && subsystemManager.horizontalSlides.getSlidesState() == HorizontalSlidesState.A_LITTLE_OUT
                && subsystemManager.horizontalSlides.isIdle()
                && subsystemManager.bottomClaw.isLoweredAndClosed()
                && subsystemManager.arm.inGetOutOfWayPosition()) {
            return true;
        } else {
            return false;
        }
    }
}
