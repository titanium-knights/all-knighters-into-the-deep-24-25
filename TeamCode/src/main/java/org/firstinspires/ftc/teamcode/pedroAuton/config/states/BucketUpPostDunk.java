package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class BucketUpPostDunk extends AutonState {
    public BucketUpPostDunk(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.slides.slideToPosition(SlideState.TOP); // slides move to top
        subsystemManager.arm.toScoreSamplePos();
        subsystemManager.topClaw.open();
        if (subsystemManager.slides.getSlidesState() == SlideState.TOP
                && subsystemManager.slides.isIdle()
                && subsystemManager.arm.inScoredSamplePosition()
                && subsystemManager.topClaw.getOpenStatus()) {
            return true;
        } else {
            return false;
        }
    }
}
