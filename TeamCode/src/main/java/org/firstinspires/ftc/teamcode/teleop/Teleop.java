package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Arm;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.topClaw;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.PullUp;

@Config
@TeleOp(name="Driver Teleop", group="default")
public class Teleop extends OpMode {

    enum ButtonPressState {
       PRESSED_GOOD,
       DEPRESSED,
       UNPRESSED,
    }

    private ButtonPressState slowModeButtonState = ButtonPressState.UNPRESSED; // Slow mode button state
    private ButtonPressState sampleSpecimenButtonState = ButtonPressState.UNPRESSED; // Sample/Specimen button state
    private TeleopState teleopState = TeleopState.INIT;

    private SimpleMecanumDrive drive;
    private topClaw claw;
    private Slides slides;
    private Arm arm;
    private PullUp pullUp;


    final float STICK_MARGIN = 0.5f;
    final double normalPower = .85;
    final double slowPower = 0.30;

    final int tickMax = 20;

    final int slidesEncoderSlowModeBreakpoint = -800;

    boolean slowMode = false;
    boolean specimenMode = false;
    int ticks = 0;

    @Override
    public void init() {
        // initialize util classes for hardware
        drive = new SimpleMecanumDrive(hardwareMap);
        claw = new topClaw(hardwareMap);
        slides = new Slides(hardwareMap);
        arm = new Arm(hardwareMap);
        pullUp = new PullUp(hardwareMap);
    }

    @Override
    public void loop() {
        TeleopState prevState = teleopState;
        move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // Manual Claw controls (RB & LB)
        if (gamepad1.left_bumper) {
            claw.open();
        } else if (gamepad1.right_bumper) {
            claw.close();
        }

        // Init Position (Start)
        if (gamepad1.start && teleopState != TeleopState.INIT) {
            teleopState = TeleopState.INIT;
        }

        // Before Picking Up Position (B)
        if (gamepad1.b && teleopState != TeleopState.BEFORE_PICKUP) {
            teleopState = TeleopState.BEFORE_PICKUP;
        }

        // Specimen Pick Up Position (A)
        if (gamepad1.a && teleopState != TeleopState.SPECIMEN_PICKUP && specimenMode) {
            teleopState = TeleopState.SPECIMEN_PICKUP;
        }

        // Picking Up Position (A)
        if (gamepad1.a && teleopState != TeleopState.PICKING_UP && !specimenMode) {
            teleopState = TeleopState.PICKING_UP;
        }

        // Slide move down (RT)
        if (gamepad1.right_trigger > 0.1 && teleopState != TeleopState.MANUAL_SLIDE_DOWN) {
            teleopState = TeleopState.MANUAL_SLIDE_DOWN;
        }

        // Reset slide encoder (LT)
        if (gamepad1.left_trigger > 0.1 && teleopState != TeleopState.MANUAL_SLIDE_DOWN) {
            slides.resetSlideEncoder();
        }

        // Top Basket Position (X)
        if (gamepad1.x && teleopState != TeleopState.DROP && !specimenMode) {
            teleopState = TeleopState.DROP;
        }

        // Low Basket Position (Y)
        if (gamepad1.y && teleopState != TeleopState.DROP && !specimenMode) {
            teleopState = TeleopState.DROPLOW;
        }

        // Specimen Position (X)
        if (gamepad1.x && teleopState != TeleopState.SPECIMEN && specimenMode) {
            teleopState = TeleopState.SPECIMEN;
        }

        // Slides move up slightly (Y)
        if (gamepad1.y && teleopState != TeleopState.SPECIMENSCORE && specimenMode) {
            teleopState = TeleopState.SPECIMENSCORE;
        }

        // Slow Mode Toggle (DUp)
        if (gamepad1.dpad_up) {
            if (slowModeButtonState == ButtonPressState.UNPRESSED) {
                slowModeButtonState = ButtonPressState.PRESSED_GOOD;
                slowMode = !slowMode;
            } else if (slowModeButtonState == ButtonPressState.PRESSED_GOOD) {
                slowModeButtonState = ButtonPressState.DEPRESSED;
            }
        } else {
            slowModeButtonState = ButtonPressState.UNPRESSED;
        }

        // Sample/Specimen Mode Toggle (DLeft)
        if (gamepad1.dpad_left) {
            if (sampleSpecimenButtonState == ButtonPressState.UNPRESSED) {
                sampleSpecimenButtonState = ButtonPressState.PRESSED_GOOD;
                specimenMode = !specimenMode;
            } else if (sampleSpecimenButtonState == ButtonPressState.PRESSED_GOOD) {
                sampleSpecimenButtonState = ButtonPressState.DEPRESSED;
            }
        } else {
            sampleSpecimenButtonState = ButtonPressState.UNPRESSED;
        }

        goToPosition(teleopState);

        telemetry.addData("arm pos", arm.getPosition());
        telemetry.addData("slides pos", slides.getEncoder());
        telemetry.addData("claw pos", claw.getPosition());
        //telemetry.addData("forearm pos", claw.getForearmPosition());
        telemetry.addData("slow mode", slowMode);

        telemetry.update();
    }

    private void goToPosition(TeleopState state) {
        if (state == TeleopState.INIT) {
            slides.slideToPosition(SlideState.BOTTOM);
            arm.toInitPos();
        } else if (state == TeleopState.BEFORE_PICKUP) {
            slides.slideToPosition(SlideState.BOTTOM);
            arm.beforePickUp();
        } else if (state == TeleopState.PICKING_UP){
            slides.slideToPosition(SlideState.BOTTOM);
            arm.pickingUp();
        } else if (state == TeleopState.DROP) {
            slides.slideToPosition(SlideState.TOP);
            arm.toScoreBucketPos();
        } else if (state == TeleopState.SPECIMEN) {
            slides.slideToPosition(SlideState.MEDIUM);
            arm.toScoreSpecimenPos();
        } else if (state == TeleopState.SPECIMENSCORE) {
            slides.slideToPosition(SlideState.MEDIUMSCORE);
        } else if (state == TeleopState.MANUAL_SLIDE_UP) {
            slides.slideToPosition(SlideState.MANUALUP);
        } else if (state == TeleopState.MANUAL_SLIDE_DOWN) {
            slides.slideToPosition(SlideState.MANUALDOWN);
        } else if (state == TeleopState.SPECIMEN_PICKUP) {
            slides.slideToPosition(SlideState.BOTTOM);
            arm.pickingUpSpecimen();
        }
    }

    public void move(float x, float y, float turn) {
        // If the stick movement is negligible, ignore it
        if (Math.abs(x) <= STICK_MARGIN) x = .0f;
        if (Math.abs(y) <= STICK_MARGIN) y = .0f;
        if (Math.abs(turn) <= STICK_MARGIN) turn = .0f;

        if (x != .0f || y != .0f || turn != .0f) {
            ticks = Math.max(ticks + 1, tickMax);
        } else {
            ticks = 0;
        }

        double tickMultiplier = (ticks * ticks * 1.0) / tickMax / tickMax;

        double multiplier;
        if (slides.getEncoder() < slidesEncoderSlowModeBreakpoint || slowMode) {
            multiplier = slowPower;
        } else {
            multiplier = normalPower;
        }

        drive.move(x * multiplier, y * multiplier, turn * multiplier);
    }
}