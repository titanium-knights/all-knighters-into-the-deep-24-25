package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Arm {
    DcMotor armMotor;
    public boolean slowMode = false;
    
    // encoder ticks per 360 degrees for 312 rpm motor
    public static double ENCODER_TICKS = 537.7;


    // the below is archaic and very mostly likely wrong pls ignore it
    /* ENCODER TICKS EXPLAINED:
    28 rotations of worm gear : 1 rotation of the gear
    since the motor directly turns the worm gear, that means that now, the gear that is connected to the lift will turn slower
    in other words, it will turn 28 times slower
    that means in one motor rotation, there will be 537.6/28 encoder ticks */

    // position presets
    private static final double INIT_ANGLE = 0;
    private static final double DROP_ANGLE = 30;
    private static final double PICKUP_ANGLE = 210;

    public static double FULL_POWER = 1;
    public static double SLOW_POWER = 0.8 * FULL_POWER;

    public Arm(HardwareMap hmap) {
        this.armMotor = hmap.dcMotor.get(CONFIG.armMotor);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setZeroPowerBehavior(BRAKE);
    }

    // completely stop lift
    public void stop() {
        armMotor.setPower(0);
    }

    // purpose: make sure you don't interrupt a preset
    public boolean isBusy() {
        return armMotor.isBusy();
    }

    // overload the other setPower to default slowMode to false

    /**
     * @param dir: false = to back, true = towards init
     */
    public void setPower(boolean dir) {
        armMotor.setPower(FULL_POWER * (dir ? 1 : -1));
    }

    // Returns lift position in degrees, robot centric (init pos is 0)
    public double getPosition() {
        return -armMotor.getCurrentPosition() / ENCODER_TICKS * 360;
    }

    public boolean runToPosition(double angle) {
        // converts angle into encoder ticks and then runs to position
        // with input validation
        armMotor.setTargetPosition((int) (ENCODER_TICKS *-angle/360));
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // with run to position always positive power (setPower will be the one determining direction)
        // run to position is always in presets or else it'll be jittery
        armMotor.setPower(-0.3);

        return true;
    }

    /**
     * Directions for manual control
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
