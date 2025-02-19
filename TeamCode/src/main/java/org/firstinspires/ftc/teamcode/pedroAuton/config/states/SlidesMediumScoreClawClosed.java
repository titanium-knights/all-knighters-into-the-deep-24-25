package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;


public class SlidesMediumScoreClawClosed extends AutonState {

    public SlidesMediumScoreClawClosed(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.slides.slideToPosition(SlideState.MEDIUM_SCORE); // slides move to top
        subsystemManager.topClaw.close();
        // subsystemManager.bottomClaw.rightWristInitPosition(); // move claw down so it's not in the way of bucket
        if (subsystemManager.slides.getSlidesState() == SlideState.MEDIUM_SCORE && subsystemManager.topClaw.isClosed()) {
            return true;
        } else {
            return false;
        }
    }
}
