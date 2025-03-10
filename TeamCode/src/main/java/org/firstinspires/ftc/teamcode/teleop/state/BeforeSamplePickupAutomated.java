package org.firstinspires.ftc.teamcode.teleop.state;

import static org.firstinspires.ftc.teamcode.teleop.Teleop.SLOW_MODE_MULTIPLIER;

import static java.lang.Double.min;
import static java.lang.Math.max;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.teleop.Teleop;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BeforeSamplePickupAutomated extends TeleopState {
    HardwareMap hmap;
    Telemetry telemetry;
    public double ogAngle, angle, rotationAngle, rotationTheta;
    public static final int WINDOW = 160; // max range is 320
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
            Thread.sleep(2000);
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
        encoder = 0;
        telemetry.addLine("y coordinate: " + yCoord);
        telemetry.addLine("horizontal slides: " + Math.abs(subsystemManager.horizontalSlides.getEncoder()));
        telemetry.addLine("condition true?" + (yCoord < 360 && Math.abs(subsystemManager.horizontalSlides.getEncoder()) <= subsystemManager.horizontalSlides.maxForward));
        telemetry.update();

        ArrayList<Double> thetas = new ArrayList<>();

        while ((Math.abs(xCoord - 320) >= WINDOW || yCoord < 240) && Math.abs(subsystemManager.horizontalSlides.getEncoder()) <= subsystemManager.horizontalSlides.maxForward) {
            telemetry.addLine("y coordinate: " + yCoord);
            telemetry.addLine("horizontal slides: " + Math.abs(subsystemManager.horizontalSlides.getEncoder()));
            telemetry.addLine("horizontal slides power: " + subsystemManager.horizontalSlides.getPower());
            telemetry.update();
            subsystemManager.horizontalSlides.manualForward(0.35);

            subsystemManager.drive.move(gamepad2.left_stick_x * SLOW_MODE_MULTIPLIER, gamepad2.left_stick_y * SLOW_MODE_MULTIPLIER, gamepad2.right_stick_x * SLOW_MODE_MULTIPLIER);

            if (Math.abs(subsystemManager.horizontalSlides.getEncoder()) >= 40) { // change this
                telemetry.addLine("we got here!");
                telemetry.update();
                drsd = subsystemManager.webcam.bestDetectionCoordsAngle();
                xCoord = drsd.getX();
                yCoord = drsd.getY();
                encoder = subsystemManager.horizontalSlides.getEncoder();
                encoder = max(encoder - 225, -subsystemManager.horizontalSlides.maxForward);
            }

            if (yCoord != -1 && Math.abs(xCoord - 320) < WINDOW) {
                thetas.add(drsd.getTheta());
            }
        }

        thetas = (ArrayList<Double>)thetas.stream().filter(d -> d!=180.0).collect(Collectors.toList());

        telemetry.addLine("out of the loop!");
        subsystemManager.horizontalSlides.stop();
        subsystemManager.horizontalSlides.slideToPosition((int) encoder);

        if (thetas.isEmpty()) {
            return;
        }

        ogAngle = thetas.get(thetas.size() / 2);
        angle = ogAngle % 180;
        if (angle < 0) {
            angle += 180;
        }
        angle = 180 - angle;
        telemetry.addData("angle: ", angle);


        rotationAngle = (angle + 90) % 180;
        telemetry.addData("rotation angle: ", rotationAngle);

        rotationTheta = ((rotationAngle * Math.PI) / 180) + Math.PI;
        if (rotationTheta > 2 * Math.PI) {
            rotationTheta -= 2 * Math.PI;
        }
        telemetry.addData("rotationTheta: ", rotationTheta);
        telemetry.update();
        subsystemManager.bottomClaw.rotate(rotationTheta);
    }
}