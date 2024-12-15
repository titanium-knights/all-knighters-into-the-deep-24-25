package org.firstinspires.ftc.teamcode.utilities;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slides {

    // On the physical robot, the more negative the value, the higher it is
    // Same thing as encoder value
    private int pos;

    // Preset encoder values
    public final int maxHeight = -2200;
    public final int minHeight = 0;
    public final int BUFFER = 50;

    // Preset power values for both directions (based on weight)
    // Negative is up, positive is down
    private double slideUpPower = -1.0;
    private double slideDownPower = 1.0;
    private static double idlePower = -0.3;

    private DcMotor slideMotor;

    public Slides(HardwareMap hmap) {
        this.slideMotor = hmap.dcMotor.get(CONFIG.slidesMotor);

        // Calibrate the encoder to initial position
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.pos = 0;

        // Ensure the motor brakes when power is zero
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    // For telemetry only
    public int getEncoder() {
        return slideMotor.getCurrentPosition();
    }

    // Used in initialization and as in-between movements
    public void stop() {
        // This is the power required to be stationary
        if (Math.abs(slideMotor.getCurrentPosition()) > 100) {
            slideMotor.setPower(idlePower);
        } else {
            slideMotor.setPower(0.0);
        }
    }

    private boolean encoderValueWithinBufferOfTarget(int targetEncoderValue) {
        return Math.abs(slideMotor.getCurrentPosition() - targetEncoderValue) <= BUFFER;
    }

    public void resetSlideEncoder() {
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.pos = 0;
    }

    // Exists to switch between target encoder values
    public boolean slideToPosition(SlideState state) {
        if (encoderValueWithinBufferOfTarget(state.getEncoderValue())) {
            stop();
            return true;
        } else {
            updateSliderPowerBasic(state.getEncoderValue());
            return false;
        }
    }

    // Manual control methods accepting controller values
    public void manualUp(double power) {
        // Assume power is from 0.0 to 1.0
        double adjustedPower = -Math.abs(power); // Negative for upward movement
        slideMotor.setPower(adjustedPower);
    }

    public void manualDown(double power) {
        // Assume power is from 0.0 to 1.0
        double adjustedPower = Math.abs(power); // Positive for downward movement
        slideMotor.setPower(adjustedPower);
    }

    public class SlideAction implements Action {
        SlideState slideState;

        public SlideAction(SlideState slideState) {
            this.slideState = slideState;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            slideToPosition(slideState);
            return false;
        }
    }

    public Action getSlideAction(SlideState slideState) {
        return new SlideAction(slideState);
    }

    // Exists for basic proportional control (could be expanded to PID)
    private void updateSlidesPower(int targetEncoderValue) {
        int currentPos = slideMotor.getCurrentPosition();
        double distanceAway = targetEncoderValue - currentPos;

        // Simple proportional control
        double kP = 0.001; // Proportional gain (adjust as needed)
        double power = kP * distanceAway;

        // Limit power to max values
        power = Math.max(-1.0, Math.min(1.0, power));
        if (Math.abs(power) < 0.5) {
            power = 0.5 * (power > 0.0 ? 1.0 : -1.0);
        }
        slideMotor.setPower(power);
    }

    private void updateSliderPowerBasic(int targetEncoderValue) {
        int pos = slideMotor.getCurrentPosition();
        double distanceAway = targetEncoderValue - pos;
        if (distanceAway > 0) { // moving down
            slideMotor.setPower(slideDownPower);
        } else if (distanceAway < 0) { // moving up
            slideMotor.setPower(slideUpPower);
        }
    }
}
