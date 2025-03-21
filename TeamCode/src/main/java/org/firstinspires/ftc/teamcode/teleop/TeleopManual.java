package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickup;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickupAutomated;
import org.firstinspires.ftc.teamcode.teleop.state.Neutral;
import org.firstinspires.ftc.teamcode.teleop.state.SampleTransfer;

import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickupTwist90;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeManualSamplePickup;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSampleScore;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSpecimenScore;
import org.firstinspires.ftc.teamcode.teleop.state.SampleTransferAutomated;
//import org.firstinspires.ftc.teamcode.teleop.state.SpecimenScore;
import org.firstinspires.ftc.teamcode.teleop.state.Init;

import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

import java.util.Arrays;

@TeleOp(name = "Manual Driver Teleop", group = "User Control")
public class TeleopManual extends OpMode {
    public static TeleopState currentState;
    private final Gamepad prevGamepad1 = new Gamepad();
    private final Gamepad prevGamepad2 = new Gamepad();
    private SubsystemManager subsystemManager;
    // instance variables for all potential states
    private Neutral neutralState;
    private BeforeSamplePickupAutomated beforeSamplePickupAutomatedState;
    private BeforeSamplePickup beforeSamplePickupState;
    private BeforeManualSamplePickup beforeManualSamplePickupState;
    private BeforeSamplePickupTwist90 beforeSamplePickupTwist90State;
    private SampleTransferAutomated sampleTransferAutomatedState;
    private BeforeSampleScore beforeBucketScoreState;
    private BeforeSpecimenScore beforeSpecimenScoreState;
    private SampleTransfer sampleTransfer;
  
    private Init initState;
    private static boolean slowMode = false;
    public static final double SLOW_MODE_MULTIPLIER = 0.5;

    private boolean beforePickup = false;

    private boolean manualMode = true;

    enum ButtonPressState {
        PRESSED_GOOD, // the first time we see the button
        DEPRESSED, // you haven't let go
        UNPRESSED // it's not pressed
    }

    private ButtonPressState bottomClawButton = ButtonPressState.UNPRESSED;
    private ButtonPressState twistButton = ButtonPressState.UNPRESSED;
    private ButtonPressState topClawButton = ButtonPressState.UNPRESSED;
    private ButtonPressState rotatorButton = ButtonPressState.UNPRESSED;
    private ButtonPressState manualButton = ButtonPressState.UNPRESSED;
    enum ClawPosition {
        HORIZONTAL,
        ORTHOGONAL
    }
    private ClawPosition clawPosition = ClawPosition.HORIZONTAL;

    @Override
    public void init() {
        // instantiate all hardware util classes
        // register all teleop states
        subsystemManager = new SubsystemManager(hardwareMap, ConfidenceOrientationVectorPipeline.Color.RED, GeneralTeleop.Strategy.SAMPLE);
        neutralState = new Neutral(subsystemManager);
        beforeSamplePickupAutomatedState = new BeforeSamplePickupAutomated(subsystemManager, hardwareMap, telemetry);
        beforeSamplePickupState = new BeforeSamplePickup(subsystemManager);
        beforeManualSamplePickupState = new BeforeManualSamplePickup(subsystemManager);
        beforeSamplePickupTwist90State = new BeforeSamplePickupTwist90(subsystemManager);
        sampleTransferAutomatedState = new SampleTransferAutomated(subsystemManager);
        sampleTransfer = new SampleTransfer(subsystemManager);
        beforeBucketScoreState = new BeforeSampleScore(subsystemManager);
        beforeSpecimenScoreState = new BeforeSpecimenScore(subsystemManager);
        initState = new Init(subsystemManager);

        // set current state to be at init
        currentState = initState;
    }

