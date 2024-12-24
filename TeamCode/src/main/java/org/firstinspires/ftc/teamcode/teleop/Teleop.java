package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.state.Neutral;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

public class Teleop extends OpMode {
    private TeleopState currentState;
    private SubsystemManager subsystemManager;
    private final Gamepad prevGamepad1 = new Gamepad();
    private final Gamepad prevGamepad2 = new Gamepad();

    // instance variables for all potential states
    private Neutral neutralState;

    @Override
    public void init() {
        // instantiate all hardware util classes
        subsystemManager = new SubsystemManager(hardwareMap);
        // register all teleop states
        neutralState = new Neutral(subsystemManager);
    }

    @Override
    public void loop() {
        // non-state based logic
        subsystemManager.drive.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // logic to run to states
        if (gamepad1.a) {
            currentState = neutralState;
        } else if (gamepad1.b) {
            // currentState = someOtherState;
        }

        // run the current state
        currentState.runState();

        // save the state of the gamepads for the next loop
        prevGamepad1.copy(gamepad1);
        prevGamepad2.copy(gamepad2);
    }
}
