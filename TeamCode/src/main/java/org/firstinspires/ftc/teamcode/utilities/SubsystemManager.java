package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline.DetectionResultScaledData;

/**
 * This class is used to encapsulate all utility classes for subsystems.
 * Any OpMode (teleop or auton) can initialize this class and access any utility class
 * The instance of this object should be passed to any TeleopState to provide access to utility classes
 * This prevents the need to pass references to several utility classes to each TeleopState
 */
public class SubsystemManager {

    // store hardware util classes as publicly accessible fields
    public SimpleMecanumDrive drive;
    public Arm arm;
    public BottomClaw bottomClaw;
    public TopClaw topClaw;
    public Slides slides;
    public HorizontalSlides horizontalSlides;
    public Webcam webcam;
    public Telemetry telemetry;
    public double yCoord, angle, rotationTheta;

    public SubsystemManager(HardwareMap hmap, Telemetry telemetry) {
        // add util class initializations here
        drive = new SimpleMecanumDrive(hmap);
        arm = new Arm(hmap);
        bottomClaw = new BottomClaw(hmap);
        horizontalSlides = new HorizontalSlides(hmap);
        topClaw = new TopClaw(hmap);
        slides = new Slides(hmap);
//        webcam = new Webcam(hmap, telemetry);
        this.telemetry = telemetry;
    }

    public void extendToPickupPosition() {
        DetectionResultScaledData drsd = webcam.bestDetectionCoordsAngle();
        yCoord = -1;
        telemetry.addLine("y coordinate: " + yCoord);
        telemetry.addLine("horizontal slides: " + Math.abs(horizontalSlides.getEncoder()));

        while (yCoord < 120 && Math.abs(horizontalSlides.getEncoder()) <= horizontalSlides.maxForward - 20) {
            telemetry.addLine("y coordinate: " + yCoord);
            telemetry.addLine("horizontal slides: " + Math.abs(horizontalSlides.getEncoder()));
            telemetry.update();
            horizontalSlides.manualBack(1.0);

            if (Math.abs(horizontalSlides.getEncoder()) >= 40) { // change this
                drsd = webcam.bestDetectionCoordsAngle();
                yCoord = drsd.getY();
            }
        }
        horizontalSlides.stop();
        yCoord = drsd.getY();
        angle = drsd.getTheta() % 360;
        if (angle > 180) {
            angle -= 180;
        }
        rotationTheta = (angle * Math.PI) / 180;
        bottomClaw.rotate(rotationTheta);
    }
}