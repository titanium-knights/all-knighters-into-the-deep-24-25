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

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    DcMotor slideMotor;

    public int getEncoder() {
        return slideMotor.getCurrentPosition();
    }

    public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
        return slideMotor.getZeroPowerBehavior();
    }

    public DcMotor.RunMode getMode() {
        return slideMotor.getMode();
    }

    public void runToPosition(int encoderPos) {
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(1.0);
        slideMotor.setTargetPosition(encoderPos);
    }

    public void changeToUpState() {
        if (!currentSlideState.equals("UP")) {
            currentSlideState = "UP";
            runToPosition(maxHeight);
        }
    }

    public void changeToDownState() {
        if (!currentSlideState.equals("DOWN")) {
            currentSlideState = "DOWN";
            runToPosition(minHeight);
        }
    }
}