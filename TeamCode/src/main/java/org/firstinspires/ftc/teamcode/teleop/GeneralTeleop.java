package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

//import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickup;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickupAutomated;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickupAutomatedv2;
import org.firstinspires.ftc.teamcode.teleop.state.Neutral;
import org.firstinspires.ftc.teamcode.teleop.state.SampleTransfer;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickupTwist90;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSampleScore;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSpecimenScore;
import org.firstinspires.ftc.teamcode.teleop.state.SampleTransferAutomated;
import org.firstinspires.ftc.teamcode.teleop.state.Init;

import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

import java.util.Arrays;

public class GeneralTeleop {
    public static TeleopState currentState;
    private final Gamepad prevGamepad1 = new Gamepad();
    private final Gamepad prevGamepad2 = new Gamepad();
    private SubsystemManager subsystemManager;
    // instance variables for all potential states
    private Neutral neutralState;
    private BeforeSamplePickupAutomatedv2 beforeSamplePickupAutomatedStatev2;
    private BeforeSamplePickup beforeSamplePickupState;
    private BeforeSamplePickupTwist90 beforeSamplePickupTwist90State;
    private SampleTransferAutomated sampleTransferAutomatedState;
    private BeforeSampleScore beforeBucketScoreState;
    private BeforeSpecimenScore beforeSpecimenScoreState;
    private SampleTransfer sampleTransfer;
    private Init initState;
    private static boolean slowMode = false;
    public static final double SLOW_MODE_MULTIPLIER = 0.5;

    private boolean beforePickup = false;

    private boolean manualMode = false;

    public ConfidenceOrientationVectorPipeline.Color color = null;

    public enum Strategy {
        SAMPLE,
        SPECIMEN

    }
    private Strategy strategy = Strategy.SAMPLE;



    enum ButtonPressState {
        PRESSED_GOOD, // the first time we see the button
        DEPRESSED, // you haven't let go
        UNPRESSED // it's not pressed
    }

    private ButtonPressState bottomClawButton = ButtonPressState.UNPRESSED;
    private ButtonPressState topClawButton = ButtonPressState.UNPRESSED;
    private ButtonPressState rotatorButton = ButtonPressState.UNPRESSED;
    private ButtonPressState manualButton = ButtonPressState.UNPRESSED;

    private ButtonPressState left_dpad = ButtonPressState.UNPRESSED;

    private ButtonPressState strategyButton = ButtonPressState.UNPRESSED;
    enum ClawPosition {
        HORIZONTAL,
        ORTHOGONAL
    }
    private ClawPosition clawPosition = ClawPosition.HORIZONTAL;

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        // instantiate all hardware util classes
        subsystemManager = new SubsystemManager(hardwareMap, color);
        // register all teleop states
        neutralState = new Neutral(subsystemManager);
        beforeSamplePickupAutomatedStatev2 = new BeforeSamplePickupAutomatedv2(subsystemManager, hardwareMap, telemetry);
        beforeSamplePickupState = new BeforeSamplePickup(subsystemManager);
        beforeSamplePickupTwist90State = new BeforeSamplePickupTwist90(subsystemManager);
        sampleTransferAutomatedState = new SampleTransferAutomated(subsystemManager);
        sampleTransfer = new SampleTransfer(subsystemManager);
        beforeBucketScoreState = new BeforeSampleScore(subsystemManager);
        beforeSpecimenScoreState = new BeforeSpecimenScore(subsystemManager);
        initState = new Init(subsystemManager);

