package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Arm;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.topClaw;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.SlideState;

import java.util.LinkedList;
import java.util.Queue;

@Config
@TeleOp(name="Driver Teleop", group="default")
public class Teleop extends OpMode {

    enum ButtonPressState {
        PRESSED_GOOD,
        UNPRESSED,
    }

    private ButtonPressState yButtonState = ButtonPressState.UNPRESSED;
    private ButtonPressState aButtonState = ButtonPressState.UNPRESSED;

    private TeleopState teleopState = TeleopState.INIT;
    private TeleopState previousTeleopState = TeleopState.INIT;

    private SimpleMecanumDrive drive;
    private topClaw claw;
    private Slides slides;
    private Arm arm;

    final float STICK_MARGIN = 0.05f;  // Adjusted deadzone for finer control
    final double normalPower = .85;
    final double slowPower = 0.30;

    final int slidesEncoderSlowModeBreakpoint = -800;

    boolean slowMode = false;

    // Button press history
    private static final int BUTTON_HISTORY_SIZE = 10;
    private final Queue<ButtonEvent> buttonPressHistory = new LinkedList<>();

    // ButtonEvent class to store button events and their timestamps
    private static class ButtonEvent {
        String buttonName;
        long timestamp;

        ButtonEvent(String buttonName, long timestamp) {
            this.buttonName = buttonName;
            this.timestamp = timestamp;
        }
    }

    // Previous states for other buttons
    private boolean prevBButtonState = false;
    private boolean prevXButtonState = false;
    private boolean prevYButtonState = false;
    private boolean prevDpadUpState = false;
    private boolean prevDpadDownState = false;
    private boolean prevDpadLeftState = false;
    private boolean prevDpadRightState = false;
    private boolean prevLeftBumperState = false;
    private boolean prevRightBumperState = false;
    private boolean prevLeftStickButtonState = false;
    private boolean prevRightStickButtonState = false;
    private boolean prevStartButtonState = false;
    private boolean prevBackButtonState = false;
    private boolean prevLeftTriggerPressed = false;
    private boolean prevRightTriggerPressed = false;

    @Override
    public void init() {
        // Initialize hardware
        drive = new SimpleMecanumDrive(hardwareMap);
        claw = new topClaw(hardwareMap);
        slides = new Slides(hardwareMap);
        arm = new Arm(hardwareMap);
    }

