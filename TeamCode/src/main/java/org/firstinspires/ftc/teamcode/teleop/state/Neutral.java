package org.firstinspires.ftc.teamcode.teleop.state;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class Neutral extends TeleopState {
    public Neutral(SubsystemManager subsystemManager) {
        super(subsystemManager);
    }

    @Override
    public void runToState(TeleopState state) {
        // empty because there are no states dependent on being in the neutral state
    }

    @Override
    public void runState() {
        Neutral.isActive = true;

        subsystemManager.topClaw.close();
        subsystemManager.bottomClaw.closeClaw();
        subsystemManager.scissors.moveToIdlePosition();
        subsystemManager.arm.toInitPos();
    }
}
