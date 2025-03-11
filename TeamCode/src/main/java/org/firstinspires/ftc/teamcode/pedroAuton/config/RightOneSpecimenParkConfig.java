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
    public static final Pose START_POSE = new Pose(9, 62, Math.toRadians(0));
    // Pose for scoring the preloaded specimen (drives the robot closer to the bar).
    public static final Pose SCORE_SPECIMEN_BAR_POSE = new Pose(39, 62, Math.toRadians(0));
    public static final Pose SCORE_SPECIMEN_BAR_POSE_1 = new Pose(36.5, 70, Math.toRadians(2));
    public static final Pose SCORE_SPECIMEN_BAR_POSE_2 = new Pose(36.5, 76, Math.toRadians(2));
    public static final Pose SCORE_SPECIMEN_BAR_POSE_3 = new Pose(37, 80, Math.toRadians(2));

    // Pose for aligning before specimen retrieval.
    public static final Pose ALIGN_TO_PREPARE_FOR_RETRIEVAL = new Pose(30, 36, Math.toRadians(0));

    // Poses for the first retrieval motion.
    public static final Pose RETRIEVE_SPECIMEN_POSE1_1 = new Pose(60, 36, Math.toRadians(40));
    public static final Pose ENDING_POINT1 = new Pose(60, 27, Math.toRadians(120));

    // Poses for the second retrieval motion.
    public static final Pose RETRIEVE_SPECIMEN_POSE2_1 = new Pose(60, 27, Math.toRadians(45));
    public static final Pose ENDING_POINT2 = new Pose(60, 19, Math.toRadians(120));

    // Poses for the third retrieval motion.
    public static final Pose RETRIEVE_SPECIMEN_POSE3_1 = new Pose(60, 20, Math.toRadians(40));
    public static final Pose ENDING_POINT3 = new Pose(60, 12, Math.toRadians(120));

    // Poses for scoring additional specimen.
    public static final Pose GET_SPECIMEN_POSE_1 = new Pose(24.4, 23.8, Math.toRadians(8));
    public static final Pose GET_SPECIMEN_POSE_2 = new Pose(26.5, 27.6, Math.toRadians(8));
    public static final Pose GET_SPECIMEN_POSE_3 = new Pose(26.6, 27.6, Math.toRadians(7));
    public static final Pose ALIGN_TO_SCORE_POSE = new Pose(25, 65, Math.toRadians(7));

    // Parking Pose
    public static final Pose PARK_POSE = new Pose(13, 27, Math.toRadians(0));

    // ===== Timing Parameters (in seconds) =====

    public static final double SCORING_INITIAL_WAIT_SECONDS = 0.5; //3.0

    // Sleep time (in milliseconds) between segments.
    public static final int SEGMENT_SLEEP_TIME_MS = 50;

    public static final int GRAB_SPECIMEN_WAIT_MS = 500;
    public static final double WAIT_PLS = 0.3; // in seconds

    /**
     * ROUTINE defines the full autonomous routine as an ordered list of step descriptors.
     * (Action codes include, for example, "CLOSE_CLAW", "OPEN_CLAW", "SLIDE_MEDIUM", etc.)
     */
    public static final List<AutonStepDescriptor> ROUTINE = new ArrayList<>(Arrays.asList(
            // --- Scoring the preloaded specimen ---
            new AutonStepDescriptor("MEDIUM_CLOSED"),
            new AutonStepDescriptor(START_POSE, SCORE_SPECIMEN_BAR_POSE),
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),
            new AutonStepDescriptor("BOTTOM_OPEN"),
            new AutonStepDescriptor(SCORE_SPECIMEN_BAR_POSE, ALIGN_TO_PREPARE_FOR_RETRIEVAL), // Drive to pickup alignment.

            // --- Retrieval Motion 1 ---
            new AutonStepDescriptor("SWIPEUP"),
            new AutonStepDescriptor(ALIGN_TO_PREPARE_FOR_RETRIEVAL, RETRIEVE_SPECIMEN_POSE1_1),
            new AutonStepDescriptor("SWIPEDOWN"),
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE1_1, ENDING_POINT1),
            new AutonStepDescriptor("SWIPEUP"),


            // --- Retrieval Motion 2 ---
            new AutonStepDescriptor(ENDING_POINT1, RETRIEVE_SPECIMEN_POSE2_1),
            new AutonStepDescriptor("SWIPEDOWN"),
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE2_1, ENDING_POINT2),
            new AutonStepDescriptor("SWIPEUP"),


            // --- Retrieval Motion 3 ---
            new AutonStepDescriptor(ENDING_POINT2, RETRIEVE_SPECIMEN_POSE3_1),
            new AutonStepDescriptor("SWIPEDOWN"),
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE3_1, ENDING_POINT3),
            new AutonStepDescriptor("SWIPEFOLD"),


            // 2ND SPECIMEN
            // --- Align to get specimen ---
            // todo: change to ending point 3 if you figure out retrieval motion 3
            new AutonStepDescriptor(ENDING_POINT2, GET_SPECIMEN_POSE_1),        // Move from the sample pushed previously // Turn to have top claw face wall

            // --- Get the specimen from human player ---
            new AutonStepDescriptor("HORI_SPECIN_OPEN"),
            new AutonStepDescriptor(WAIT_PLS),
            new AutonStepDescriptor("HORI_SPECOUT_OPEN"),
            new AutonStepDescriptor(WAIT_PLS),
            new AutonStepDescriptor("HORI_SPECOUT_CLOSED"),
            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPOPEN"),
            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPCLOSED"),
            new AutonStepDescriptor(WAIT_PLS),
            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMOPEN_TOPCLOSED"),
            new AutonStepDescriptor(WAIT_PLS),
            new AutonStepDescriptor("MEDIUM_CLOSED"),

            // --- Score specimen position ---
            new AutonStepDescriptor(GET_SPECIMEN_POSE_1, ALIGN_TO_SCORE_POSE), // Move closer to the submersible for scoring
            new AutonStepDescriptor(ALIGN_TO_SCORE_POSE, SCORE_SPECIMEN_BAR_POSE_1),
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),              // Wait for initial positioning.
            new AutonStepDescriptor("BOTTOM_OPEN"),

            // --- Re-align to get specimen again ---
            new AutonStepDescriptor(SCORE_SPECIMEN_BAR_POSE_1, ALIGN_TO_SCORE_POSE),  // Drive to pickup alignment.
            new AutonStepDescriptor(ALIGN_TO_SCORE_POSE, GET_SPECIMEN_POSE_2),
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),

            // 3RD SPECIMEN
            // --- Get the specimen from human player ---
            new AutonStepDescriptor("HORI_SPECIN_OPEN"),
            new AutonStepDescriptor(WAIT_PLS),
            new AutonStepDescriptor("HORI_SPECOUT_OPEN"),
            new AutonStepDescriptor(WAIT_PLS),
            new AutonStepDescriptor("HORI_SPECOUT_CLOSED"),
            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPOPEN"),
            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPCLOSED"),
            new AutonStepDescriptor(WAIT_PLS),
            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMOPEN_TOPCLOSED"),
            new AutonStepDescriptor(WAIT_PLS),
            new AutonStepDescriptor("MEDIUM_CLOSED"),

            // --- Score specimen position ---
            new AutonStepDescriptor(GET_SPECIMEN_POSE_2, ALIGN_TO_SCORE_POSE), // Move closer to the submersible for scoring
            new AutonStepDescriptor(ALIGN_TO_SCORE_POSE, SCORE_SPECIMEN_BAR_POSE_2),
            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),              // Wait for initial positioning.
            new AutonStepDescriptor("BOTTOM_OPEN"),

            // --- Re-align to get specimen again ---
            new AutonStepDescriptor(SCORE_SPECIMEN_BAR_POSE_2, ALIGN_TO_SCORE_POSE),  // Drive to pickup alignment.
            //new AutonStepDescriptor(ALIGN_TO_SCORE_POSE, GET_SPECIMEN_POSE_3),
            //new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0),

            // 4TH SPECIMEN
            // --- Get the specimen from human player ---
