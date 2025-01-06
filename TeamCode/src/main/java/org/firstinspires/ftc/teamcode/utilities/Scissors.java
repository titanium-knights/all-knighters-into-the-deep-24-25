package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Scissors {

    private final DcMotor scissorsMotor;
    public final int BUFFER = 10;
    public final double idlePowerIN = -.0; // -0.02, -0.02 with tightly screwed
    public final double idlePowerOUT = -.0; // -0.05, -0.05 with tightly screwed
    public final double scissorsOutPower = 1; // 0.5, -0.8 with tightly screwed
    public final double scissorsInPower = -1; // -0.3, 0.5 with tightly screwed

    private int pos;

    public Scissors(HardwareMap hardwareMap) {
        this.scissorsMotor = hardwareMap.dcMotor.get(CONFIG.scissorsMotor);

        // Calibrate the encoder to initial position
        scissorsMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        scissorsMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.pos = 0;

        // Ensure the motor brakes when power is zero
        scissorsMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }


    public int getEncoder() {
        return scissorsMotor.getCurrentPosition();
    }

    public void stop(double idlePower) {
        // The power required to be stationary
        scissorsMotor.setPower(idlePower);
    }

    private boolean encoderValueWithinBufferOfTarget(int targetEncoderValue) {
        return Math.abs(scissorsMotor.getCurrentPosition() - targetEncoderValue) <= BUFFER;
    }

    public void resetSlideEncoder() {
        scissorsMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        scissorsMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.pos = 0;
    }

    // Exists to switch between target encoder values
    public boolean scissorsToPosition(ScissorsState state) {
        if (encoderValueWithinBufferOfTarget(state.getEncoderValue())) {
            if (state == ScissorsState.IN) {
                stop(idlePowerIN);
            } else if (state == ScissorsState.OUT){
                stop(idlePowerOUT);
            }
            return true;
        } else {
            updateScissorsPower(state.getEncoderValue(), state);
            return false;
        }
    }

    public void manualDown(double power) {
        // Assume power is from 0.0 to 1.0
        double adjustedPower = Math.abs(power); // Positive for downward movement
        scissorsMotor.setPower(adjustedPower);
    }
    public void manualUp(double power) {
        // Assume power is from 0.0 to 1.0
        double adjustedPower = -Math.abs(power); // Negative for upward movement
        scissorsMotor.setPower(adjustedPower);
    }

    public double getScissorsPower() {
        return scissorsMotor.getPower();
    }

    private boolean updateScissorsPower(int targetEncoderValue, ScissorsState state) {
        int pos = scissorsMotor.getCurrentPosition();
        double distanceAway = targetEncoderValue - pos;
        if (distanceAway > 0) { // moving in
            scissorsMotor.setPower(scissorsInPower * Math.abs(distanceAway) / 76 * .7 + scissorsInPower * .3);
            return true;
        } else if (distanceAway < 0) { // moving out
            scissorsMotor.setPower(scissorsOutPower * Math.abs(distanceAway) / 76 * .7 + scissorsOutPower * .3);
            return true;
        } else {
            if (state == ScissorsState.IN) {
                scissorsMotor.setPower(idlePowerIN);
                return true;
            } else if (state == ScissorsState.OUT) {
                scissorsMotor.setPower(idlePowerOUT);
                return true;
            } else {
                return false;
            }
        }
    }
}