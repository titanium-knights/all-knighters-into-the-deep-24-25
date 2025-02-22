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

    public SubsystemManager(HardwareMap hmap) {
        // add util class initializations here
        drive = new SimpleMecanumDrive(hmap);
        arm = new Arm(hmap);
        bottomClaw = new BottomClaw(hmap);
        horizontalSlides = new HorizontalSlides(hmap);
        topClaw = new TopClaw(hmap);
        slides = new Slides(hmap);
        webcam = new Webcam(hmap);
    }
}