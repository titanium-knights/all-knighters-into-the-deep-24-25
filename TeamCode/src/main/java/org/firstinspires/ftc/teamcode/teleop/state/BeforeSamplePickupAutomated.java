package org.firstinspires.ftc.teamcode.teleop.state;

import static org.firstinspires.ftc.teamcode.teleop.Teleop.SLOW_MODE_MULTIPLIER;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.teleop.Teleop;

import org.firstinspires.ftc.teamcode.teleop.TeleopState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;
import org.firstinspires.ftc.teamcode.utilities.Webcam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BeforeSamplePickupAutomated extends TeleopState {
    HardwareMap hmap;
    Telemetry telemetry;
    public double ogAngle, angle, rotationAngle, rotationTheta;
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

        subsystemManager.arm.toReceivingPos();

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            return;
        }

        extendToPickupPosition(gamepad1, gamepad2);
    }

    // It is done. My legacy.
    public void extendToPickupPosition(Gamepad gamepad1, Gamepad gamepad2) {
        double yCoord;
        ConfidenceOrientationVectorPipeline.DetectionResultScaledData drsd = subsystemManager.webcam.bestDetectionCoordsAngle();
        yCoord = -1;
        telemetry.addLine("y coordinate: " + yCoord);
        telemetry.addLine("horizontal slides: " + Math.abs(subsystemManager.horizontalSlides.getEncoder()));
        telemetry.addLine("condition true?" + (yCoord < 360 && Math.abs(subsystemManager.horizontalSlides.getEncoder()) <= subsystemManager.horizontalSlides.maxForward - 20));
        telemetry.update();

        ArrayList<Double> thetas = new ArrayList<>();

        while (yCoord < 240 && Math.abs(subsystemManager.horizontalSlides.getEncoder()) <= subsystemManager.horizontalSlides.maxForward - 20) {
            telemetry.addLine("y coordinate: " + yCoord);
            telemetry.addLine("horizontal slides: " + Math.abs(subsystemManager.horizontalSlides.getEncoder()));
            telemetry.addLine("horizontal slides power: " + subsystemManager.horizontalSlides.getPower());
            telemetry.update();
            subsystemManager.horizontalSlides.manualBack(0.5);

            subsystemManager.drive.move(gamepad2.left_stick_x * SLOW_MODE_MULTIPLIER, gamepad2.left_stick_y * SLOW_MODE_MULTIPLIER, gamepad2.right_stick_x * SLOW_MODE_MULTIPLIER);

            if (Math.abs(subsystemManager.horizontalSlides.getEncoder()) >= 40) { // change this
                telemetry.addLine("we got here!");
                telemetry.update();
                drsd = subsystemManager.webcam.bestDetectionCoordsAngle();
                yCoord = drsd.getY();
            }

            if (yCoord != -1) {
                thetas.add(drsd.getTheta());
            }
        }
        telemetry.addLine("out of the loop!");
        subsystemManager.horizontalSlides.stop();
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
