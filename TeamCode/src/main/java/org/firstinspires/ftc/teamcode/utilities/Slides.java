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
    int maxHeight = 1900;
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

    public void stop() {
        slideMotor.setPower(-0.3);
        // I set it to -0.3 bc its too heavy to hold up at only 0
        // also in this case negative = going up, pos = going down, hence the - for the 0.3
    }

    public void changeToUpState() {
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();
        if (pos >= maxHeight){ // if its over the max height, stop running motor
            slideMotor.setPower(0.0);
            return;
        }
        state = MotorState.UP;
        slideMotor.setPower(-1.0);
    }

    public void changeToDownState() {
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();
        if (pos < 0) { // if its at the bottom, turn off power
            slideMotor.setPower(0.0);
            return;
        }
        state = MotorState.DOWN;
        slideMotor.setPower(0.3);
    }
}