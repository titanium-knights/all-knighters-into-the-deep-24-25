package org.firstinspires.ftc.teamcode.pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.teamcode.utilities.CONFIG;

public class LConstants {
    static {
        ThreeWheelIMUConstants.forwardTicksToInches = 0.0005285356789805085;
        ThreeWheelIMUConstants.strafeTicksToInches = 0.0005330987731201786;
        ThreeWheelIMUConstants.turnTicksToInches = 0.001998322827427511;

        ThreeWheelIMUConstants.leftY = 14.208125;
        ThreeWheelIMUConstants.rightY = -15.08125;
        ThreeWheelIMUConstants.strafeX = .23622;
        ThreeWheelIMUConstants.leftEncoder_HardwareMapName = CONFIG.BACK_LEFT;
        ThreeWheelIMUConstants.rightEncoder_HardwareMapName = CONFIG.BACK_RIGHT;
        ThreeWheelIMUConstants.strafeEncoder_HardwareMapName = CONFIG.FRONT_RIGHT;

        ThreeWheelIMUConstants.leftEncoderDirection = Encoder.FORWARD;
        ThreeWheelIMUConstants.rightEncoderDirection = Encoder.FORWARD;
        ThreeWheelIMUConstants.strafeEncoderDirection = Encoder.FORWARD;

        ThreeWheelIMUConstants.IMU_HardwareMapName = "imu";
        ThreeWheelIMUConstants.IMU_Orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.RIGHT);
    }
}
