package org.firstinspires.ftc.teamcode.pedroAuton.config.states;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class HorizontalTransferClosed extends AutonState {
    public HorizontalTransferClosed(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public boolean update() {
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM); // slides move to top
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.NEUTRAL);
        subsystemManager.bottomClaw.rightWristUpPosition();
        subsystemManager.bottomClaw.closeClaw();
        subsystemManager.arm.toReceivingPos();
        subsystemManager.bottomClaw.neutralClawRotatorPosition();
        // subsystemManager.bottomClaw.rightWristInitPosition(); // move claw down so it's not in the way of bucket
        if (subsystemManager.slides.getSlidesState() == SlideState.BOTTOM
                && subsystemManager.slides.isIdle()
                && subsystemManager.horizontalSlides.getSlidesState() == HorizontalSlidesState.NEUTRAL
                && subsystemManager.horizontalSlides.isIdle()
                && subsystemManager.bottomClaw.inTransferPosition()
                && subsystemManager.bottomClaw.isClosed()
                && !subsystemManager.bottomClaw.inOrthoPos()
                && subsystemManager.arm.inReceivingPosition()) {
            return true;
        } else {
//            telemetry.addData("Slides at bottom:", subsystemManager.slides.getSlidesState());
//            telemetry.addData("Slides are idle:", subsystemManager.slides.isIdle());
//            telemetry.addData("Hor slides at neutral:", subsystemManager.horizontalSlides.getSlidesState());
//            telemetry.addData("Hor slides idle:", subsystemManager.horizontalSlides.isIdle());
//            telemetry.addData("Bottom claw in transfer pos:", subsystemManager.bottomClaw.inTransferPosition());
//            telemetry.addData("Bottom claw is closed:", subsystemManager.bottomClaw.isClosed());
            return false;
        }
    }
}
