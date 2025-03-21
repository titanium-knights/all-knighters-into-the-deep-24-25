package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;


public class InitAuton extends AutonState {

    public InitAuton(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM); // slides move to top
        subsystemManager.topClaw.close();
        subsystemManager.arm.raisinPos();

        if (subsystemManager.slides.getSlidesState() == SlideState.BOTTOM && subsystemManager.slides.isIdle() && !subsystemManager.topClaw.getOpenStatus()) {
            return true;
        } else {
            return false;
        }
    }
}
