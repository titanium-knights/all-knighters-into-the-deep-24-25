package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.config.Config;

@Config
public class CONFIG {
    //TODO: label all strings with associated ports and types.
    // ex: ch motor 3 (control hub > motor > port 3)

    // drivetrain wheels
    public static String FRONT_LEFT = "fl";  // ch motor 0
    public static String FRONT_RIGHT = "fr"; // ch motor 2
    public static String BACK_LEFT = "bl";   // ch motor 3
    public static String BACK_RIGHT = "br";  // ch motor 1

    // scissors
    public static String scissorsR= "scissorsR"; // tape: none, ch servo 2
    public static String scissorsL = "scissorsL"; // tape: none, eh servo 5

    // webcam
    public static String webcam = "Webcam 1"; //

    // intake
    public static String clawServoBottom = "clawB"; //
    public static String clawRotator = "clawR"; //
    public static String scissorsServo = "scissors"; //

    // outtake:
    public static String slidesMotor = "slidesMotor"; //
    public static String armServo = "armServo";
    public static String clawServo = "claw";

    // yuma's active intake
    public static String rightIntake = "rightIntake";
    public static String leftIntake = "leftIntake";
}