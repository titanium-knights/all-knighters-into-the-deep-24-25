package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public abstract class TeleopState {
    protected final SubsystemManager subsystemManager;
    protected final TeleopState[] dependencyStates;

    public TeleopState(SubsystemManager subsystemManager) {
        this(subsystemManager, new TeleopState[0]);
    }

    public TeleopState(SubsystemManager subsystemManager, TeleopState[] dependencies) {
        this.subsystemManager = subsystemManager;
        this.dependencyStates = dependencies;
    }

    /**
     * @return whether the state is currently active or not
     */
    public boolean isActive() {
        return Teleop.currentState != null && Teleop.currentState.getClass().equals(this.getClass());
    }

    /**
     * Function to move subsystems to the state and run any code needed to maintain the state.
     * This is where the main logic of the state should be placed.
     */
    public abstract void runState(Gamepad gamepad1, Gamepad gamepad2);

    public TeleopState[] getDependencyStates() {
        return dependencyStates;
    }

    public void reset() {

    }
}