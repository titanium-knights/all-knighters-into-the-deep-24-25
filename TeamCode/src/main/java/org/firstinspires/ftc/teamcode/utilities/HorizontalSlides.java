package org.firstinspires.ftc.teamcode.utilities;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HorizontalSlides {

    private static final double slideForwardPower = 1.0;
    private static final double slideBackPower = -1.0;
    private static final double idlePower = 0.0;
    public final int maxForward = 590;
    public final int minBack = 0;
    public final int BUFFER = 50;
    private final DcMotor horizontalSlidesMotor;
    private int pos;

    public HorizontalSlides(HardwareMap hmap) {
        this.horizontalSlidesMotor = hardwareMap.dcMotor.get(CONFIG.horizontalSlidesMotor);
        horizontalSlidesMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontalSlidesMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.pos = 0;
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

    public boolean slideToPosition(int targetPosition) {
        if (encoderValueWithinBufferOfTarget(targetPosition)) {
            stop();
            return true;
        } else {
            updateSlidesPowerBasic(targetPosition);
            return false;
        }
    }

    public void manualForward(double power) {
        double adjustedPower = Math.abs(power);
        horizontalSlidesMotor.setPower(adjustedPower);
    }

    public void manualBack(double power) {
        double adjustedPower = -Math.abs(power);
        horizontalSlidesMotor.setPower(adjustedPower);
    }

    private void updateSlidesPowerBasic(int targetEncoderValue) {
        int pos = horizontalSlidesMotor.getCurrentPosition();
        double distanceAway = targetEncoderValue - pos;
        if (distanceAway > 0) { // forward
            horizontalSlidesMotor.setPower(slideForwardPower);
        } else if (distanceAway < 0) { // back
            horizontalSlidesMotor.setPower(slideBackPower);
        } else {
            horizontalSlidesMotor.setPower(idlePower);
        }
    }

    private void updateSlidesPower(int targetEncoderValue) {
        int currentPos = horizontalSlidesMotor.getCurrentPosition();
        double distanceAway = targetEncoderValue - currentPos;
        double kP = 0.001;
        double power = kP * distanceAway;
        power = Math.max(-1.0, Math.min(1.0, power));

        if (Math.abs(power) < 0.5) {
            power = 0.5 * (power > 0.0 ? 1.0 : -1.0);
        }

        horizontalSlidesMotor.setPower(power);
    }

    public void retract() {
        slideToPosition(minBack);
    }

    public void goOut() {
        slideToPosition(maxForward);
    }
}
