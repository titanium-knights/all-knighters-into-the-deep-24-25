package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickup;
import org.firstinspires.ftc.teamcode.teleop.state.Neutral;
import org.firstinspires.ftc.teamcode.teleop.state.SamplePickup;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

import java.util.Arrays;

@TeleOp(name = "Driver Teleop", group = "User Control")
public class Teleop extends OpMode {
    public static TeleopState currentState;
    private final Gamepad prevGamepad1 = new Gamepad();
    private final Gamepad prevGamepad2 = new Gamepad();
    private SubsystemManager subsystemManager;
    // instance variables for all potential states
    private Neutral neutralState;
    private BeforeSamplePickup beforeSamplePickupState;
    private SamplePickup samplePickupState;

    @Override
    public void init() {
        // instantiate all hardware util classes
        subsystemManager = new SubsystemManager(hardwareMap);
        // register all teleop states
        neutralState = new Neutral(subsystemManager);
        beforeSamplePickupState = new BeforeSamplePickup(subsystemManager);
        samplePickupState =
                new SamplePickup(subsystemManager, new TeleopState[] {beforeSamplePickupState});
        // set current state to be at neutral
        currentState = neutralState;
    }

    @Override
    public void loop() {
        // non-state based logic
        subsystemManager.drive.move(
                gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // logic to run to states
        if (gamepad1.a) {
            switchToState(neutralState);
        } else if (gamepad1.b) {
            switchToState(beforeSamplePickupState);
        } else if (gamepad1.x) {
            switchToState(samplePickupState);
        }

        // run the current state
        currentState.runState(gamepad1, gamepad2);

        // save the state of the game controllers for the next loop
        // useful for debounce + rising/falling edge detection
        prevGamepad1.copy(gamepad1);
        prevGamepad2.copy(gamepad2);
    }

    public void switchToState(TeleopState state) {
        // if the state we're trying to move to has potential dependencies and we are not in one of
        // them, don't move
        if (state.getDependencyStates().length == 0
                || Arrays.asList(state.getDependencyStates()).contains(Teleop.currentState)) {
            currentState = state;
        }
    }
}
