package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class LowHang extends AutonState {
    public LowHang(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM); // slides move to top
        subsystemManager.bottomClaw.rightWristDownPosition();
        subsystemManager.bottomClaw.closeClaw();
        subsystemManager.arm.toHangPosition();
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.IN);
        // subsystemManager.bottomClaw.rightWristInitPosition(); // move claw down so it's not in the way of bucket
        if (subsystemManager.slides.getSlidesState() == SlideState.BOTTOM
                && subsystemManager.slides.isIdle()
                && subsystemManager.arm.inScoredSamplePosition()
                && subsystemManager.horizontalSlides.getSlidesState() == HorizontalSlidesState.IN
                && subsystemManager.horizontalSlides.isIdle()
                && subsystemManager.bottomClaw.isLoweredAndClosed()) {
            return true;
        } else {
            return false;
        }
    }
}