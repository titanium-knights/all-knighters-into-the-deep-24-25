package org.firstinspires.ftc.teamcode.utilities;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HorizontalSlides {

    private static final double slideForwardPower = 1;
    private static final double slideBackPower = -1;
    private static final double idlePower = 0.0;
    public final int maxForward = 2150;
    public final int minBack = 0;
    public final int BUFFER = 30;
    private int pos;
    private final DcMotor horizontalSlidesMotor;
    private HorizontalSlidesState currentState;


    public HorizontalSlides(HardwareMap hmap) {
        this.horizontalSlidesMotor = hmap.dcMotor.get(CONFIG.horizontalSlidesMotor);
        horizontalSlidesMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontalSlidesMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        horizontalSlidesMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public int getEncoder() {
        return horizontalSlidesMotor.getCurrentPosition();
    }

    public void stop() {
        horizontalSlidesMotor.setPower(idlePower);
    }

    private boolean encoderValueWithinBufferOfTarget(int targetEncoderValue) {
        return Math.abs(horizontalSlidesMotor.getCurrentPosition() - targetEncoderValue) <= BUFFER;
    }

    public void resetSlideEncoder() {
        horizontalSlidesMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontalSlidesMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.pos = 0;
    }

    public boolean slideToPosition(HorizontalSlidesState state) {
        currentState = state;
        int targetPosition = state.getEncoderValue();
        if (encoderValueWithinBufferOfTarget(targetPosition)) {
            stop();
            return true;
        } else {
            updateSlidesPower(targetPosition);
            return false;
        }
    }

    public double getPower() {
        return horizontalSlidesMotor.getPower();
    }

    public HorizontalSlidesState getSlidesState() {
        return currentState;
    }

    public boolean isIdle() {
        return horizontalSlidesMotor.getPower() == idlePower;
    }

    public void manualForward(double power) {
        horizontalSlidesMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double adjustedPower = Math.abs(power); // Positive for backward movement
        horizontalSlidesMotor.setPower(adjustedPower);
    }

    public void manualBack(double power) {
        horizontalSlidesMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double adjustedPower = -Math.abs(power); // Positive for forward movement
        horizontalSlidesMotor.setPower(adjustedPower);
    }

    private void updateSlidesPower(int targetEncoderValue) {
        horizontalSlidesMotor.setTargetPosition(targetEncoderValue);
        horizontalSlidesMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        horizontalSlidesMotor.setPower(slideForwardPower);
    }
}