    @Override
    public void loop() {
        long currentTime = System.currentTimeMillis();

        // 'A' Button state management using ButtonPressState
        if (gamepad1.a) {
            if (aButtonState == ButtonPressState.UNPRESSED) {
                aButtonState = ButtonPressState.PRESSED_GOOD;
                // Button just pressed
                addButtonEvent("A_PRESSED", currentTime);
            }
            // else, button is still pressed, do nothing
        } else {
            if (aButtonState == ButtonPressState.PRESSED_GOOD) {
                aButtonState = ButtonPressState.UNPRESSED;
                // Button just released
                addButtonEvent("A_RELEASED", currentTime);
            }
            // else, button is still unpressed, do nothing
        }

        // Check for other buttons pressed during 'A' counting
        if (isCountingAPresses() && isAnyOtherButtonJustPressed()) {
            // Reset the counting
            buttonPressHistory.clear();
        }

        // Check for five consecutive 'A' button presses and releases
        if (checkForFiveConsecutiveAPresses()) {
            if (teleopState != TeleopState.MANUAL_CONTROL) {
                previousTeleopState = teleopState;
                teleopState = TeleopState.MANUAL_CONTROL;
            } else {
                teleopState = previousTeleopState;
            }
            // Clear the button press history after toggling
            buttonPressHistory.clear();
        }

        // 'Y' Button state management using ButtonPressState
        if (gamepad1.y) {
            if (yButtonState == ButtonPressState.UNPRESSED) {
                yButtonState = ButtonPressState.PRESSED_GOOD;
                // Button just pressed, toggle slow mode
                slowMode = !slowMode;
                // Reset 'A' counting if in progress
                if (isCountingAPresses()) {
                    buttonPressHistory.clear();
                }
            }
            // else, button is still pressed, do nothing
        } else {
            if (yButtonState == ButtonPressState.PRESSED_GOOD) {
                yButtonState = ButtonPressState.UNPRESSED;
                // Button just released
                // No action needed
            }
            // else, button is still unpressed, do nothing
        }

        // Robot movement control is always active
        move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        if (teleopState == TeleopState.MANUAL_CONTROL) {
            manualControl();
        } else {
            // State transitions based on gamepad inputs

            // Init Position (Start)
            if (gamepad1.start && teleopState != TeleopState.INIT) {
                teleopState = TeleopState.INIT;
            }

            // Before Picking Up Position (A)
            if (gamepad1.a && teleopState != TeleopState.BEFORE_PICKUP && !isCountingAPresses()) {
                teleopState = TeleopState.BEFORE_PICKUP;
            }

            // Pick Up Specimen Position (Dpad Right)
            if (gamepad1.dpad_right && teleopState != TeleopState.SPECIMEN_PICKUP) {
                teleopState = TeleopState.SPECIMEN_PICKUP;
            }

            // Picking Up Position (Dpad Down)
            if (gamepad1.dpad_down && teleopState != TeleopState.PICKING_UP) {
                teleopState = TeleopState.PICKING_UP;
            }

            // Drop Position (B)
            if (gamepad1.b && teleopState != TeleopState.DROP) {
                teleopState = TeleopState.DROP;
            }

            // Drop Low Position (Trigger Right)
            if (gamepad1.b && teleopState != TeleopState.DROP_LOW) {
                teleopState = TeleopState.DROP_LOW;
            }

            // Specimen Position (X)
            if (gamepad1.x && teleopState != TeleopState.SPECIMEN) {
                teleopState = TeleopState.SPECIMEN;
            }

            // Specimen Lower Arm (Dpad Up)
            if (gamepad1.dpad_up && teleopState != TeleopState.SPECIMEN_SCORE) {
                teleopState = TeleopState.SPECIMEN_SCORE;
            }

            // Apply the positions based on the state
            goToPosition(teleopState);
        }

        // Update previous states for other buttons
        updatePreviousButtonStates();

        // Telemetry
        telemetry.addData("Arm Position", arm.getPosition());
        telemetry.addData("Slides Position", slides.getEncoder());
        telemetry.addData("Claw Position", claw.getPosition());
        telemetry.addData("Slow Mode", slowMode);
        telemetry.addData("Teleop State", teleopState);
        telemetry.addData("Button Press History Size", buttonPressHistory.size());
        telemetry.update();
    }

    private void goToPosition(TeleopState state) {
        switch (state) {
            case INIT:
                slides.slideToPosition(SlideState.BOTTOM);
                arm.toInitPos();
                break;
            case BEFORE_PICKUP:
                slides.slideToPosition(SlideState.BOTTOM);
                arm.beforePickUp();
                break;
            case PICKING_UP:
                slides.slideToPosition(SlideState.BOTTOM);
                arm.pickingUp();
                break;
            case DROP:
                slides.slideToPosition(SlideState.TOP);
                arm.toScoreBucketPos();
                break;
            case DROP_LOW:
                slides.slideToPosition(SlideState.TOP);
                arm.toLowScoreBucketPos();
                break;
            case SPECIMEN:
                slides.slideToPosition(SlideState.MEDIUM);
                arm.toScoreSpecimenPos();
                break;
            case SPECIMEN_SCORE:
                slides.slideToPosition(SlideState.MEDIUMSCORE);
                break;
            case SPECIMEN_PICKUP:
                slides.slideToPosition(SlideState.BOTTOM);
                arm.pickingUpSpecimen();
                break;
            default:
                break;
        }
    }

    private void manualControl() {
        // Claw control
        if (gamepad1.left_bumper) {
            claw.open();
        } else if (gamepad1.right_bumper) {
            claw.close();
        }

        // Slides control
        if (gamepad1.dpad_up) {
            slides.manualUp(gamepad1.right_trigger);
        } else if (gamepad1.dpad_down) {
            slides.manualDown(gamepad1.left_trigger);
        } else {
            slides.stop();
        }

        // Arm control using D-pad or right stick Y-axis
        if (gamepad1.right_trigger > 0.1) {
            arm.manualUp(1.0);
        } else if (gamepad1.left_trigger > 0.1) {
            arm.manualDown(1.0);
        } else if (gamepad1.right_stick_y < -0.1) { // Stick pushed up
            arm.manualUp(-gamepad1.right_stick_y); // Convert to positive value
        } else if (gamepad1.right_stick_y > 0.1) { // Stick pushed down
            arm.manualDown(gamepad1.right_stick_y);
        } else {
            arm.stop();
        }

        // Additional manual controls can be added here
    }

