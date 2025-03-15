package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Slides {

    // Preset power values for both directions (based on weight)
    // Negative is up, positive is down
    private static final double slideUpPower = -1.0;
    private static final double slideDownPower = 1.0;
    private static final double idlePower = -0.2;

    // Preset encoder values
    public final int maxHeight = -2200;
    public final int minHeight = 0;
    public static int BUFFER = 50;
    private final DcMotor slidesMotor;
    private SlideState currentState;

    // On the physical robot, the more negative the value, the higher it is
    // store the current position of the slides
    private int pos;

    public Slides(HardwareMap hmap) {
        this.slidesMotor = hmap.dcMotor.get(CONFIG.slidesMotor);

        // Calibrate the encoder to initial position
        slidesMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slidesMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.pos = 0;

        // Ensure the motor brakes when power is zero
        slidesMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public int getEncoder() {
        return slidesMotor.getCurrentPosition();
    }

    public void stop() {
        // The power required to be stationary
        slidesMotor.setPower(idlePower);
    }

    private boolean encoderValueWithinBufferOfTarget(int targetEncoderValue) {
        return Math.abs(slidesMotor.getCurrentPosition() - targetEncoderValue) <= BUFFER;
    }

    public void resetSlideEncoder() {
        slidesMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slidesMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.pos = 0;
    }

    // Exists to switch between target encoder values
    public boolean slideToPosition(SlideState state) {
        currentState = state;
        if (encoderValueWithinBufferOfTarget(state.getEncoderValue())) {
            stop();
            return true;
        } else {
            updateSlidesPowerBasic(state.getEncoderValue());
            return false;
        }
    }

    public SlideState getSlidesState() {
        return currentState;
    }

    public boolean isIdle() {
        return slidesMotor.getPower() == idlePower;
    }

    // Manual control methods accepting controller values
    public void manualUp(double power) {
        // Assume power is from 0.0 to 1.0
        double adjustedPower = -Math.abs(power); // Negative for upward movement
        slidesMotor.setPower(adjustedPower);
    }

    public void manualDown(double power) {
        // Assume power is from 0.0 to 1.0
        double adjustedPower = Math.abs(power); // Positive for downward movement
        slidesMotor.setPower(adjustedPower);
    }

    private void updateSlidesPowerBasic(int targetEncoderValue) {
        int pos = slidesMotor.getCurrentPosition();
        double distanceAway = targetEncoderValue - pos;
        if (distanceAway > 0) { // moving down
            slidesMotor.setPower(slideDownPower);
        } else if (distanceAway < 0) { // moving up
            slidesMotor.setPower(slideUpPower);
        } else {
            slidesMotor.setPower(idlePower);
        }
    }

    // basic proportional control (potential future use)
    private void updateSlidesPower(int targetEncoderValue) {
        int currentPos = slidesMotor.getCurrentPosition();
        double distanceAway = targetEncoderValue - currentPos;

        // Simple proportional control
        double kP = 0.001; // Proportional gain (adjust as needed)
        double power = kP * distanceAway;

        // Limit power to max values
        power = Math.max(-1.0, Math.min(1.0, power));

        // Round up minute values to prevent stalling
        if (Math.abs(power) < 0.5) {
            power = 0.5 * (power > 0.0 ? 1.0 : -1.0);
        }

        slidesMotor.setPower(power);
    }
}