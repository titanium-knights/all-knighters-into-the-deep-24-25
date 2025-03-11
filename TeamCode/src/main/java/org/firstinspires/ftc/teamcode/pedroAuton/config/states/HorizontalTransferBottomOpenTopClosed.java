package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;


public class HorizontalTransferBottomOpenTopClosed extends AutonState {

    public HorizontalTransferBottomOpenTopClosed(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.IN);
        subsystemManager.bottomClaw.rightWristUpPosition();
        subsystemManager.bottomClaw.openClaw();
        subsystemManager.arm.toReceivingPos();
        subsystemManager.topClaw.close();
        if (subsystemManager.slides.getSlidesState() == SlideState.BOTTOM
                && subsystemManager.slides.isIdle()
                && subsystemManager.horizontalSlides.getSlidesState() == HorizontalSlidesState.IN
                && subsystemManager.horizontalSlides.isIdle()
                && subsystemManager.bottomClaw.inTransferPosition()
                && !subsystemManager.bottomClaw.isClosed()
                && subsystemManager.arm.inReceivingPosition()
                && !subsystemManager.topClaw.getOpenStatus()) {
            return true;
        } else {
            return false;
        }
    }
}