//            new AutonStepDescriptor("HORI_SPECIN_OPEN"),
//            new AutonStepDescriptor(WAIT_PLS),
//            new AutonStepDescriptor("HORI_SPECOUT_OPEN"),
//            new AutonStepDescriptor(WAIT_PLS),
//            new AutonStepDescriptor("HORI_SPECOUT_CLOSED"),
//            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPOPEN"),
//            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPCLOSED"),
//            new AutonStepDescriptor(WAIT_PLS),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMOPEN_TOPCLOSED"),
//            new AutonStepDescriptor(WAIT_PLS),
//            new AutonStepDescriptor("MEDIUM_CLOSED"),
//
//            // --- Score specimen position ---
//            new AutonStepDescriptor(GET_SPECIMEN_POSE_3, ALIGN_TO_SCORE_POSE), // Move closer to the submersible for scoring
//            new AutonStepDescriptor(ALIGN_TO_SCORE_POSE, SCORE_SPECIMEN_BAR_POSE_3),
//            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),              // Wait for initial positioning.
//            new AutonStepDescriptor("BOTTOM_OPEN"),

            // --- Park ---
            // todo: change to score specimen pose 3 if we get 4th specimen working
            new AutonStepDescriptor(ALIGN_TO_SCORE_POSE, PARK_POSE),  // Drive to pickup alignment.
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