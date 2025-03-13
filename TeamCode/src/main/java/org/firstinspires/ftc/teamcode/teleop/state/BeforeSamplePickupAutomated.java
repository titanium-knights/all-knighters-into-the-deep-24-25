package org.firstinspires.ftc.teamcode.teleop.state;

import static org.firstinspires.ftc.teamcode.teleop.Teleop.SLOW_MODE_MULTIPLIER;

import static java.lang.Double.min;
import static java.lang.Math.max;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.teleop.Teleop;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BeforeSamplePickupAutomated extends TeleopState {
    HardwareMap hmap;
    Telemetry telemetry;
    public double ogAngle, angle, rotationAngle, rotationTheta;

    public static final int WINDOW = 160; // max range is 320
    public boolean pickupable;
    public static double slideSpeed = 0.7;
    public static int slideDelay = 1500;

    public static double clawDistanceAdjust = 0.5;

//    public Point[] points;
    public BeforeSamplePickupAutomated(SubsystemManager subsystemManager, HardwareMap hmap, Telemetry telemetry) {
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

        try {
            Thread.sleep(slideDelay);
        } catch (Exception e) {
            return;
        }

        extendToPickupPosition(gamepad1, gamepad2);
    }

    // It is done. My legacy.
    // Just when I thought I was out, they pull me back in.
    public void extendToPickupPosition(Gamepad gamepad1, Gamepad gamepad2) {
        double xCoord, yCoord, encoder;
        ConfidenceOrientationVectorPipeline.DetectionResultScaledData drsd = subsystemManager.webcam.bestDetectionCoordsAngle();
        yCoord = -1;
        xCoord = 320;
        pickupable = false;
        encoder = 0;

        telemetry.addLine("y coordinate: " + yCoord);
        telemetry.addLine("horizontal slides: " + Math.abs(subsystemManager.horizontalSlides.getEncoder()));
        telemetry.addLine("condition true?" + (Math.abs(subsystemManager.horizontalSlides.getEncoder()) <= subsystemManager.horizontalSlides.maxForward));
        telemetry.update();

        ArrayList<Double> thetas = new ArrayList<>();

        while (yCoord == -1 && Math.abs(subsystemManager.horizontalSlides.getEncoder()) <= subsystemManager.horizontalSlides.maxForward) {
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
                encoder = max(encoder - clawDistanceAdjust/537*120/25.4, -subsystemManager.horizontalSlides.maxForward);
            }

            if (pickupable) {
                thetas.add(drsd.getTheta());
            }
        }

//        points=drsd.points;

        telemetry.addLine("out of the loop!");
        subsystemManager.horizontalSlides.stop();
        subsystemManager.horizontalSlides.slideToPosition((int) encoder);

        while (!pickupable) {
            if (xCoord < (double) (2 * WINDOW) /3){
                subsystemManager.drive.move(-0.2, 0, 0);
            } else if (xCoord > 2 * (double) (2 * WINDOW) /3){
                subsystemManager.drive.move(0.2, 0, 0);
            }

            drsd = subsystemManager.webcam.bestDetectionCoordsAngle();
            pickupable = drsd.pickupable;
            if (drsd.getY() != -1) {
                thetas.add(drsd.getTheta());
            }
        }

        FtcDashboard.getInstance().stopCameraStream();

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
}