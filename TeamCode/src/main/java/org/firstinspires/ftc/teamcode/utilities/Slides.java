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
    int minHeight = 0;

    public Slides(HardwareMap hmap) {
        this.slideMotor = hmap.dcMotor.get(CONFIG.slidesMotor);
        this.pos = 0;

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    DcMotor slideMotor;

    public int getEncoder() {
        return -slideMotor.getCurrentPosition();
    }

    public void stop() {
        slideMotor.setPower(-0.3);
        // I set it to -0.3 bc its too heavy to hold up at only 0
        // also in this case negative = going up, pos = going down, hence the - for the 0.3
    }

//    public void setTargetPosition(int encoderValue) {
//        pos = getEncoder();
//        if (pos == encoderValue) {
//            stop();
//        } else {
//            slideMotor.setTargetPosition(encoderValue);
//            slideMotor.setPower(power);
//            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        }
//    }

    public void changeToUpState() {
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();
        if (pos >= maxHeight){ // if its over the max height, stop running motor
            slideMotor.setPower(0.0);
        } else {
            slideMotor.setPower(-1.0);
        }
    }

    public void changeToDownState() {
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();
        if (pos < minHeight) { // if its at the bottom, turn off power
            slideMotor.setPower(0.0);
        } else {
            slideMotor.setPower(0.3);
        }
    }
}