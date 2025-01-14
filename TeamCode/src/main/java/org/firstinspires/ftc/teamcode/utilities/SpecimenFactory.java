package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SpecimenFactory {
    private final DcMotor slidesMotor;
    public SpecimenFactory(HardwareMap hmap) {
        this.slidesMotor = hmap.dcMotor.get(CONFIG.slidesMotor);

        // Calibrate the encoder to initial position
        slidesMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slidesMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // Ensure the motor brakes when power is zero
        slidesMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
