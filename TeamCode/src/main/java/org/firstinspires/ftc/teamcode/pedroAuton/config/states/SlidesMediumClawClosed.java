package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;


public class SlidesMediumClawClosed extends AutonState {

    public SlidesMediumClawClosed(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.slides.slideToPosition(SlideState.MEDIUM_SCORE); // slides move to top
        subsystemManager.topClaw.close();
        subsystemManager.arm.toScoreSpecimenPos();
        if (subsystemManager.slides.getSlidesState() == SlideState.MEDIUM_SCORE
                && subsystemManager.slides.isIdle()
                && !subsystemManager.topClaw.getOpenStatus()
                && subsystemManager.arm.inScoredSpecimenPosition()) {
            return true;
        } else {
            return false;
        }
    }
}
