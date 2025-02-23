package org.firstinspires.ftc.teamcode.pedroAuton.config;

import com.pedropathing.localization.Pose;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * RightOneSpecimenParkConfig is the autonomous configuration for the RightOneSpecimenPark OpMode.
 * It centralizes tuned poses, timings, and the full “script” for the routine.
 *
 * (You can create additional configurations by writing another class that implements IAutonConfig.)
 */
public class LeftOneSpecimenParkConfig implements IAutonConfig {

    // ===== Pose Definitions (angles in radians) =====

    // Starting pose of the robot.
    public static final Pose START_POSE = new Pose(10, 82, Math.toRadians(0));

    // Pose for scoring the preloaded specimen (drives the robot closer to the bar).
    public static final Pose SCORE_SPECIMEN_BAR_POSE = new Pose(40.3, 82, Math.toRadians(0));

    // Poses for the first retrieval motion.
    public static final Pose RETRIEVE_SPECIMEN_POSE1 = new Pose(22, 128, Math.toRadians(155));

    // Poses for the second retrieval motion.
    public static final Pose RETRIEVE_SPECIMEN_POSE2 = new Pose(20, 128, Math.toRadians(180));

    // Poses for the third retrieval motion.
    public static final Pose RETRIEVE_SPECIMEN_POSE3 = new Pose(45, 120, Math.toRadians(270));

    // Poses for scoring additional specimen.
    public static final Pose SCORE_BUCKET_POSE = new Pose(15, 128, Math.toRadians(125));

    // Parking Pose
    public static final Pose PARK_POSE1 = new Pose(60, 110, Math.toRadians(0));
    public static final Pose PARK_POSE2 = new Pose(70, 95, Math.toRadians(270));

    // ===== Timing Parameters (in seconds) =====

    public static final double SCORING_INITIAL_WAIT_SECONDS = 0.5; //3.0
    public static final double SCORING_SLIDES_WAIT_SECONDS = 0.0; //2.0

    // Sleep time (in milliseconds) between segments.
    public static final int SEGMENT_SLEEP_TIME_MS = 50;
    public static final int WAIT_PLS = 2000;

    public static final int GRAB_SPECIMEN_WAIT_MS = 500;

    /**
     * ROUTINE defines the full autonomous routine as an ordered list of step descriptors.
     * (Action codes include, for example, "CLOSE_CLAW", "OPEN_CLAW", "SLIDE_MEDIUM", etc.)
     */
    public static final List<AutonStepDescriptor> ROUTINE = new ArrayList<>(Arrays.asList(
            // --- Scoring the preloaded specimen ---
            new AutonStepDescriptor(
                    new ArrayList<AutonStepDescriptor>(Arrays.asList(
                            new AutonStepDescriptor(START_POSE, SCORE_SPECIMEN_BAR_POSE),  // Drive toward the bar.                        // Ensure claw is closed.        // Wait for initial positioning.
                            new AutonStepDescriptor("MEDIUM_CLOSED", SCORING_SLIDES_WAIT_SECONDS)          // Adjust slides.
                    ))
            ),
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),
            new AutonStepDescriptor("MEDIUM_SCORE_CLOSED", SCORING_SLIDES_WAIT_SECONDS),
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),           // Wait for initial positioning.
            new AutonStepDescriptor("BOTTOM_OPEN"),                            // Open claw to release specimen.

            // --- Transition ---
            new AutonStepDescriptor(SCORE_SPECIMEN_BAR_POSE, RETRIEVE_SPECIMEN_POSE1), // Drive to pickup alignment.
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0), // Short pause.

            // --- Score Motion 1 ---
            new AutonStepDescriptor("HORI_OUT_OPEN"),
            new AutonStepDescriptor("HORI_OUT_CLOSED"),
            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
            new AutonStepDescriptor("HORI_TRANSFER_CLOSED"),
            new AutonStepDescriptor("HORI_TRANSFER_OPEN"),
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE1, SCORE_BUCKET_POSE),
            new AutonStepDescriptor("BUCKET_UP_PRE_DUNK"),
            new AutonStepDescriptor("BUCKET_UP_POST_DUNK"),
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),
            new AutonStepDescriptor(SCORE_BUCKET_POSE, RETRIEVE_SPECIMEN_POSE2),
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),
            new AutonStepDescriptor("HORI_TRANSFER_CLOSED"), // just to make sure we're folded in


            // --- Score Motion 2 ---
            new AutonStepDescriptor("HORI_OUT_OPEN"),
            new AutonStepDescriptor("HORI_OUT_CLOSED"),
            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
            new AutonStepDescriptor("HORI_TRANSFER_CLOSED"),
            new AutonStepDescriptor("HORI_TRANSFER_OPEN"),
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE1, SCORE_BUCKET_POSE),
            new AutonStepDescriptor("BUCKET_UP_PRE_DUNK"),
            new AutonStepDescriptor("BUCKET_UP_POST_DUNK"),
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),
            new AutonStepDescriptor(SCORE_BUCKET_POSE, RETRIEVE_SPECIMEN_POSE3),
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),
            new AutonStepDescriptor("HORI_TRANSFER_CLOSED"), // just to make sure we're folded in


            // --- Score Motion 3 ---
//            new AutonStepDescriptor("HORI_OUT_OPEN_TWIST"),
//            new AutonStepDescriptor("HORI_OUT_CLOSED_TWIST"),
//            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_CLOSED"),
//            new AutonStepDescriptor("HORI_TRANSFER_OPEN"),
//            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE1, SCORE_BUCKET_POSE),
//            new AutonStepDescriptor("BUCKET_UP_PRE_DUNK"),
//            new AutonStepDescriptor("BUCKET_UP_POST_DUNK"),
//            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),
//            new AutonStepDescriptor(SCORE_BUCKET_POSE, RETRIEVE_SPECIMEN_POSE3),
//            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_CLOSED"), // just to make sure we're folded in

            // Park ("Hang")
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE3, PARK_POSE1),
            new AutonStepDescriptor(PARK_POSE1, PARK_POSE2)
            //new AutonStepDescriptor("LOW_HANG")
    ));

    // ===== IAutonConfig Interface Methods =====

    @Override
    public Pose getStartPose() {
        return START_POSE;
    }

    @Override
    public List<AutonStepDescriptor> getRoutine() {
        return ROUTINE;
    }

    @Override
    public int getSegmentSleepTimeMs() {
        return SEGMENT_SLEEP_TIME_MS;
    }
}