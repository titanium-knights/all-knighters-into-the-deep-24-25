package org.firstinspires.ftc.teamcode.teleop;

import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public abstract class TeleopState {
    protected final SubsystemManager subsystemManager;
    protected static boolean isActive = false;
    protected final TeleopState[] dependentStates;

    public TeleopState(SubsystemManager subsystemManager) {
        this(subsystemManager, new TeleopState[0]);
    }

    public TeleopState(SubsystemManager subsystemManager, TeleopState[] dependentStates) {
        this.subsystemManager = subsystemManager;
        this.dependentStates = dependentStates;
    }

    /**
     * Function to move from one state to another. Can be used to
     * link states that should not be globally reachable. For example,
     * if state B can only be reached from state A, then state A should
     * have a runToState function that can move to state B, while the main
     * loop would not have direct access to state B.
     * @param state - the state that the code should move to. The overridden
     *              function should check if the state is legal to transition to
     *              from the current state.
     */
    public abstract void runToState(TeleopState state);

    /**
     * Function to move subsystems to the state and run any code needed to maintain the state.
     * This is where the main logic of the state should be placed.
     * This function should mark the current state as active.
     * TODO: not sure if theres a way to enforce this on all child classes
     */
    public abstract void runState();

    /**
     * @return whether the state is currently active or not
     */
    public static boolean isActive() {
        return isActive;
    }
}
