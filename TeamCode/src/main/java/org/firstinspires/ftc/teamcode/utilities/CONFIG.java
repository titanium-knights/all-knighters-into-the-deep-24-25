package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;

@Config
public class CONFIG {
    // drivetrain wheels
    public static String FRONT_LEFT = "fl"; //ch 0
    public static String FRONT_RIGHT = "fr"; //ch 2
    public static String BACK_LEFT = "bl"; //ch 3
    public static String BACK_RIGHT = "br"; //ch 1

    // webcam
    public static String webcam = "Webcam 1";

    // intake
    public static String clawServoBottom = "clawB";
    public static String clawRotator = "clawR"; // clawRotator
    public static String scissorsServo = "scissors";

    // outtake:
    public static String slidesMotor = "slidesMotor";
    public static String armServo = "armServo";
    public static String clawServo = "claw";
    // yuma's active intake
    public static String rightIntake = "rightIntake";
    public static String leftIntake = "leftIntake";


    // pull up (left is with the robot facing forward away from you)
    public static String leftPullUp = "cl";
    public static String rightPullUp = "cr";


    
    // comment out the below if you aren't using specimen sweatshop factory
    public static String servoThatKeepsTheHookInPlace = "sTKTHIP";
    public static String servoThatAppliesForceToHook = "sTAFT";


}