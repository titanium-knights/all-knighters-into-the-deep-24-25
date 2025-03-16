package org.firstinspires.ftc.teamcode.teleop.state;

import static org.firstinspires.ftc.teamcode.teleop.Teleop.SLOW_MODE_MULTIPLIER;

import static java.lang.Double.min;
import static java.lang.Math.max;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.teleop.Teleop;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.Base64;
import java.util.stream.Collectors;

@Config
public class BeforeSamplePickupAutomatedv2 extends TeleopState {
    HardwareMap hmap;
    Telemetry telemetry;
    public double angle, rotationAngle, rotationTheta = Math.PI / 2, angleSeen;

    public static final double WINDOW_X = 640.0; // max range is 320
    public static final double WINDOW_Y = 480.0;
    public static final double WINDOW_Y_IN = 2 + 23.0/32.0  ;
    public static final double WINDOW_X_IN = 3 + 10.0/16.0;
    public static final double INTOENCODER = 537.7/120*25.4;
    public boolean pickupable = false;
    public static double slideSpeed = 0.7;
    public static int servoRotationDelay = 1500;

    public static double slidesWithdrawForAdjust = -1;

    public static double slidesAdvanceForPickUp = 4;

    public boolean wristRotated = false;
    public boolean slidesExtending = false;
    public boolean objectDetected = false;
    public boolean objectInFrame = false;
    public boolean slidesInPosition = false;
    public boolean pictureTaken = false;
    public boolean readyForPickup = false;
    public boolean timeReset = false;
    public boolean finishedPickup = false;
    public boolean adjusting = false;
    public boolean minAreaMet = false;

    public int yCoordOfFoundThing = -1;

    public final ConfidenceOrientationVectorPipeline.DetectionResultScaledData defaultDRSD = new ConfidenceOrientationVectorPipeline.DetectionResultScaledData(-1, -1, -1, -1);

    ElapsedTime time = new ElapsedTime();

    double xCoord, yCoord, encoder;

    public static int pickUpWindow = 25;
    public static int clawMidpoint = 300;

    public static double adjustmentToClaw = 3;

    public static double driveSpeed = 0.3;

    public static double minDetectionValue = 250;

    public boolean inited = false;
    public BeforeSamplePickupAutomatedv2(SubsystemManager subsystemManager, HardwareMap hmap, Telemetry telemetry) {
        super(subsystemManager);
        this.hmap = hmap;
        this.telemetry = telemetry;
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2){
        Teleop.setSlowMode(true);
        if (!inited) {
            subsystemManager.bottomClaw.neutralClawRotatorPosition();
            subsystemManager.bottomClaw.openClaw();
            inited = true;
        }
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.bottomClaw.rightWristDownPosition(); // claw is rotated down
        subsystemManager.topClaw.open();
        subsystemManager.arm.toGetOutOfWay();

        if (!timeReset){time.reset(); timeReset=true;}

        if (!wristRotated){
            if (time.milliseconds() > servoRotationDelay){
                wristRotated = true;
            } else{
                if (!(Math.abs(subsystemManager.horizontalSlides.getEncoder()) <= subsystemManager.horizontalSlides.maxForward)){
                    subsystemManager.horizontalSlides.stop();
                    telemetry.addLine("we have stopped moving slides");
                    telemetry.update();
                }
                return;
            }
        }
        extendToPickupPosition(gamepad1, gamepad2);
    }

