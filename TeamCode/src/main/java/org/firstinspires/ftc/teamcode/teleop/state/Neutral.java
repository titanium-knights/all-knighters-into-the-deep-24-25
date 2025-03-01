package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class Neutral extends TeleopState {
    public Neutral(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) { // everything in neutral position
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);

        // if horizontal slides are further out than 150 encoder ticks:
        // 1) let arm get out of way 2) let bottom claw slightly open to let block slip down
        if (subsystemManager.horizontalSlides.getEncoder() < -150) {
            subsystemManager.arm.toGetOutOfWay();
            subsystemManager.bottomClaw.openClawHalf();
        } else {
            subsystemManager.arm.toReceivingPos();
        }
        subsystemManager.horizontalSlides.slideToPosition(HorizontalSlidesState.IN);
        subsystemManager.bottomClaw.neutralClawRotatorPosition();
        subsystemManager.bottomClaw.rightWristUpPosition();
    }
}
