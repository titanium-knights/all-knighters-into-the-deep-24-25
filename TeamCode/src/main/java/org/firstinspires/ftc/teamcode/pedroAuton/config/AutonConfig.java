package org.firstinspires.ftc.teamcode.pedroAuton.config;

import com.pedropathing.localization.Pose;
import java.util.List;
import java.util.Arrays;

/**
 * AutonConfig centralizes all tuned parameters for the autonomous routine.
 * This includes field positions (poses), timing parameters, and a complete
 * “script” of autonomous steps (both motion and mechanism actions).
 *
 * By modifying only this file, the entire autonomous routine (including actions)
 * can be retuned without changing the execution logic.
 */
public class AutonConfig {
    // ===== Pose Definitions (angles in radians) =====

    // Starting pose of the robot.
    public static final Pose START_POSE = new Pose(10, 62, Math.toRadians(0));

    // Updated pose for scoring the preloaded specimen.
    // Previously the robot did not get close enough to the bar.
    public static final Pose SCORE_SPECIMEN_BAR_POSE = new Pose(45, 62, Math.toRadians(0));

    // Pose for aligning before starting specimen retrieval.
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
    public static final Pose RETRIEVE_SPECIMEN_POSE3_2 = new Pose(60, 13, Math.toRadians(0));
    public static final Pose ENDING_POINT3 = new Pose(20, 13, Math.toRadians(0));

    // ===== Timing Parameters =====

    // (All delays are in seconds unless otherwise noted.)
    public static final double SCORING_INITIAL_WAIT_SECONDS = 3.0; // Reduced to avoid overshoot/jitter.
    public static final double SCORING_SLIDES_WAIT_SECONDS = 1.5;
    public static final double SCORING_CLAW_WAIT_SECONDS = 1.0;
    public static final double SCORING_RETRACT_WAIT_SECONDS = 2.0;

    // Sleep time (in milliseconds) inserted between motion segments.
    public static final int SEGMENT_SLEEP_TIME_MS = 500;

    // ===== Step Script Definitions =====

    /**
     * StepType enumerates the types of autonomous steps.
     */
    public enum StepType {
        PATH,   // Follow a path between two poses.
        ACTION, // Execute a mechanism action.
        SLEEP   // Wait for a specified duration.
    }

    /**
     * AutonStepDescriptor holds the configuration data for one step.
     * For PATH steps, the start and end poses are provided.
     * For ACTION steps, an action code string is used.
     * For SLEEP steps, the wait duration (in seconds) is given.
     */
    public static class AutonStepDescriptor {
        public StepType type;
        // For PATH steps:
        public Pose startPose;
        public Pose endPose;
        // For ACTION steps:
        public String actionCode;
        // For SLEEP steps:
        public double duration; // seconds

        // Constructor for PATH steps.
        public AutonStepDescriptor(Pose startPose, Pose endPose) {
            this.type = StepType.PATH;
            this.startPose = startPose;
            this.endPose = endPose;
        }

        // Constructor for ACTION steps.
        public AutonStepDescriptor(String actionCode) {
            this.type = StepType.ACTION;
            this.actionCode = actionCode;
        }

        // Constructor for SLEEP steps.
        public AutonStepDescriptor(double duration) {
            this.type = StepType.SLEEP;
            this.duration = duration;
        }
    }

    /**
     * ROUTINE defines the full autonomous routine as an ordered list of step descriptors.
     * Action codes are defined as follows:
     * - "CLOSE_CLAW": Close the claw.
     * - "OPEN_CLAW": Open the claw.
     * - "SLIDE_MEDIUM": Move slides to a medium position.
     * - "SLIDE_MEDIUM_SCORE": Move slides to the scoring position.
     * - "SLIDE_BOTTOM": Retract slides.
     */
    public static final List<AutonStepDescriptor> ROUTINE = Arrays.asList(
            // --- Scoring the preloaded specimen ---
            new AutonStepDescriptor(START_POSE, SCORE_SPECIMEN_BAR_POSE),  // Drive toward the scoring bar.
            new AutonStepDescriptor("CLOSE_CLAW"),                         // Ensure claw is closed.
            new AutonStepDescriptor("SLIDE_MEDIUM"),                         // Move slides to medium position.
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),           // Wait for initial positioning.
            new AutonStepDescriptor("SLIDE_MEDIUM_SCORE"),                   // Move slides into scoring position.
            new AutonStepDescriptor("OPEN_CLAW"),                            // Open claw to release specimen.
            new AutonStepDescriptor("SLIDE_BOTTOM"),                         // Retract slides.

            // --- Transition to specimen retrieval ---
            new AutonStepDescriptor(SCORE_SPECIMEN_BAR_POSE, ALIGN_TO_PREPARE_FOR_RETRIEVAL), // Move to pickup alignment.
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),                 // Short pause.

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
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0)
    );
}