    private void move(float x, float y, float turn) {
        // Deadzone adjustment
        if (Math.abs(x) <= STICK_MARGIN) x = 0.0f;
        if (Math.abs(y) <= STICK_MARGIN) y = 0.0f;
        if (Math.abs(turn) <= STICK_MARGIN) turn = 0.0f;

        // Movement multiplier
        double multiplier = (slides.getEncoder() < slidesEncoderSlowModeBreakpoint || slowMode) ? slowPower : normalPower;
        drive.move(x * multiplier, y * multiplier, turn * multiplier);
    }

    private void addButtonEvent(String buttonName, long timestamp) {
        if (buttonPressHistory.size() >= BUTTON_HISTORY_SIZE) {
            buttonPressHistory.poll(); // Remove oldest event
        }
        buttonPressHistory.add(new ButtonEvent(buttonName, timestamp));
    }

    private boolean checkForFiveConsecutiveAPresses() {
        if (buttonPressHistory.size() < 10) {
            return false;
        }

        ButtonEvent[] events = buttonPressHistory.toArray(new ButtonEvent[0]);

        // Check for pattern: A_PRESSED, A_RELEASED, repeated 5 times
        for (int i = events.length - 10; i <= events.length - 2; i += 2) {
            if (!events[i].buttonName.equals("A_PRESSED") || !events[i + 1].buttonName.equals("A_RELEASED")) {
                return false;
            }
        }
        return true;
    }

    private boolean isCountingAPresses() {
        // Check if the last events are A_PRESSED or A_RELEASED
        if (buttonPressHistory.isEmpty()) {
            return false;
        }
        for (ButtonEvent event : buttonPressHistory) {
            if (!event.buttonName.startsWith("A_")) {
                return false;
            }
        }
        return true;
    }

    private boolean isAnyOtherButtonJustPressed() {
        return (gamepad1.b && !prevBButtonState) ||
                (gamepad1.x && !prevXButtonState) ||
                (gamepad1.y && !prevYButtonState) ||
                (gamepad1.dpad_up && !prevDpadUpState) ||
                (gamepad1.dpad_down && !prevDpadDownState) ||
                (gamepad1.dpad_left && !prevDpadLeftState) ||
                (gamepad1.dpad_right && !prevDpadRightState) ||
                (gamepad1.left_bumper && !prevLeftBumperState) ||
                (gamepad1.right_bumper && !prevRightBumperState) ||
                (gamepad1.left_stick_button && !prevLeftStickButtonState) ||
                (gamepad1.right_stick_button && !prevRightStickButtonState) ||
                (gamepad1.start && !prevStartButtonState) ||
                (gamepad1.back && !prevBackButtonState) ||
                (gamepad1.left_trigger > 0.1 && !prevLeftTriggerPressed) ||
                (gamepad1.right_trigger > 0.1 && !prevRightTriggerPressed);
    }

    private void updatePreviousButtonStates() {
        prevBButtonState = gamepad1.b;
        prevXButtonState = gamepad1.x;
        prevYButtonState = gamepad1.y;
        prevDpadUpState = gamepad1.dpad_up;
        prevDpadDownState = gamepad1.dpad_down;
        prevDpadLeftState = gamepad1.dpad_left;
        prevDpadRightState = gamepad1.dpad_right;
        prevLeftBumperState = gamepad1.left_bumper;
        prevRightBumperState = gamepad1.right_bumper;
        prevLeftStickButtonState = gamepad1.left_stick_button;
        prevRightStickButtonState = gamepad1.right_stick_button;
        prevStartButtonState = gamepad1.start;
        prevBackButtonState = gamepad1.back;
        prevLeftTriggerPressed = gamepad1.left_trigger > 0.1;
        prevRightTriggerPressed = gamepad1.right_trigger > 0.1;
    }
}
