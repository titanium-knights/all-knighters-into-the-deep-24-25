package org.firstinspires.ftc.teamcode.pedroPathing.constants;

import com.pedropathing.localization.Localizers;
import com.pedropathing.follower.FollowerConstants;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.utilities.CONFIG;

public class FConstants {
    static {
        FollowerConstants.localizers = Localizers.THREE_WHEEL;

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
        FollowerConstants.mass = 12.02;

        FollowerConstants.xMovement = 73.36937904945326; // now: 59.07727, 62.428, 57.5, 60.07, 64. Before: 66.14857687058154
        // 3/11: 75.3, 74, 72, 71.9, 73.36

        FollowerConstants.yMovement = 59.183211728341156; // now: 55.2, 52.86, 54
        // 3/11: 61.027, 59.6826, 58.962, 57.86, 59.1

        FollowerConstants.forwardZeroPowerAcceleration = -40.839114466965526; // now: -46, -49, -43, -48, -42 before: -39.623173380073275
        // 3/11: -40.90730301793315, -40.20714596542462, -38.884252254555136, -35.68221072137201, -38.575798938535755, -42.32347886697591
        FollowerConstants.lateralZeroPowerAcceleration = -95;  // before: 71.09840593627987
        // -76.64041014461063, -71.54722691459065, -72.39305562810408, -74.25430689291791, -81.36585978489387
        // @60 in/sec: -77, -93, -95, -92, -91, -89, -121
        // @30 in/sec: -80, -73, -81, -105, -79, -78, -122, -73, -103, -88

        FollowerConstants.translationalPIDFCoefficients.setCoefficients(0.2,0.00002,0.003,0.00);
        FollowerConstants.useSecondaryTranslationalPID = false;
//        FollowerConstants.secondaryTranslationalPIDFCoefficients.setCoefficients(0.1,0,0.01,0); // Not being used, @see useSecondaryTranslationalPID

        FollowerConstants.headingPIDFCoefficients.setCoefficients(3,0.00,0.2,0);
        FollowerConstants.useSecondaryHeadingPID = false;
//        FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(0,0,0.1,0); // Not being used, @see useSecondaryHeadingPID

        FollowerConstants.drivePIDFCoefficients.setCoefficients(0.01,0,0.000001,0.6,0);
        FollowerConstants.useSecondaryDrivePID = false;
//        FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(0.1,0,0,0.6,0); // Not being used, @see useSecondaryDrivePID

        FollowerConstants.zeroPowerAccelerationMultiplier = 5;
        FollowerConstants.centripetalScaling = 0.0005; // used to be .0007

        FollowerConstants.pathEndTimeoutConstraint = 100;
        FollowerConstants.pathEndTValueConstraint = 0.990; // from 0 to 1
        FollowerConstants.pathEndVelocityConstraint = 0.06;
        FollowerConstants.pathEndTranslationalConstraint = 0.1;
        FollowerConstants.pathEndHeadingConstraint = 0.007;

        FollowerConstants.automaticHoldEnd = true;

        FollowerConstants.holdPointHeadingScaling = .12; // default is .35
        FollowerConstants.holdPointTranslationalScaling = .2; // default is .45
    }
}