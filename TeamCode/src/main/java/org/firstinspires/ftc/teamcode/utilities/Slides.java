package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slides {
    // apparently: NEGATIVE IS UP
    // Positive power is counter clockwise,
    // position at initial
    int pos;

    enum MotorState {
        IDLE,
        UP,
        DOWN
    }

    MotorState state;

    //Preset heights,
    int maxHeight = 2180;
    int midHeight = 1000;
    int minHeight = 10;

    // slide state
    String currentSlideState = "DOWN";

    public Slides(HardwareMap hmap) {
        this.slideMotor = hmap.dcMotor.get(CONFIG.slidesMotor);
        this.state = MotorState.IDLE;
        this.pos = 0;

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    DcMotor slideMotor;

    public int getEncoder() {
        return -slideMotor.getCurrentPosition();
    }

    public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
        return slideMotor.getZeroPowerBehavior();
    }

    public DcMotor.RunMode getMode() {
        return slideMotor.getMode();
    }

    public void runToPosition(int encoder) {
        // converts angle into encoder ticks and then runs to position
        slideMotor.setTargetPosition(encoder);

        // with run to position always positive argument (setPower will be the one determining direction)
        // run to position is always in presets or else it'll be jittery
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(1.0);
    }

    public void changeToUpState() {
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();
        if (pos >= maxHeight){
            slideMotor.setPower(0.0);
            return;
        }
        if (state == MotorState.UP && pos >= midHeight + 1500){
            slideMotor.setPower(1.0);
            return;
        }
        if (state == MotorState.UP){
            return;
        }
        state = MotorState.UP;
        slideMotor.setPower(1.0);
    }

    public void changeToDownState() {
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();

        if (state == MotorState.DOWN && pos <= 1800) {
            slideMotor.setPower(-0.4);
            pos = getEncoder();
            return;
        }

        if (state == MotorState.DOWN) {
            return;
        }
        state = MotorState.DOWN;
        slideMotor.setPower(-0.6);
    }
}