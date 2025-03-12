package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class SwipeUp extends AutonState {

    public SwipeUp(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.MIDDLE); // slides move to top
        subsystemManager.swiper.up();
        // subsystemManager.bottomClaw.rightWristInitPosition(); // move claw down so it's not in the way of bucket
        if (subsystemManager.horizontalSlides.getSlidesState() == HorizontalSlidesState.MIDDLE && subsystemManager.horizontalSlides.isIdle()
            && subsystemManager.swiper.getUpStatus()) {
            return true;
        } else {
            return false;
        }
    }

}
