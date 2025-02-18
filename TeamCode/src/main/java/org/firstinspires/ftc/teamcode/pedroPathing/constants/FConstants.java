package org.firstinspires.ftc.teamcode.pedroPathing.constants;

import com.pedropathing.localization.Localizers;
import com.pedropathing.follower.FollowerConstants;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.utilities.CONFIG;

public class FConstants {
    static {
        FollowerConstants.localizers = Localizers.THREE_WHEEL_IMU;

        // back of the robot has the vertical slides on it
        FollowerConstants.leftFrontMotorName = CONFIG.BACK_LEFT;
        FollowerConstants.leftRearMotorName = CONFIG.FRONT_LEFT;
        FollowerConstants.rightFrontMotorName = CONFIG.BACK_RIGHT;
        FollowerConstants.rightRearMotorName = CONFIG.FRONT_RIGHT;

        FollowerConstants.leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        FollowerConstants.leftRearMotorDirection = DcMotorSimple.Direction.REVERSE;
        FollowerConstants.rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        FollowerConstants.rightRearMotorDirection = DcMotorSimple.Direction.FORWARD;

        // remember to update this (kg)
        FollowerConstants.mass = 9.63; //10.75; //11.84;

        FollowerConstants.xMovement = 66.14857687058154;
        FollowerConstants.yMovement = 52.546002199434454;

        FollowerConstants.forwardZeroPowerAcceleration = -39.623173380073275;
        FollowerConstants.lateralZeroPowerAcceleration = -71.09840593627987;

        FollowerConstants.translationalPIDFCoefficients.setCoefficients(0.2,0.00002,0.003,0.00);
        FollowerConstants.useSecondaryTranslationalPID = false;
        FollowerConstants.secondaryTranslationalPIDFCoefficients.setCoefficients(0.1,0,0.01,0); // Not being used, @see useSecondaryTranslationalPID

        FollowerConstants.headingPIDFCoefficients.setCoefficients(0.3,0.00,0.002,0.00);
        FollowerConstants.useSecondaryHeadingPID = false;
        FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(0,0,0.1,0); // Not being used, @see useSecondaryHeadingPID

        FollowerConstants.drivePIDFCoefficients.setCoefficients(0.01,0,0.000001,0.6,0);
        FollowerConstants.useSecondaryDrivePID = false;
        FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(0.1,0,0,0.6,0); // Not being used, @see useSecondaryDrivePID

        FollowerConstants.zeroPowerAccelerationMultiplier = 5;
        FollowerConstants.centripetalScaling = 0.0007;

        FollowerConstants.pathEndTimeoutConstraint = 500;
        FollowerConstants.pathEndTValueConstraint = 0.990; // from 0 to 1
        FollowerConstants.pathEndVelocityConstraint = 0.1;
        FollowerConstants.pathEndTranslationalConstraint = 0.3;
        FollowerConstants.pathEndHeadingConstraint = 0.007;

         FollowerConstants.automaticHoldEnd = true;

         FollowerConstants.holdPointHeadingScaling = .12; // default is .35
        FollowerConstants.holdPointTranslationalScaling = .5; // default is .45
    }
}
