package org.firstinspires.ftc.teamcode.pedroAuton.config;


import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public abstract class AutonState {
    protected final SubsystemManager subsystemManager;

    public AutonState(SubsystemManager subsystemManager) {
        this.subsystemManager = subsystemManager;
    }

    public abstract boolean update(); // boolean indicates whether the state is done


}
