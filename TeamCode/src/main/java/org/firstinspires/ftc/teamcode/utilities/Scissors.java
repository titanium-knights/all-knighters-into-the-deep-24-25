package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Scissors {

    private final DcMotor scissorsMotor;
    public final int BUFFER = 10;
    public final double idlePowerIN = -.02; // -0.02
    public final double idlePowerOUT = -.05; // -0.05
    public final double scissorsOutPower = -.8; // -0.5
    public final double scissorsInPower = .5; // 0.3

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
            updateSlidesPowerBasic(state.getEncoderValue(), state);
            return false;
        }
    }

    public void manualDown(double power) {
        // Assume power is from 0.0 to 1.0
        double adjustedPower = Math.abs(power); // Positive for downward movement
        scissorsMotor.setPower(adjustedPower);
    }

    private boolean updateSlidesPowerBasic(int targetEncoderValue, ScissorsState state) {
        int pos = scissorsMotor.getCurrentPosition();
        double distanceAway = targetEncoderValue - pos;
        if (distanceAway > 0) { // moving down
            scissorsMotor.setPower(scissorsOutPower);
            return true;
        } else if (distanceAway < 0) { // moving up
            scissorsMotor.setPower(scissorsInPower);
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