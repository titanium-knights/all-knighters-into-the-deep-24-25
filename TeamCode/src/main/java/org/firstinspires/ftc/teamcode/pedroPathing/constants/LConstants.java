package org.firstinspires.ftc.teamcode.pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.teamcode.utilities.CONFIG;

public class LConstants {
    static {
//        ThreeWheelIMUConstants.forwardTicksToInches = 0.0005285356789805085;
//        ThreeWheelIMUConstants.strafeTicksToInches = 0.0005330987731201786;
//        ThreeWheelIMUConstants.turnTicksToInches = 0.00345396830067402;
//        // before: 0.001998322827427511
//        // before: 0.00200977183130706
//        // 0.0034439643852543964
//        // 0.00344915278048366
//        // 0.003456964791976623
//        // 0.003519727204886374
//
//
//        ThreeWheelIMUConstants.leftY = 14.208125;
//        ThreeWheelIMUConstants.rightY = -15.08125;
//        ThreeWheelIMUConstants.strafeX = .23622;
//        ThreeWheelIMUConstants.leftEncoder_HardwareMapName = CONFIG.BACK_LEFT;
//        ThreeWheelIMUConstants.rightEncoder_HardwareMapName = CONFIG.BACK_RIGHT;
//        ThreeWheelIMUConstants.strafeEncoder_HardwareMapName = CONFIG.FRONT_RIGHT;
//
//        ThreeWheelIMUConstants.leftEncoderDirection = Encoder.FORWARD;
//        ThreeWheelIMUConstants.rightEncoderDirection = Encoder.FORWARD;
//        ThreeWheelIMUConstants.strafeEncoderDirection = Encoder.FORWARD;
//
//        ThreeWheelIMUConstants.IMU_HardwareMapName = "imu";
//        ThreeWheelIMUConstants.IMU_Orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.RIGHT);

        ThreeWheelConstants.forwardTicksToInches = 0.0005285356789805085;
        ThreeWheelConstants.strafeTicksToInches = 0.0005330987731201786;
        ThreeWheelConstants.turnTicksToInches = 0.0014035813648817649;//0.00345396830067402; //0.001998322827427511

        ThreeWheelConstants.leftY = 14.208125;
        ThreeWheelConstants.rightY = -15.08125;
        ThreeWheelConstants.strafeX = .23622;
        ThreeWheelConstants.leftEncoder_HardwareMapName = CONFIG.BACK_LEFT;
        ThreeWheelConstants.rightEncoder_HardwareMapName = CONFIG.BACK_RIGHT;
        ThreeWheelConstants.strafeEncoder_HardwareMapName = CONFIG.FRONT_RIGHT;

        ThreeWheelConstants.leftEncoderDirection = Encoder.FORWARD;
        ThreeWheelConstants.rightEncoderDirection = Encoder.FORWARD;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.FORWARD;
    }
}
