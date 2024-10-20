package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

//NEGATIVE IS UP
import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slides {
    // Positive power is counter clockwise,
    //position at initial
    int pos;

    enum MotorState {
        IDLE,
        UP,
        DOWN
    }

    MotorState state;

    //Preset heights,
    // TO DO: CALIBRATE
    int maxHeight = 3480;
    int midHeight = 2424;
    int lowHeight = 1272;
    int dropHeight = 800;

    public Slides(HardwareMap hmap){
        this.slideMotor = hmap.dcMotor.get(CONFIG.slidesMotor);
        this.state = MotorState.IDLE;
        this.pos = 0;

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setZeroPowerBehavior(BRAKE);
    }

    DcMotor slideMotor;

    public int getEncoder(){
        return -slideMotor.getCurrentPosition();
    }

    public int getTarget(){
        return slideMotor.getTargetPosition();
    }

    public DcMotor.RunMode getMode(){
        return slideMotor.getMode();
    }

    public void setPower(double power){
        slideMotor.setPower(-power);
    }

    public void stop(){
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(0);

        pos = getEncoder();

        state = MotorState.IDLE;
    }

    public boolean isBusy() {return slideMotor.isBusy();}

    public void setTarget(int target){
        slideMotor.setTargetPosition(-target);
    }

    public void reset(){
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        pos = 0;
        state = MotorState.IDLE;

        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void runToPosition(){
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(0.9);

    }
    public void todrop(){
        setTarget(dropHeight);
        runToPosition();
    }

    public void tozero(){
        setTarget(0);
        runToPosition();
        pos = getEncoder();
    }

    public void totrue(){
        setTarget(0);
        runToPosition();
        pos = getEncoder();
    }

    public void low(){
        setTarget(lowHeight);
        runToPosition();
        pos = getEncoder();
    }

    public void middle(){
        setTarget(midHeight);
        runToPosition();
        pos = getEncoder();
    }
    public void high(){
        setTarget(maxHeight);
        runToPosition();
        pos = getEncoder();
    }

    public void upHold(){
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();
        if (pos >= maxHeight){
            setPower(0);
            return;
        }
        if (state == MotorState.UP && pos >= midHeight + 1500){
            setPower(1);
            pos = getEncoder();
            return;
        }
        if (state == MotorState.UP){
            return;
        }
        state = MotorState.UP;
        setPower(1);
    }

    public void downHold() {
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pos = getEncoder();

        if (state == MotorState.DOWN && pos <= 1800) {
            setPower(-0.4);
            pos = getEncoder();
            return;
        }

        if (state == MotorState.DOWN) {
            return;
        }
        state = MotorState.DOWN;
        setPower(-0.6);

    }
}