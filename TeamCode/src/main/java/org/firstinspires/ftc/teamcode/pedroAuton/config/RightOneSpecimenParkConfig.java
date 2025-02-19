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
    public static final Pose RETRIEVE_SPECIMEN_POSE3_2 = new Pose(60, 9, Math.toRadians(0));
    public static final Pose ENDING_POINT3 = new Pose(20, 9.9, Math.toRadians(0));

    // ===== Timing Parameters (in seconds) =====

    public static final double SCORING_INITIAL_WAIT_SECONDS = 0.5; //3.0
    public static final double SCORING_SLIDES_WAIT_SECONDS = 0.5; //2.0
    public static final double SCORING_CLAW_WAIT_SECONDS = 3.0;
    public static final double SCORING_RETRACT_WAIT_SECONDS = 2.0;

    // Sleep time (in milliseconds) between segments.
    public static final int SEGMENT_SLEEP_TIME_MS = 500;


    // ===== Step Script Definitions =====

    /**
     * StepType enumerates the types of steps.
     */
    public enum StepType {
        PATH,      // A path between two poses.
        ACTION,    // A mechanism action.
        SLEEP,     // A pause (sleep).
        PARALLEL   // A composite step running multiple sub-steps concurrently.
    }

    /**
     * AutonStepDescriptor holds the configuration for one step.
     * - For PATH steps: provide start and end poses.
     * - For ACTION steps: provide an action code and optional timeout (in seconds).
     * - For SLEEP steps: provide a duration (in seconds).
     * - For PARALLEL steps: provide a list of sub-step descriptors.
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
        // For ACTION steps, a timeout after which the step is forced complete.
        public double timeout;  // seconds
        // For PARALLEL steps:
        public List<AutonStepDescriptor> subSteps;

        // Constructor for PATH steps.
        public AutonStepDescriptor(Pose startPose, Pose endPose) {
            this.type = StepType.PATH;
            this.startPose = startPose;
            this.endPose = endPose;
        }

        // Constructor for ACTION steps without timeout.
        public AutonStepDescriptor(String actionCode) {
            this.type = StepType.ACTION;
            this.actionCode = actionCode;
        }

        // Constructor for ACTION steps with timeout.
        public AutonStepDescriptor(String actionCode, double timeout) {
            this.type = StepType.ACTION;
            this.actionCode = actionCode;
            this.timeout = timeout;
        }

        // Constructor for SLEEP steps.
        public AutonStepDescriptor(double duration) {
            this.type = StepType.SLEEP;
            this.duration = duration;
        }

        // Constructor for PARALLEL steps.
        public AutonStepDescriptor(List<AutonStepDescriptor> subSteps) {
            this.type = StepType.PARALLEL;
            this.subSteps = subSteps;
        }
    }

    /**
     * ROUTINE defines the full autonomous routine as an ordered list of step descriptors.
     * (Action codes include, for example, "CLOSE_CLAW", "OPEN_CLAW", "SLIDE_MEDIUM", etc.)
     *
     * Note: Here we also show a PARALLEL step so that, for instance, the robot
     * can drive to a new position while concurrently adjusting its slides.
     */
    public static final List<AutonStepDescriptor> ROUTINE = new ArrayList<>(Arrays.asList(
            // --- Scoring the preloaded specimen ---
            new AutonStepDescriptor("SLIDE_MEDIUM", SCORING_SLIDES_WAIT_SECONDS), // Move slides to medium.
            new AutonStepDescriptor(START_POSE, BEFORE_SCORE_SPECIMEN_POSE),  // Drive toward the bar.
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),
            new AutonStepDescriptor(BEFORE_SCORE_SPECIMEN_POSE, SCORE_SPECIMEN_BAR_POSE),  // Drive toward the bar.
            new AutonStepDescriptor("CLOSE_CLAW"),                         // Ensure claw is closed.
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),           // Wait for initial positioning.
            new AutonStepDescriptor("SLIDE_MEDIUM_SCORE", SCORING_SLIDES_WAIT_SECONDS),          // Adjust slides.
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),           // Wait for initial positioning.
            new AutonStepDescriptor("OPEN_CLAW"),                            // Open claw to release specimen.
            new AutonStepDescriptor("SLIDE_BOTTOM", SCORING_RETRACT_WAIT_SECONDS), // Retract slides.
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