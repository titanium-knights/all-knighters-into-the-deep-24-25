package org.firstinspires.ftc.teamcode.utilities;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slides {

    // On the physical robot, the more negative the value, the higher it is
    // Same thing as encoder value
    int pos;

    // Preset encoder values,
    final int maxHeight = -2100;
    final int minHeight = 0;
    final int BUFFER = 50;

    // Preset power values for both directions (based on weight)
    // Negative is up, positive is down
    double slideUpPower = -1;
    double slideDownPower = 1;
    double idlePower = -0.3;

    DcMotor slideMotor;

    public Slides(HardwareMap hmap) {
        this.slideMotor = hmap.dcMotor.get(CONFIG.slidesMotor);

        // calibrate the encoder to initial position
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.pos = 0;

        // catch all, but in theory the motor should never be at zero power
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    // for telemetry only
    public int getEncoder() {
        return slideMotor.getCurrentPosition();
    }

    // used in initialization and l as in between movements
    public void stop() {
        // this is the power required to be stationary
        slideMotor.setPower(idlePower);
    }

    private boolean encoderValueWithinBufferOfTarget(int targetEncoderValue) {
        return Math.abs(slideMotor.getCurrentPosition() - targetEncoderValue) <= BUFFER;
    }

    public void resetSlideEncoder() {
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.pos = 0;
    }

    // exists to switch between target encoder values
    public boolean slideToPosition(SlideState state) {
        if (state == SlideState.MANUALUP) {
            slideMotor.setPower(-.8);
            return false;
        } else if (state == SlideState.MANUALDOWN) {
            slideMotor.setPower(0.3);
            return false;
        } else if (encoderValueWithinBufferOfTarget(state.getEncoderValue())) {
            stop();
            return true;
        } else {
            updateSliderPower(state.getEncoderValue());
            return false;
        }
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

    // exists for PID
    private void updateSliderPower(int targetEncoderValue) {
        int pos = slideMotor.getCurrentPosition();
        double distanceAway = targetEncoderValue - pos;
        if (distanceAway > 0) { // moving down
            slideMotor.setPower(slideDownPower);
        } else if (distanceAway < 0) { // moving up
            slideMotor.setPower(slideUpPower);
        }
    }
}