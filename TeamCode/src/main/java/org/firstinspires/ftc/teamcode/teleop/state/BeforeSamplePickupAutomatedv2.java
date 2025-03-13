package org.firstinspires.ftc.teamcode.teleop.state;

import static org.firstinspires.ftc.teamcode.teleop.Teleop.SLOW_MODE_MULTIPLIER;

import static java.lang.Double.min;
import static java.lang.Math.max;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
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
    public double ogAngle, angle, rotationAngle, rotationTheta;

    public static final int WINDOW = 160; // max range is 320

    public static final double INTOENCODER = 537.7/120*25.4;
    public boolean pickupable;
    public static double slideSpeed = 0.7;
    public static int servoRotationDelay = 1500;

    public static double slidesWithdrawForAdjust = -0.5;

    public static double slidesAdvanceForPickUp = 4;


    public boolean wristRotated;
    public boolean slidesExtending;
    public boolean objectDetected;
    public boolean objectInFrame;
    public boolean slidesInPosition;
    public boolean pictureTaken;
    public boolean readyForPickup;
    public boolean timeReset;

    ElapsedTime time;

    double xCoord, yCoord, encoder, theta;

    //    public Point[] points;
    public BeforeSamplePickupAutomatedv2(SubsystemManager subsystemManager, HardwareMap hmap, Telemetry telemetry) {
        super(subsystemManager);
        this.hmap = hmap;
        this.telemetry = telemetry;
    }

    @Override
    public void runState(Gamepad gamepad1, Gamepad gamepad2) {
        Teleop.setSlowMode(true);
        subsystemManager.slides.slideToPosition(SlideState.BOTTOM);
        subsystemManager.bottomClaw.neutralClawRotatorPosition();
        subsystemManager.bottomClaw.rightWristDownPosition(); // claw is rotated down
        subsystemManager.bottomClaw.openClaw();
        subsystemManager.topClaw.open();
        subsystemManager.arm.toGetOutOfWay();

        if (!timeReset){time.reset(); timeReset=true;}

        if (!wristRotated){
            if (time.milliseconds() > servoRotationDelay){
                wristRotated = true;
            } else{
                return;
            }
        }

        extendToPickupPosition(gamepad1, gamepad2);
    }

    // It is done. My legacy.
    // Just when I thought I was out, they pull me back in.
    public void extendToPickupPosition(Gamepad gamepad1, Gamepad gamepad2) {
        ConfidenceOrientationVectorPipeline.DetectionResultScaledData drsd = subsystemManager.webcam.bestDetectionCoordsAngle();
        if (!slidesExtending){
            subsystemManager.horizontalSlides.manualForward(slideSpeed);
            slidesExtending = true;
        }
        if (Math.abs(subsystemManager.horizontalSlides.getEncoder()) <= subsystemManager.horizontalSlides.maxForward - (slidesAdvanceForPickUp - slidesWithdrawForAdjust) * INTOENCODER){
            subsystemManager.horizontalSlides.stop();
            return;
        }
        if (!objectDetected){
            yCoord = -1;
            xCoord = 320;
            pickupable = false;
            encoder = 0;

            if (Math.abs(subsystemManager.horizontalSlides.getEncoder()) >= 40) { // change this
                telemetry.addLine("we got here!");
                telemetry.update();
                drsd = subsystemManager.webcam.bestDetectionCoordsAngle();
                xCoord = drsd.getX();
                yCoord = drsd.getY();
                pickupable = drsd.pickupable;
                theta = drsd.getTheta();

                encoder = subsystemManager.horizontalSlides.getEncoder();
                encoder = max(encoder - slidesWithdrawForAdjust*INTOENCODER, -subsystemManager.horizontalSlides.maxForward);
            }

            if (yCoord != -1){
                objectDetected = true;
            } else{
                return;
            }

            subsystemManager.horizontalSlides.stop();
            if (pickupable) {
//                thetas.add(drsd.getTheta());
            }

        }

        ArrayList<Double> thetas = new ArrayList<>();

        while (yCoord == -1 && Math.abs(subsystemManager.horizontalSlides.getEncoder()) <= subsystemManager.horizontalSlides.maxForward - (slidesAdvanceForPickUp - slidesWithdrawForAdjust) * INTOENCODER) {
            telemetry.addLine("y coordinate: " + yCoord);
            telemetry.addLine("horizontal slides: " + Math.abs(subsystemManager.horizontalSlides.getEncoder()));
            telemetry.addLine("horizontal slides power: " + subsystemManager.horizontalSlides.getPower());
            telemetry.addLine("angle detected:" + drsd.getTheta());
            telemetry.update();

            subsystemManager.horizontalSlides.manualForward(slideSpeed);

            if (Math.abs(subsystemManager.horizontalSlides.getEncoder()) >= 40) { // change this
                telemetry.addLine("we got here!");
                telemetry.update();
                drsd = subsystemManager.webcam.bestDetectionCoordsAngle();
                xCoord = drsd.getX();
                yCoord = drsd.getY();
                pickupable = drsd.pickupable;

                encoder = subsystemManager.horizontalSlides.getEncoder();
                encoder = max(encoder - slidesWithdrawForAdjust*INTOENCODER, -subsystemManager.horizontalSlides.maxForward);
            }

            if (pickupable) {
                thetas.add(drsd.getTheta());
            }
        }

//        points=drsd.points;

        telemetry.addLine("out of the loop!");

        while (!pickupable && Math.abs(subsystemManager.horizontalSlides.getEncoder()) <= subsystemManager.horizontalSlides.maxForward - (slidesAdvanceForPickUp - slidesWithdrawForAdjust) * INTOENCODER - 100) {
            if (xCoord != -1 && xCoord < 2*WINDOW - 50){
                subsystemManager.drive.move(0.3, 0, 0);
                telemetry.addData("move: ", "positive");
            } else if (xCoord > 2*WINDOW + 50){
                telemetry.addData("move: ", "negative");
                subsystemManager.drive.move(-0.3, 0, 0);
            } else if (xCoord  != -1){
                pickupable = true;
            }

            drsd = subsystemManager.webcam.bestDetectionCoordsAngle();
            pickupable = pickupable || drsd.pickupable;
            xCoord = drsd.getX();
            yCoord = drsd.getY();
            telemetry.addData("pickupable? ", pickupable);
            telemetry.addData("y coord: ", yCoord);
            telemetry.addData("x coord: ", xCoord);
            telemetry.update();

        }

        subsystemManager.drive.move(0, 0, 0);
        while (Math.abs(subsystemManager.horizontalSlides.getEncoder() - encoder) > 50){
        }

        encoder = max(encoder - slidesAdvanceForPickUp * INTOENCODER, -subsystemManager.horizontalSlides.maxForward);
        subsystemManager.horizontalSlides.slideToPosition((int) encoder);



        if (drsd.getY() != -1) {
            thetas.add(drsd.getTheta());
        }


        if (thetas.isEmpty()) {
            return;
        }


        // WARNING DO NOT TRY TO UNDERSTAND THIS, IT MAKES NO SENSE BUT IT WORKS
        ogAngle = thetas.get(thetas.size() / 2);
        angle = ogAngle % 180;

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
        telemetry.update();
        subsystemManager.bottomClaw.rotate(rotationTheta);
    }

    public void reset(){
        wristRotated = false;
        slidesExtending = false;
        objectDetected = false;
        objectInFrame = false;
        slidesInPosition = false;
        pictureTaken = false;
        readyForPickup = false;
        timeReset = false;

    }
}