package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Arm {

    // hello, this is a new branch

    public DcMotor armMotor;

    // position presets
    private final double verticalEncoderValue = 4000;
    private static final double BUFFER = 20;

    public static double FULL_POWER = 1;

    public Arm(HardwareMap hmap) {
        this.armMotor = hmap.dcMotor.get(CONFIG.armMotor);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setZeroPowerBehavior(BRAKE);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    // completely stop arm
    public void stop() {
        armMotor.setPower(0);
    }

    // Returns arm position in degrees, (init pos is 0)
    public double getEncoderValue() {
        return armMotor.getCurrentPosition();
    }

    private boolean encoderValueWithinBufferOfTarget(int targetEncoderValue) {
        return Math.abs(armMotor.getCurrentPosition() - targetEncoderValue) <= BUFFER;
    }

    public void runToPosition(ArmState state) {
        if (encoderValueWithinBufferOfTarget(state.getEncoderValue())) {
            stop();
        } else {
            updatePower(state.getEncoderValue());
        }
    }

    // exists for PID (pointless right now because we use a worm gear)
    public void updatePower(int targetEncoderValue) {
        int pos = armMotor.getCurrentPosition();
        double distanceAway = targetEncoderValue - pos;
        double multiplier = 1;
        if (distanceAway > 0) {
            armMotor.setPower(FULL_POWER * multiplier);
        } else {
            armMotor.setPower(-1 * FULL_POWER * multiplier);
        }
    }

}
