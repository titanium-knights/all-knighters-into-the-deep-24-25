package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickup;
import org.firstinspires.ftc.teamcode.teleop.state.Neutral;
import org.firstinspires.ftc.teamcode.teleop.state.SamplePickup;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickupTwist90;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeBucketScore;
import org.firstinspires.ftc.teamcode.teleop.state.BucketScore;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSpecimenScore;
import org.firstinspires.ftc.teamcode.teleop.state.SpecimenScore;
import org.firstinspires.ftc.teamcode.teleop.state.Init;

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
    private BeforeSamplePickupTwist90 beforeSamplePickupTwist90State;
    private BeforeBucketScore beforeBucketScoreState;
    private BucketScore bucketScoreState;
    private BeforeSpecimenScore beforeSpecimenScoreState;
    private SpecimenScore specimenScoreState;
    private Init initState;

    @Override
    public void init() {
        // instantiate all hardware util classes
        subsystemManager = new SubsystemManager(hardwareMap);
        // register all teleop states
        neutralState = new Neutral(subsystemManager);
        beforeSamplePickupState = new BeforeSamplePickup(subsystemManager);
        beforeSamplePickupTwist90State = new BeforeSamplePickupTwist90(subsystemManager);
        samplePickupState = new SamplePickup(subsystemManager, new TeleopState[] {beforeSamplePickupState, beforeSamplePickupTwist90State});
        beforeBucketScoreState = new BeforeBucketScore(subsystemManager);
        bucketScoreState = new BucketScore(subsystemManager, new TeleopState[] {beforeBucketScoreState});
        beforeSpecimenScoreState = new BeforeSpecimenScore(subsystemManager);
        specimenScoreState = new SpecimenScore(subsystemManager, new TeleopState[] {beforeSpecimenScoreState});
        initState = new Init(subsystemManager);

        // set current state to be at init
        currentState = initState;
    }

    @Override
    public void loop() {
        // non-state based logic

        // drivetrain
        if (beforeSamplePickupState.isActive() || samplePickupState.isActive() || beforeSamplePickupTwist90State.isActive() || beforeBucketScoreState.isActive()) {
            subsystemManager.drive.move(gamepad1.left_stick_x * 0.3, gamepad1.left_stick_y * 0.3, gamepad1.right_stick_x * 0.3);
        } else {
            subsystemManager.drive.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        }


        // claw
        if (gamepad1.left_bumper) {
            subsystemManager.bottomClaw.openClaw();
            subsystemManager.topClaw.open();
        } else if (gamepad1.right_bumper) {
            subsystemManager.bottomClaw.closeClaw();
            subsystemManager.topClaw.close();
        }


        // TODO: figure out how to implement without getting in the way of the states
        // resetting slide encoders in the case something goes wrong (gamepad2 only)
        if (gamepad2.right_stick_y > 0.1) { // Stick pushed down
            subsystemManager.slides.manualDown(gamepad2.right_stick_y);
        }
        if (gamepad2.start) {
            subsystemManager.slides.resetSlideEncoder();
        }

        // logic to run to states
        if (gamepad1.dpad_left) {
            switchToState(neutralState);
        } else if (gamepad1.dpad_right) {
            switchToState(beforeSamplePickupState);
        } else if (gamepad1.x) {
            switchToState(samplePickupState);
        } else if (gamepad1.y) {
            switchToState(beforeSamplePickupTwist90State);
        } else if (gamepad1.left_trigger > 0.01f) {
            switchToState(beforeBucketScoreState);
        } else if (gamepad1.right_trigger > 0.01f) {
            switchToState(bucketScoreState);
        } else if (gamepad1.dpad_up) {
            switchToState(beforeSpecimenScoreState);
        } else if (gamepad1.dpad_down) {
            switchToState(specimenScoreState);
        } else if (gamepad1.start) {
            switchToState(initState);
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
