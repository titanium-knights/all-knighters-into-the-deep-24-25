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
        // subsystemManager.bottomClaw.rightWristInitPosition(); // move claw down so it's not in the way of bucket
        if (subsystemManager.slides.getSlidesState() == SlideState.TOP
                && subsystemManager.slides.isIdle()
                && subsystemManager.arm.inScoredPosition()) {
            return true;
        } else {
            return false;
        }
    }
}