        // set current state to be at init
        currentState = initState;
    }

    public void loop(Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        //maintains positions of servos
        subsystemManager.swiper.up();
        subsystemManager.topClaw.maintainPosition();


        // non-state based logic

//
//        if (strategy == Strategy.SAMPLE) telemetry.addData("Strategy: ", "Sample");
//        if (strategy == Strategy.SPECIMEN) telemetry.addData("Strategy: ", "Specimen");

        // claw logic
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
        if (currentState == beforeSamplePickupAutomatedStatev2 && (abs(gamepad2.left_stick_x) > 0.1f || abs(gamepad2.left_stick_y) > 0.1f || abs(gamepad2.right_stick_x) > 0.1f)){
            switchToState(neutralState);
            telemetry.addData("switched to neutral", "a");
            subsystemManager.drive.move(gamepad2.left_stick_x, gamepad2.left_stick_y, gamepad2.right_stick_x);

        } else if (currentState != beforeSamplePickupAutomatedStatev2){
            telemetry.addLine("OMGGGGGGG");
            if (GeneralTeleop.slowMode) {
                subsystemManager.drive.move(gamepad2.left_stick_x * SLOW_MODE_MULTIPLIER, gamepad2.left_stick_y * SLOW_MODE_MULTIPLIER, gamepad2.right_stick_x * SLOW_MODE_MULTIPLIER);
            } else {
                if (gamepad2.left_stick_x > 0.3){
                    telemetry.addData("gamepad2: ", gamepad2.left_stick_x);
                }
                subsystemManager.drive.move(gamepad2.left_stick_x, gamepad2.left_stick_y, gamepad2.right_stick_x);
            }
        }


        // logic to run to states
        if (gamepad1.dpad_left) {
            switchToState(neutralState);
        } else if (gamepad1.dpad_right) {
            if (manualMode) {
                switchToState(beforeSamplePickupState);
            } else {
                switchToState(beforeSamplePickupAutomatedStatev2);
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

        // run the current state
        if (currentState == beforeSamplePickupAutomatedStatev2) {
            currentState.runState(gamepad1, gamepad2);
//            telemetry.addData("og angle: ", ((BeforeSamplePickupAutomatedv2)currentState).angleSeen);
//            telemetry.addData("angle: ", ((BeforeSamplePickupAutomatedv2)currentState).angle);
//            telemetry.addData("rotation angle: ", ((BeforeSamplePickupAutomatedv2)currentState).rotationAngle);
//            telemetry.addData("rotation theta: ", ((BeforeSamplePickupAutomatedv2)currentState).rotationTheta);
//            telemetry.addData("fps: ", subsystemManager.webcam.getFps());
//
//            telemetry.addData("wristRotated: ", ((BeforeSamplePickupAutomatedv2)currentState).wristRotated);
//            telemetry.addData("slidesExtending: ", ((BeforeSamplePickupAutomatedv2)currentState).slidesExtending);
//            telemetry.addData("objectDetected: ", ((BeforeSamplePickupAutomatedv2)currentState).objectDetected);
//            telemetry.addData("objectInFrame: ", ((BeforeSamplePickupAutomatedv2)currentState).objectInFrame);
//            telemetry.addData("slidesInPosition: ", ((BeforeSamplePickupAutomatedv2)currentState).slidesInPosition);
//            telemetry.addData("pictureTaken: ", ((BeforeSamplePickupAutomatedv2)currentState).pictureTaken);
//            telemetry.addData("readyForPickup: ", ((BeforeSamplePickupAutomatedv2)currentState).readyForPickup);
//            telemetry.addData("timeReset: ", ((BeforeSamplePickupAutomatedv2)currentState).timeReset);
//            telemetry.addData("finishedPickup: ", ((BeforeSamplePickupAutomatedv2)currentState).finishedPickup);
//            telemetry.addData("adjusting: ", ((BeforeSamplePickupAutomatedv2)currentState).adjusting);
//            telemetry.addData("pickupable: ", ((BeforeSamplePickupAutomatedv2)currentState).pickupable);
//            telemetry.addData("hori slides: ", subsystemManager.horizontalSlides.getEncoder());
//            telemetry.addData("rotation pos: ", subsystemManager.bottomClaw.getClawRotatorPosition());
//            //telemetry.update();


//            String pointString = Arrays.stream(((BeforeSamplePickupAutomated)currentState).points).map(p -> "(" + p.x + "," + p.y + ")").collect(Collectors.joining(","));
//            telemetry.addData("points: ", pointString);

            if (gamepad1.b && rotatorButton == ButtonPressState.UNPRESSED) {
                rotatorButton = ButtonPressState.PRESSED_GOOD;
                if (clawPosition == ClawPosition.HORIZONTAL) {
                    subsystemManager.bottomClaw.orthogonalClawRotatorPosition();
                    clawPosition = ClawPosition.ORTHOGONAL;
                } else {
                    subsystemManager.bottomClaw.neutralClawRotatorPosition();
                    clawPosition = ClawPosition.HORIZONTAL;
                }
            } else if (gamepad1.b && rotatorButton == ButtonPressState.PRESSED_GOOD) {
                rotatorButton = ButtonPressState.DEPRESSED;
            } else if (!gamepad1.b) {
                rotatorButton = ButtonPressState.UNPRESSED;
            }
        } else {
            beforePickup = false;
            currentState.runState(gamepad1, gamepad2);
        }


        // save the state of the game controllers for the next loop
        // useful for debounce + rising/falling edge detection
        prevGamepad1.copy(gamepad1);
        prevGamepad2.copy(gamepad2);

//        telemetry.addData("hori slides: ", subsystemManager.horizontalSlides.getEncoder());
//        telemetry.addData("rotation pos: ", subsystemManager.bottomClaw.getClawRotatorPosition());
//        //telemetry.update();
        telemetry.addData("what state are we in? ", currentState.getClass().getSimpleName());
        telemetry.addData("left stick x: ", gamepad2.left_stick_x);
        telemetry.addData("left stick y: ", gamepad2.left_stick_y);
        telemetry.addData("right stick x: ", gamepad2.right_stick_x);
    }



    public static void setSlowMode(boolean slowMode) {
        GeneralTeleop.slowMode = slowMode;
    }

    public void switchToState(TeleopState state) {
        // if the state we're trying to move to has potential dependencies and we are not in one of
        // them, don't move
        if (
                state.getDependencyStates().length == 0
                        || Arrays.asList(state.getDependencyStates()).contains(GeneralTeleop.currentState)
        ) {
            GeneralTeleop.slowMode = false;
            currentState.reset();
            currentState = state;
        }
    }
}