    @Override
    public void loop() {
        // non-state based logic

        // claw logic

        // top claw
        if (gamepad1.left_bumper && topClawButton == ButtonPressState.UNPRESSED) {
            topClawButton = ButtonPressState.PRESSED_GOOD;
        } else if (gamepad1.left_bumper && topClawButton == ButtonPressState.PRESSED_GOOD) {
            topClawButton = ButtonPressState.DEPRESSED;
        } else if (!gamepad1.left_bumper) {
            topClawButton = ButtonPressState.UNPRESSED;
        }
        if (topClawButton==ButtonPressState.PRESSED_GOOD && !subsystemManager.topClaw.getOpenStatus()) {
            subsystemManager.topClaw.open();
        } else if (topClawButton==ButtonPressState.PRESSED_GOOD && subsystemManager.topClaw.getOpenStatus()) {
            subsystemManager.topClaw.close();
        }

        // claw rotator
        if (gamepad1.b && rotatorButton == ButtonPressState.UNPRESSED) {
            rotatorButton = ButtonPressState.PRESSED_GOOD;
        } else if (gamepad1.b && rotatorButton == ButtonPressState.PRESSED_GOOD) {
            rotatorButton = ButtonPressState.DEPRESSED;
        } else if (!gamepad1.b) {
            rotatorButton = ButtonPressState.UNPRESSED;
        }
        if (rotatorButton==ButtonPressState.PRESSED_GOOD && !subsystemManager.bottomClaw.inOrthoPos()) {
            subsystemManager.bottomClaw.orthogonalClawRotatorPosition();
        } else if (rotatorButton==ButtonPressState.PRESSED_GOOD && subsystemManager.bottomClaw.inOrthoPos()){
            subsystemManager.bottomClaw.neutralClawRotatorPosition();
        }

        // bottom claw
        if (gamepad1.right_bumper && bottomClawButton == ButtonPressState.UNPRESSED){
            bottomClawButton = ButtonPressState.PRESSED_GOOD;
        } else if (gamepad1.right_bumper && bottomClawButton == ButtonPressState.PRESSED_GOOD) {
            bottomClawButton = ButtonPressState.DEPRESSED;
        } else if (!gamepad1.right_bumper) {
            bottomClawButton = ButtonPressState.UNPRESSED;
        }
        if (bottomClawButton==ButtonPressState.PRESSED_GOOD && subsystemManager.bottomClaw.isClosed()) {
            subsystemManager.bottomClaw.openClaw();
        } else if (bottomClawButton==ButtonPressState.PRESSED_GOOD && !subsystemManager.bottomClaw.isClosed()) {
            subsystemManager.bottomClaw.closeClaw();
        }

        // manual mode
        if (gamepad2.y && manualButton == ButtonPressState.UNPRESSED) {
            manualButton = ButtonPressState.PRESSED_GOOD;
            manualMode = !manualMode;
        } else if (gamepad2.y && manualButton == ButtonPressState.PRESSED_GOOD) {
            manualButton = ButtonPressState.DEPRESSED;
        } else if (!gamepad2.y) {
            manualButton = ButtonPressState.UNPRESSED;
        }

        // drivetrain
        if (TeleopManual.slowMode) {
            subsystemManager.drive.move(gamepad2.left_stick_x * SLOW_MODE_MULTIPLIER, gamepad2.left_stick_y * SLOW_MODE_MULTIPLIER, gamepad2.right_stick_x * SLOW_MODE_MULTIPLIER);
        } else {
            subsystemManager.drive.move(gamepad2.left_stick_x, gamepad2.left_stick_y, gamepad2.right_stick_x);
        }

        // logic to run to states
        if (gamepad1.dpad_left) {
            switchToState(neutralState);
        } else if (gamepad1.dpad_right) {
            if (manualMode) {
                switchToState(beforeManualSamplePickupState);
            } else {
                switchToState(beforeSamplePickupAutomatedState);
            }
        } else if (gamepad1.x) {

        } else if (gamepad1.y) {
            switchToState(sampleTransfer);
        } else if (gamepad1.a) {
            switchToState(sampleTransferAutomatedState);
        } else if (gamepad1.left_trigger > 0.01f) {
            switchToState(beforeBucketScoreState);
        } else if (gamepad1.right_trigger > 0.01f) {
        } else if (gamepad1.dpad_up) {
            switchToState(beforeSpecimenScoreState);
        } else if (gamepad1.dpad_down) {

        } else if (gamepad1.start) {
            switchToState(initState);
        }

        currentState.runState(gamepad1, gamepad2);
        // save the state of the game controllers for the next loop
        // useful for debounce + rising/falling edge detection
        prevGamepad1.copy(gamepad1);
        prevGamepad2.copy(gamepad2);

        telemetry.addData("hori slides: ", subsystemManager.horizontalSlides.getEncoder());
        telemetry.addData("rotation pos: ", subsystemManager.bottomClaw.getClawRotatorPosition());
        telemetry.update();
    }

    public static void setSlowMode(boolean slowMode) {
        TeleopManual.slowMode = slowMode;
    }

    public void switchToState(TeleopState state) {
        // if the state we're trying to move to has potential dependencies and we are not in one of
        // them, don't move
        if (state.getDependencyStates().length == 0 || Arrays.asList(state.getDependencyStates()).contains(TeleopManual.currentState)) {
            TeleopManual.slowMode = false;
            currentState = state;
        }
    }
}
