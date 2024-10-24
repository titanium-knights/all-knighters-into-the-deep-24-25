package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Arm {
    DcMotor armMotor;
    public boolean slowMode = false;

    // 1425.1 for 117 rpm motor
    // encoder ticks per 360 degrees for 312 rpm motor
    public static double ENCODER_TICKS = 537.7;//1425.1;

    // position presets
    private static final double INIT_ANGLE = 0;
    private static final double DROP_ANGLE = 54;
    private static final double PICKUP_ANGLE = 160;
    private static final double VERTICAL_ANGLE = 90;
    private static final double MAX_ANGLE = PICKUP_ANGLE;//VERTICAL_ANGLE + 115;

    public static double FULL_POWER = 0.7;
    public static double SLOW_POWER = 1.0;

    public Arm(HardwareMap hmap) {
        this.armMotor = hmap.dcMotor.get(CONFIG.armMotor);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setZeroPowerBehavior(BRAKE);
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
        return -armMotor.getCurrentPosition() / ENCODER_TICKS * 360;
    }

    public boolean runToPosition(double angle) {
        // converts angle into encoder ticks and then runs to position
        armMotor.setTargetPosition((int) (ENCODER_TICKS *-angle/360));

        // with run to position always positive argument (setPower will be the one determining direction)
        // run to position is always in presets or else it'll be jittery
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(-0.3);
        return true;
    }

    public boolean toPickUpSamples(){
        return runToPosition(80);
    }

    public boolean toFoldedPosition(){
        return runToPosition(240);
    }

    public boolean inlineWithSlides() {
        return runToPosition(15);
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
