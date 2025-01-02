package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Scissors {

    private final DcMotor scissorsMotor;
    public final int BUFFER = 20;
    public final double idlePower = 0;
    public final double scissorsOut = .1;
    public final double scissorsIn = -.1;

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

    public void stop() {
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
            stop();
            return true;
        } else {
            updateSlidesPowerBasic(state.getEncoderValue());
            return false;
        }
    }

    private void updateSlidesPowerBasic(int targetEncoderValue) {
        int pos = scissorsMotor.getCurrentPosition();
        double distanceAway = targetEncoderValue - pos;
        if (distanceAway > 0) { // moving down
            scissorsMotor.setPower(scissorsOut);
        } else if (distanceAway < 0) { // moving up
            scissorsMotor.setPower(scissorsIn);
        } else {
            scissorsMotor.setPower(idlePower);
        }
    }
}