    // It is done. My legacy.
    // Just when I thought I was out, they pull me back in.
    public void extendToPickupPosition(Gamepad gamepad1, Gamepad gamepad2){
        if (finishedPickup) {
            telemetry.addLine("we finished pickup!");
            return;
        }
        ConfidenceOrientationVectorPipeline.DetectionResultScaledData drsd;
        try {
            drsd = subsystemManager.webcam.bestDetectionCoordsAngle();
        } catch (Exception e) {
            drsd = defaultDRSD;
        }
        if (!slidesExtending && !objectDetected && Math.abs(subsystemManager.horizontalSlides.getEncoder()) <= subsystemManager.horizontalSlides.maxForward){
            telemetry.addLine("we have started moving slides");
            subsystemManager.horizontalSlides.manualForward(slideSpeed);
            slidesExtending = true;
        }

        if ((!adjusting && objectDetected) || !(Math.abs(subsystemManager.horizontalSlides.getEncoder()) <= subsystemManager.horizontalSlides.maxForward)){
            telemetry.addLine("we have stopped moving slides");
            subsystemManager.horizontalSlides.stop();
        }

        if (!objectDetected){
            telemetry.addLine("We have not detected the object");
            if (Math.abs(subsystemManager.horizontalSlides.getEncoder()) >= minDetectionValue) { // change this
                telemetry.addLine("we have seen something!");
                telemetry.addData("horizontal slides are at: ", Math.abs(subsystemManager.horizontalSlides.getEncoder()));
                try {
                    drsd = subsystemManager.webcam.bestDetectionCoordsAngle();
                } catch (Exception e) {
                    drsd = defaultDRSD;
                }

                xCoord = drsd.getX();
                yCoord = drsd.getY();

                encoder = subsystemManager.horizontalSlides.getEncoder();
                encoder = max(encoder - slidesWithdrawForAdjust*INTOENCODER, -subsystemManager.horizontalSlides.maxForward);

                if (yCoord >= 100) {
                    telemetry.addLine("we have seen something at the right position");
                    objectDetected = true;
                    subsystemManager.slides.stop();
                    subsystemManager.horizontalSlides.slideToPosition((int) encoder);
                } else{
                    telemetry.update();
                    return;
                }
            }
        } else if (!pickupable) {

            try {
                drsd = subsystemManager.webcam.bestDetectionCoordsAngle();
            } catch (Exception e) {
                drsd = defaultDRSD;
            }

            telemetry.addLine("we are moving horizontally to align");
            xCoord = drsd.getX() == -1 ? xCoord : drsd.getX();
            yCoord = drsd.getY() == -1 ? yCoord : drsd.getY();
            minAreaMet = drsd.pickupable;

            if (xCoord != -1 && xCoord < clawMidpoint - pickUpWindow){
                subsystemManager.drive.move(driveSpeed, 0, 0);
                telemetry.addData("move: ", "positive");
            } else if (xCoord != -1 && xCoord > clawMidpoint + pickUpWindow){
                telemetry.addData("move: ", "negative");
                subsystemManager.drive.move(-driveSpeed, 0, 0);
            } else if (xCoord != -1 && minAreaMet){
                pickupable = true;
                telemetry.addLine("we've reached the pickupable point");
                subsystemManager.drive.move(0, 0, 0);
                angle = drsd.getTheta();
                pickup(angle, xCoord, yCoord, minAreaMet);
            } else {
                subsystemManager.drive.move(0, 0, 0);
                telemetry.addData("alteration: ", drsd.size);
            }
            adjusting = true;

            telemetry.addData("pickupable? ", pickupable);
            telemetry.addData("y coord: ", yCoord);
            telemetry.addData("x coord: ", xCoord);

            telemetry.update();
        }
    }

    @Override
    public void reset(){
        wristRotated = false;
        slidesExtending = false;
        objectDetected = false;
        objectInFrame = false;
        slidesInPosition = false;
        pictureTaken = false;
        readyForPickup = false;
        timeReset = false;
        yCoord = -1;
        xCoord = 320;
        pickupable = false;
        encoder = 0;
        finishedPickup = false;
        inited = false;
        adjusting = false;
        minAreaMet = false;

    }

    public void pickup (double angle, double xCoord, double yCoord, boolean size){
        angleSeen = angle;

        double oldEncoder = encoder;
        encoder = max(encoder - ((WINDOW_Y/2 - yCoord)/WINDOW_Y*WINDOW_Y_IN + adjustmentToClaw)*INTOENCODER, -subsystemManager.horizontalSlides.maxForward);
        telemetry.addLine("Object Detected: " + xCoord + ", "+ yCoord + ", " + angle + ", " + encoder + ", " + oldEncoder);
        subsystemManager.horizontalSlides.slideToPosition((int) encoder);
        adjusting = true;
        time.reset();
        telemetry.update();


        telemetry.addLine("we think we can pick up and now we are adjusting and running the pick up portion");

        // WARNING DO NOT TRY TO UNDERSTAND THIS, IT MAKES NO SENSE BUT IT WORKS
        angle = angle % 180;
        if (angle < 0) {
            angle += 180;
        }
        telemetry.addData("angle: ", angle);

        rotationAngle = (angle + 90) % 180;
        telemetry.addData("rotation angle: ", rotationAngle);

        rotationTheta = Math.PI - (((rotationAngle * Math.PI) / 180));
        while (rotationTheta > 2 * Math.PI) {
            rotationTheta -= 2 * Math.PI;
        }

        while (rotationTheta < 0) {
            rotationTheta += 2 * Math.PI;
        }

        if (rotationTheta < Math.PI/2) rotationTheta += Math.PI;


        telemetry.addData("rotationTheta: ", rotationTheta);
        subsystemManager.bottomClaw.rotate(rotationTheta);
        finishedPickup = true;
    }
}
