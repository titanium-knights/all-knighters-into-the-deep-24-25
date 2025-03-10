package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;


public class HorizontalTransferOpen extends AutonState {

    public HorizontalTransferOpen(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM); // slides move to top
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.TRANSFER);
        subsystemManager.bottomClaw.rightWristUpPosition();
        subsystemManager.bottomClaw.openClaw();
        subsystemManager.arm.toReceivingPos();
        // subsystemManager.bottomClaw.rightWristInitPosition(); // move claw down so it's not in the way of bucket
        if (subsystemManager.slides.getSlidesState() == SlideState.BOTTOM
                && subsystemManager.slides.isIdle()
                && subsystemManager.horizontalSlides.getSlidesState() == HorizontalSlidesState.TRANSFER
                && subsystemManager.horizontalSlides.isIdle()
                && subsystemManager.bottomClaw.inTransferPosition()
                && !subsystemManager.bottomClaw.isClosed()
                && subsystemManager.arm.inReceivingPosition()) {
            return true;
        } else {
            return false;
        }
    }
}
