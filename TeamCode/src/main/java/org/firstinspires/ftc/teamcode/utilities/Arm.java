package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Arm {
    public DcMotor armMotor;
    public boolean slowMode = false;

    // 1425.1 for 117 rpm motor
    // encoder ticks per 360 degrees for 312 rpm motor
    public static double ENCODER_TICKS = 537.7*28;//28 is to account for the worm gear ratio

    // position presets
    private static final double INIT_ANGLE = 15;
    private static final double DROP_ANGLE = 54;
    private static final double PICKUP_ANGLE = 160;
    private static final double VERTICAL_ANGLE = 90;
    private static final double MAX_ANGLE = PICKUP_ANGLE;//VERTICAL_ANGLE + 115;

    public static double FULL_POWER = 0.7;
    public static double SLOW_POWER = 1.0;

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

    // to ensure presets aren't interrupted
    public boolean isBusy() {
        return armMotor.isBusy();
    }

    /**
     * @param dir: false = to back, true = towards init
     * this function theoretically lowers the power closer to the vertical position
     * **needs testing**
     */
    public void setPower(boolean dir) {
        // armMotor.setPower(SLOW_POWER * (dir ? 1 : -1));
        if (dir) {
            if (getPosition() < VERTICAL_ANGLE) {
                armMotor.setPower(FULL_POWER);
            }
            else if (getPosition() >= VERTICAL_ANGLE && getPosition() < (PICKUP_ANGLE)) {
                armMotor.setPower(SLOW_POWER);
            }
            else {
                armMotor.setPower(0);
            }
        }
        else {
            if (getPosition() > VERTICAL_ANGLE && getPosition() < PICKUP_ANGLE) {
                armMotor.setPower(-SLOW_POWER);
            }
            else if (getPosition() < VERTICAL_ANGLE) {
                armMotor.setPower(-SLOW_POWER * 0.5);
            }
            else {
                armMotor.setPower(0);
            }
        }
    }

    // Returns arm position in degrees, (init pos is 0)
    public double getPosition() {
        return armMotor.getCurrentPosition() / ENCODER_TICKS * 360;
    }

    public boolean runToPosition(double angle) {
        // converts angle into encoder ticks and then runs to position
        armMotor.setTargetPosition((int) (ENCODER_TICKS *angle/360));

        // with run to position always positive argument (setPower will be the one determining direction)
        // run to position is always in presets or else it'll be jittery
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(1.0);
        return true;
    }

    public boolean toPickUpSamples(){
        return runToPosition(230);
    }

    public boolean toDropSamples() {
        return runToPosition(65);
    }

    public boolean toDropSpecimen(){
        return runToPosition(15);
    }

    public boolean inlineWithSlides() {
        return runToPosition(90);
    }
    public boolean goToFoldedPosition() {
        return runToPosition(0);
    }

    /**
     * Directions for manual control
     * these do not work
     * TODO! fix these
     */
    public void setDirectionTowardsInit() {
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(false);
    }

    public void setDirectionAwayFromInit() {
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(true);
    }
}
