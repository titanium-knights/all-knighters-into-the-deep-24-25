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
    public final int BUFFER = 50;
    private int pos;
    private final DcMotor horizontalSlidesMotor;


    public HorizontalSlides(HardwareMap hmap) {
        this.horizontalSlidesMotor = hmap.dcMotor.get(CONFIG.horizontalSlidesMotor);
        //this.horizontalSlidesMotor = hardwareMap.dcMotor.get(CONFIG.horizontalSlidesMotor);
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
        horizontalSlidesMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.pos = 0;
    }

    public boolean slideToPosition(HorizontalSlidesState state) {
        int targetPosition = state.getEncoderValue();
        if (encoderValueWithinBufferOfTarget(targetPosition)) {
            stop();
            return true;
        } else {
            updateSlidesPower(targetPosition);
            return false;
        }
    }


    public void manualForward(double power) {
        double adjustedPower = Math.abs(power); // Positive for downward movement
        horizontalSlidesMotor.setPower(adjustedPower);
    }

    public void manualBack(double power) {
        double adjustedPower = -Math.abs(power); // Positive for downward movement
        horizontalSlidesMotor.setPower(adjustedPower);
    }

    private void updateSlidesPower(int targetEncoderValue) {
        horizontalSlidesMotor.setTargetPosition(targetEncoderValue);
        horizontalSlidesMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        horizontalSlidesMotor.setPower(slideForwardPower);
    }

//    public void retract() {
//        slideToPosition(minBack);
//    }
//
//    public void goOut() {
//        slideToPosition(maxForward);
//    }
}
