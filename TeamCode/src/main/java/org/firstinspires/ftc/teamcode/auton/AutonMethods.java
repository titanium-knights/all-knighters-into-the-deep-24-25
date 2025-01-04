package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.TopClaw;
import org.firstinspires.ftc.teamcode.utilities.Scissors;


public abstract class AutonMethods extends LinearOpMode {
    public SimpleMecanumDrive drivetrain;
    public Slides slides;
    public TopClaw topClaw;
    public Scissors scissors;


    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new SimpleMecanumDrive(hardwareMap);
        slides = new Slides(hardwareMap);
        topClaw = new TopClaw(hardwareMap);
        scissors = new Scissors(hardwareMap);
    }

    public final double POWER = 0.5; //12.58 V

    public void stopDrive() {
        drivetrain.move(0, 0, 0);
        sleep(100);
    }

    // FORWARD AND BACKWARD ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void backOneee() { // slightly more than backOne
        drivetrain.move(0,POWER, 0);
        sleep(1450);
        stopDrive();
    }

    public void backOne () {
        drivetrain.move(0,POWER, 0);
        sleep(980);
        stopDrive();
    }

    public void backTwo() {
        drivetrain.move(0,POWER, 0);
        sleep(2000);
        stopDrive();
    }
    public void backFour() { // turns slightly right
        drivetrain.move(0,POWER, 0);
        sleep(4000);
        stopDrive();
    }
    public void backHalf() {
        drivetrain.move(0,POWER, 0);
        sleep(500);
        stopDrive();
    }
    public void forwardHalf() {
        drivetrain.move(0,-POWER, 0);
        sleep(900);
        stopDrive();
    }
    public void forwardOne() {
        drivetrain.move(0,-POWER, 0);
        sleep(980);
        stopDrive();
    }
    public void forwardNineFourths() {
        drivetrain.move(0,-POWER,0);
        sleep(900);
        stopDrive();
    }
    public void forwardOneHalf() {
        drivetrain.move(0, -POWER, 0);
        sleep(1800);
        stopDrive();
    }
    public void forwardTwo() {
        drivetrain.move(0, -POWER, 0);
        sleep(2200);
        stopDrive();
    }
    public void forwardFour() { // turns slightly left
        drivetrain.move(0,-POWER, 0);
        sleep(3500);
        stopDrive();
    }
    public void forwardOneFourth() {
        drivetrain.move(0,-POWER, 0);
        sleep(450);
        stopDrive();
    }
    public void forwardOneEighth() {
        drivetrain.move(0,-POWER, 0);
        sleep(360);
        stopDrive();
    }
    public void forwardThree() { // TODO: tune this value
        drivetrain.move(0, -POWER, 0);
        sleep(3900);
        stopDrive();
    }
    public void backThree() { // TODO: tune this
        drivetrain.move(0,POWER, 0);
        sleep(3900);
        stopDrive();
    }

    public void backOneEighth() {
        drivetrain.move(0,POWER, 0);
        sleep(360);
        stopDrive();
    }
    public void backOneFourth() {
        drivetrain.move(0,POWER, 0);
        sleep(450);
        stopDrive();
    }

    public void backOneSixteenth() {
        drivetrain.move(0,POWER, 0);
        sleep(150);
        stopDrive();
    }

    // DIAGONAL //////////////////////////////////////////////////////////////////////////////////////////
    public void diagonalBackRightOne() {
        drivetrain.move(POWER,-POWER, 0);
        sleep(600);
        stopDrive();
    }
    public void diagonalForwardRightOne() {
        drivetrain.move(POWER,-POWER, 0);
        sleep(800);
        stopDrive();
    }



    // TURNING ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void turnClockwise() {
        drivetrain.move(0,0, -POWER);
        sleep(890);
        stopDrive();
    }
    public void turnCounterClockwise() {
        drivetrain.move(0,0, POWER);
        sleep(1150);
        stopDrive();
    }

    // STRAFING ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void rightOneEighth() {
        drivetrain.move(-POWER, 0, 0);
        sleep(200);
        stopDrive();
    }
    public void rightOneFourth() {
        drivetrain.move(POWER, 0, 0);
        sleep(400);
        stopDrive();
    }

    public void rightHalf() {
        drivetrain.move(POWER, 0, 0);
        sleep(1500);
        stopDrive();
    }
    public void rightOne() {
        drivetrain.move(POWER, 0, 0);
        sleep(1200);
        stopDrive();
    }
    public void rightOneHalf() {
        drivetrain.move(POWER, 0, 0);
        sleep(3000);
        stopDrive();
    }

    public void rightTwo() {
        drivetrain.move(POWER, 0, 0);
        sleep(2200);
        stopDrive();
    }
    public void rightFour() {
        drivetrain.move(POWER,0, 0);
        sleep(5200);
        stopDrive();
    }

    public void leftHalf() {
        drivetrain.move(-POWER, 0, 0);
        sleep(800);
        stopDrive();
    }

    public void leftFour() { // NOTE: moves slightly forward
        drivetrain.move(-POWER,0, 0);
        sleep(5600);
        stopDrive();
    }

    public void leftOneEighth() {
        drivetrain.move(-POWER, 0, 0);
        sleep(200); // TODO: tune
        stopDrive();
    }

    public void leftOne() {
        drivetrain.move(-POWER, 0, 0);
        sleep(2000);
        stopDrive();
    }

    public void leftOneHalf() {
        drivetrain.move(-POWER, 0, 0);
        sleep(1600);
        stopDrive();
    }

    public void leftOneFourth() {
        drivetrain.move(-POWER, 0, 0);
        sleep(400);
        stopDrive();
    }

    public void leftTwo() {
        drivetrain.move(-POWER, 0, 0);
        sleep(2200);
        stopDrive();
    }

    // SCORING /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void attachSpecimenToHigherBar() {
        slides.slideToPosition(SlideState.BOTTOM);
        boolean slidesAtPosition = false;
        while (!slidesAtPosition && opModeIsActive()) {
            slidesAtPosition = slides.slideToPosition(SlideState.MEDIUM);
            topClaw.close();
        }
        sleep(500);

        forwardOne();
        topClaw.close();

        slidesAtPosition = false;
        while (!slidesAtPosition && opModeIsActive()) {
            slidesAtPosition = slides.slideToPosition(SlideState.MEDIUM_SCORE);
            topClaw.close();
        }
        sleep(500);

        topClaw.open();
        sleep(500);

        slidesAtPosition = false;
        while (!slidesAtPosition && opModeIsActive()) {
            slidesAtPosition = slides.slideToPosition(SlideState.BOTTOM);
        }
        sleep(500);
    }
    public void pickUpFromWall() {
        drivetrain.move(0, 1, 0);
        sleep(300);
        topClaw.open();
        sleep(500);
        topClaw.close();
    }

}