package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slides {

    // On the physical robot, the more negative the value, the higher it is
    // Same thing as encoder value
    int pos;

    // Preset encoder values,
    final int maxHeight = -2200;
    final int minHeight = 0;
    final int totalHeight = Math.abs(maxHeight - minHeight);
    final int BUFFER = 20;

    // Preset power values for both directions (based on weight)
    // Negative is up, positive is down
    double slideUpPower = -1.0;
    double slideDownPower = 1.0;
    double idlePower = 0;

    // i don't want to do this but i have to
    boolean hasBeenCalibrated = true;

    int pastEncoderValue = 0;
    int pastEncoderValue2 = 0;

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
        if (hasBeenCalibrated) {
            slideMotor.setPower(0);
        } else {
            slideMotor.setPower(idlePower);
        }
    }

    private boolean encoderValueWithinBufferOfTarget(int targetEncoderValue) {
        return Math.abs(slideMotor.getCurrentPosition() - targetEncoderValue) <= BUFFER;
    }

    public double getChangeInEncoderValues() {
        return slideMotor.getCurrentPosition() - pastEncoderValue;
    }

    private boolean slidesStoppedMoving() {
        return Math.abs(getChangeInEncoderValues()) <= .5;
    }

    public boolean isCalibrated() {
        return hasBeenCalibrated;
    }

    // exists to switch between target encoder values
    public void slideToPosition(SlideState state) {
        if (state == SlideState.MANUALUP) {
            slideMotor.setPower(-.8);
            hasBeenCalibrated = false;
        } else if (state == SlideState.MANUALDOWN) {
            slideMotor.setPower(0.1);
        } else if (encoderValueWithinBufferOfTarget(state.getEncoderValue())) {
            stop();
        } else if (!hasBeenCalibrated && slidesStoppedMoving() && state == SlideState.BOTTOM && slideMotor.getCurrentPosition() > -150) {
            // recalibrate if it's at the bottom and not moving and just came down
            slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            hasBeenCalibrated = true;
            stop();
        } else {
            updateSliderPower(state.getEncoderValue());
            hasBeenCalibrated = false;
        }
        pastEncoderValue = slideMotor.getCurrentPosition();
        pastEncoderValue2 = pastEncoderValue;
    }

    // exists for PID
    private void updateSliderPower(int targetEncoderValue) {
        int pos = slideMotor.getCurrentPosition();
        double distanceAway = targetEncoderValue - pos;
        double multiplier = Math.abs(distanceAway / totalHeight) / 4 + .75;
        if (distanceAway > 0) { // moving down
            slideMotor.setPower(slideDownPower);
        } else if (distanceAway < 0) { // moving up
            slideMotor.setPower(slideUpPower);
        }
    }
}