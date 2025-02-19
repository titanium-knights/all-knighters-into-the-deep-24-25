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
public class RightOneSpecimenParkConfig implements IAutonConfig {

    // ===== Pose Definitions (angles in radians) =====

    // Starting pose of the robot.
    public static final Pose START_POSE = new Pose(10, 62, Math.toRadians(0));

    public static final Pose BEFORE_SCORE_SPECIMEN_POSE = new Pose(35, 62, Math.toRadians(0));

    // Pose for scoring the preloaded specimen (drives the robot closer to the bar).
    public static final Pose SCORE_SPECIMEN_BAR_POSE = new Pose(41, 62, Math.toRadians(0));

    // Pose for aligning before specimen retrieval.
    public static final Pose ALIGN_TO_PREPARE_FOR_RETRIEVAL = new Pose(20, 36, Math.toRadians(0));

    // Poses for the first retrieval motion.
    public static final Pose RETRIEVE_SPECIMEN_POSE1_1 = new Pose(60, 36, Math.toRadians(0));
    public static final Pose RETRIEVE_SPECIMEN_POSE1_2 = new Pose(60, 27, Math.toRadians(0));
    public static final Pose ENDING_POINT1 = new Pose(20, 27, Math.toRadians(0));

    // Poses for the second retrieval motion.
    public static final Pose RETRIEVE_SPECIMEN_POSE2_1 = new Pose(60, 27, Math.toRadians(0));
    public static final Pose RETRIEVE_SPECIMEN_POSE2_2 = new Pose(60, 20, Math.toRadians(0));
    public static final Pose ENDING_POINT2 = new Pose(20, 20, Math.toRadians(0));

    // Poses for the third retrieval motion.
    public static final Pose RETRIEVE_SPECIMEN_POSE3_1 = new Pose(60, 20, Math.toRadians(0));
    public static final Pose RETRIEVE_SPECIMEN_POSE3_2 = new Pose(60, 9.2, Math.toRadians(0));
    public static final Pose ENDING_POINT3 = new Pose(20, 9.2, Math.toRadians(0));

    // Poses for scoring additional specimen.
    public static final Pose TURNING_POSE4_1 = new Pose(20, 36, Math.toRadians(0));
    public static final Pose TURNING_POSE4_2 = new Pose(20, 36, Math.toRadians(180));
    public static final Pose GET_SPECIMEN_POSE = new Pose(10, 36, Math.toRadians(180));

    // ===== Timing Parameters (in seconds) =====

    public static final double SCORING_INITIAL_WAIT_SECONDS = 0.5; //3.0
    public static final double SCORING_SLIDES_WAIT_SECONDS = 0.5; //2.0
    public static final double SCORING_CLAW_WAIT_SECONDS = 3.0;
    public static final double SCORING_RETRACT_WAIT_SECONDS = 2.0;

    // Sleep time (in milliseconds) between segments.
    public static final int SEGMENT_SLEEP_TIME_MS = 500;

    /**
     * ROUTINE defines the full autonomous routine as an ordered list of step descriptors.
     * (Action codes include, for example, "CLOSE_CLAW", "OPEN_CLAW", "SLIDE_MEDIUM", etc.)
     */
    public static final List<AutonStepDescriptor> ROUTINE = new ArrayList<>(Arrays.asList(
            // --- Scoring the preloaded specimen ---
            new AutonStepDescriptor("MEDIUM_CLOSED", SCORING_SLIDES_WAIT_SECONDS), // Move slides to medium.
            new AutonStepDescriptor(START_POSE, BEFORE_SCORE_SPECIMEN_POSE),  // Drive toward the bar.
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),
            new AutonStepDescriptor(BEFORE_SCORE_SPECIMEN_POSE, SCORE_SPECIMEN_BAR_POSE),  // Drive toward the bar.                        // Ensure claw is closed.
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),           // Wait for initial positioning.
            new AutonStepDescriptor("MEDIUM_SCORE_CLOSED", SCORING_SLIDES_WAIT_SECONDS),          // Adjust slides.
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),           // Wait for initial positioning.
            new AutonStepDescriptor("BOTTOM_OPEN"),                            // Open claw to release specimen.
            new AutonStepDescriptor(SCORE_SPECIMEN_BAR_POSE, ALIGN_TO_PREPARE_FOR_RETRIEVAL), // Drive to pickup alignment.

            // --- Transition ---
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0), // Short pause.

            // --- Retrieval Motion 1 ---
            new AutonStepDescriptor(ALIGN_TO_PREPARE_FOR_RETRIEVAL, RETRIEVE_SPECIMEN_POSE1_1),
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE1_1, RETRIEVE_SPECIMEN_POSE1_2),
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE1_2, ENDING_POINT1),
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),

            // --- Retrieval Motion 2 ---
            new AutonStepDescriptor(ENDING_POINT1, RETRIEVE_SPECIMEN_POSE2_1),
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE2_1, RETRIEVE_SPECIMEN_POSE2_2),
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE2_2, ENDING_POINT2),
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),

            // --- Retrieval Motion 3 ---
            new AutonStepDescriptor(ENDING_POINT2, RETRIEVE_SPECIMEN_POSE3_1),
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE3_1, RETRIEVE_SPECIMEN_POSE3_2),
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE3_2, ENDING_POINT3),
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),

            // --- Align to get specimen from wall ---
            new AutonStepDescriptor(ENDING_POINT3, TURNING_POSE4_1),        // Move from the sample pushed previously
            new AutonStepDescriptor(TURNING_POSE4_1, TURNING_POSE4_2),      // Turn to have top claw face wall

            // --- Get the specimen from wall ---
            new AutonStepDescriptor(TURNING_POSE4_2, GET_SPECIMEN_POSE),    // Move to be flush against wall
            new AutonStepDescriptor("CLOSE_CLAW"),                          // Close claw
            new AutonStepDescriptor("SLIDE_MEDIUM"),                    // Raise slides todo: change to maxwell's updated

            // --- Move to scoring specimen position ---
            new AutonStepDescriptor(GET_SPECIMEN_POSE, TURNING_POSE4_2),        // move back from wall
            new AutonStepDescriptor(TURNING_POSE4_2, TURNING_POSE4_1),          // Turn to have top claw face submersible
            new AutonStepDescriptor(TURNING_POSE4_1, SCORE_SPECIMEN_BAR_POSE),  // Move to the submersible

            // --- Score specimen ---
            new AutonStepDescriptor("CLOSE_CLAW"),                         // Ensure claw is closed.
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),           // Wait for initial positioning.
            new AutonStepDescriptor("SLIDE_MEDIUM_SCORE", SCORING_SLIDES_WAIT_SECONDS),          // Adjust slides.
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),           // Wait for initial positioning.
            new AutonStepDescriptor("OPEN_CLAW"),                            // Open claw to release specimen.
            new AutonStepDescriptor("SLIDE_BOTTOM", SCORING_RETRACT_WAIT_SECONDS), // Retract slides.

            // --- Re-align to get specimen again ---
            new AutonStepDescriptor(SCORE_SPECIMEN_BAR_POSE, TURNING_POSE4_1),  // Drive to pickup alignment.
            new AutonStepDescriptor(TURNING_POSE4_1, TURNING_POSE4_2),          // Turn to have top claw face wall
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0)
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