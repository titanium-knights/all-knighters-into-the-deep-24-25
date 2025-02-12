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
    public Scissors scissors;
    public TopClaw topClaw;
    public Slides slides;
    private HorizontalSlides horizontalSlides;
    private Webcam webcam;

    public SubsystemManager(HardwareMap hmap, Telemetry telemetry) {
        // add util class initializations here
        drive = new SimpleMecanumDrive(hmap);
        arm = new Arm(hmap);
        bottomClaw = new BottomClaw(hmap);
        scissors = new Scissors(hmap);
        horizontalSlides = new HorizontalSlides(hmap);
        topClaw = new TopClaw(hmap);
        slides = new Slides(hmap);
        webcam = new Webcam(hmap, telemetry);
    }

    public extendToPickupPosition() {
        DetectionResultScaledData drsd = webcam.bestDetectionCoordsAngle();
        while (drsd.getY() < 240) {
            horizontalSlides.manualForward(0.7);
        }
    }
}