package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class SwipeFold extends AutonState {

    public SwipeFold(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.IN); // slides move to top
        subsystemManager.swiper.up();
        if (subsystemManager.horizontalSlides.getSlidesState() == HorizontalSlidesState.IN && subsystemManager.horizontalSlides.isIdle()) {
            return true;
        } else {
            return false;
        }
    }

}
