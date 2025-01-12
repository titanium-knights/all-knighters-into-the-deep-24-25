package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickup;
import org.firstinspires.ftc.teamcode.teleop.state.Neutral;
import org.firstinspires.ftc.teamcode.teleop.state.SampleTransfer;
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
    private SampleTransfer sampleTransferState;
    private BeforeBucketScore beforeBucketScoreState;
    private BucketScore bucketScoreState;
    private BeforeSpecimenScore beforeSpecimenScoreState;
    private SpecimenScore specimenScoreState;
    private Init initState;
    private static boolean slowMode = false;
    private static final double SLOW_MODE_MULTIPLIER = 0.3;

    @Override
    public void init() {
        // instantiate all hardware util classes
        subsystemManager = new SubsystemManager(hardwareMap);
        // register all teleop states
        neutralState = new Neutral(subsystemManager);
        beforeSamplePickupState = new BeforeSamplePickup(subsystemManager);
        beforeSamplePickupTwist90State = new BeforeSamplePickupTwist90(subsystemManager);
        samplePickupState = new SamplePickup(subsystemManager, new TeleopState[] {beforeSamplePickupState, beforeSamplePickupTwist90State});
        sampleTransferState = new SampleTransfer(subsystemManager);
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
        if (Teleop.slowMode) {
            subsystemManager.drive.move(gamepad2.left_stick_x * SLOW_MODE_MULTIPLIER, gamepad2.left_stick_y * SLOW_MODE_MULTIPLIER, gamepad2.right_stick_x * SLOW_MODE_MULTIPLIER);
        } else {
            subsystemManager.drive.move(gamepad2.left_stick_x, gamepad2.left_stick_y, gamepad2.right_stick_x);
        }


        // claw
        if (gamepad1.left_bumper) {
            subsystemManager.bottomClaw.openClaw();
            subsystemManager.topClaw.open();
        } else if (gamepad1.right_bumper) {
            subsystemManager.bottomClaw.closeClaw();
            subsystemManager.topClaw.close();
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
        } else if (gamepad1.b) {
            switchToState(sampleTransferState);
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

        telemetry.addData("scissor encoder pos:", subsystemManager.scissors.getEncoder());
        telemetry.addData("scissors power: ", subsystemManager.scissors.getScissorsPower());
    }

    public static void setSlowMode(boolean slowMode) {
        Teleop.slowMode = slowMode;
    }

    public void switchToState(TeleopState state) {
        // if the state we're trying to move to has potential dependencies and we are not in one of
        // them, don't move
        if (
                state.getDependencyStates().length == 0
                || Arrays.asList(state.getDependencyStates()).contains(Teleop.currentState)
        ) {
            Teleop.slowMode = false;
            currentState = state;
        }
    }
}
