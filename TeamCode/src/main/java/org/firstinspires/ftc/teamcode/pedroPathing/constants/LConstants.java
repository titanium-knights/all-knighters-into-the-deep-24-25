package org.firstinspires.ftc.teamcode.pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.teamcode.utilities.CONFIG;

public class LConstants {
    static {
        ThreeWheelIMUConstants.forwardTicksToInches = .001989436789;
        ThreeWheelIMUConstants.strafeTicksToInches = .001989436789;
        ThreeWheelIMUConstants.turnTicksToInches = .001989436789;
        ThreeWheelIMUConstants.leftY = 5.551181;
        ThreeWheelIMUConstants.rightY = -5.866142;
        ThreeWheelIMUConstants.strafeX = .23622;
        ThreeWheelIMUConstants.leftEncoder_HardwareMapName = CONFIG.BACK_RIGHT;
        ThreeWheelIMUConstants.rightEncoder_HardwareMapName = CONFIG.FRONT_LEFT;
        ThreeWheelIMUConstants.strafeEncoder_HardwareMapName = CONFIG.FRONT_RIGHT;
        // TODO: check directions
        ThreeWheelIMUConstants.leftEncoderDirection = Encoder.REVERSE;
        ThreeWheelIMUConstants.rightEncoderDirection = Encoder.REVERSE;
        ThreeWheelIMUConstants.strafeEncoderDirection = Encoder.FORWARD;

        ThreeWheelIMUConstants.IMU_HardwareMapName = "imu";
        ThreeWheelIMUConstants.IMU_Orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.LEFT);
        //TODO: check the orientation of the IMU
    }
}
