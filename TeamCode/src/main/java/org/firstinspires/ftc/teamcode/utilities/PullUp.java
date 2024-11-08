package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class PullUp {

    // hello, this is a new branch

    public DcMotor leftMotor;
    public DcMotor rightMotor;

    // position presets
    private static final double BUFFER = 20;

    public static double FULL_POWER = 1;

    public PullUp(HardwareMap hmap) {
        this.leftMotor = hmap.dcMotor.get(CONFIG.leftPullUp);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftMotor.setZeroPowerBehavior(BRAKE);
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        this.rightMotor = hmap.dcMotor.get(CONFIG.rightPullUp);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setZeroPowerBehavior(BRAKE);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    // completely stop arm
    public void stop() {
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

    // Returns arm position in degrees, (init pos is 0)
    public double getLeftEncoderValue() {
        return leftMotor.getCurrentPosition();
    }

    public double getRightEncoderValue() {
        return rightMotor.getCurrentPosition();
    }

    private boolean encoderValueWithinBufferOfTarget(int targetEncoderValue) {
        return Math.abs(leftMotor.getCurrentPosition() - targetEncoderValue) <= BUFFER && Math.abs(rightMotor.getCurrentPosition() - targetEncoderValue) <= BUFFER;
    }

//    public void runToPosition(PullUpState state) {
//        if (encoderValueWithinBufferOfTarget(state.getEncoderValue())) {
//            stop();
//        } else {
//            updatePower(state.getEncoderValue());
//        }
//    }

//    // exists for PID (pointless right now because we use a worm gear)
//    public void updatePower(int targetEncoderValue) {
//        int pos = armMotor.getCurrentPosition();
//        double distanceAway = targetEncoderValue - pos;
//        double multiplier = 1;
//        if (distanceAway > 0) {
//            armMotor.setPower(FULL_POWER * multiplier);
//        } else {
//            armMotor.setPower(-1 * FULL_POWER * multiplier);
//        }
//    }

}
