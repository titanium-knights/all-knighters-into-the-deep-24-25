package org.firstinspires.ftc.teamcode.teleop.state;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.Teleop;
import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class BeforeSamplePickup extends TeleopState {
    public BeforeSamplePickup(SubsystemManager subsystemManager, TeleopState[] dependentStates) {
        super(subsystemManager, dependentStates);
    }

    @Override
    public void runToState(TeleopState state) {
        if (state instanceof SamplePickup) {
            Teleop.currentState = state;
        }
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        subsystemManager.scissors.moveToFullyExtended();

        if (gamepad1.dpad_up) {
            runToState(dependentStates[0]);
        }
    }
}
