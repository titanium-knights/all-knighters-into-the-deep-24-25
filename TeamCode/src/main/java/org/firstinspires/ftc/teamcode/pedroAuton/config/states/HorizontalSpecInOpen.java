package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class HorizontalSpecInOpen extends AutonState {
    public HorizontalSpecInOpen(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.bottomClaw.openClaw();
        subsystemManager.bottomClaw.rightWristDownPosition();
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM); // slides move to top
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.IN);
        subsystemManager.arm.toGetOutOfWay();
        if (subsystemManager.slides.getSlidesState() == SlideState.BOTTOM
                && subsystemManager.slides.isIdle()
                && subsystemManager.horizontalSlides.getSlidesState() == HorizontalSlidesState.IN
                && subsystemManager.horizontalSlides.isIdle()
                && !subsystemManager.bottomClaw.isLoweredAndClosed()
                && subsystemManager.arm.inGetOutOfWayPosition()) {
            return true;
        } else {
            return false;
        }
    }
}
