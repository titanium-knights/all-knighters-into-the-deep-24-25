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

    public DcMotor.RunMode getMode() {
        return slideMotor.getMode();
    }

    public void runToPosition() {
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(0.9);
    }

    public void setPower(double power){
        slideMotor.setPower(-0.95*power);
    }

    public void stop(){
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setPower(0);
        pos = getEncoder();
        state = MotorState.IDLE;
        telemetry.addData("Zero Power Behavior", slideMotor.getZeroPowerBehavior());
    }

    public void runToPosition(int encoderPos) {
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(-1.0);
        slideMotor.setTargetPosition(encoderPos);
    }

    public void upHold() {
        runToPosition(maxHeight);
//        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        pos = getEncoder();
//        if (pos >= maxHeight){
//            setPower(0);
//            return;
//        }
//        if (state == MotorState.UP && pos >= midHeight + 15){
//            setPower(1);
//            pos = getEncoder();
//            return;
//        }
//        if (state == MotorState.UP) {
//            return;
//        }
//        state = MotorState.UP;
//        setPower(1);
    }

    public void downHold() {
        runToPosition(10);
//        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        pos = getEncoder();
//
//        if (state == MotorState.DOWN && pos <= 1800) {
//            setPower(-0.4);
//            pos = getEncoder();
//            return;
//        }
//
//        if (state == MotorState.DOWN) {
//            return;
//        }
//        state = MotorState.DOWN;
//        setPower(-0.6);
    }
}