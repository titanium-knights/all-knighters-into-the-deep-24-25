package org.firstinspires.ftc.teamcode.pedroAuton.config;

import com.acmerobotics.dashboard.config.Config;
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
@Config
public class LeftOneSpecimenParkConfig implements IAutonConfig {

    // ===== Pose Definitions (angles in radians) =====

    // Starting pose of the robot.
    //public static  Pose START_POSE = new Pose(9, 82, Math.toRadians(0)); // changed
//    public static  Pose START_POSE = new Pose(9, 82, Math.toRadians(0));
    public static  Pose START_POSE = new Pose(9,106, Math.toRadians(90));
    // Pose for scoring the preloaded specimen (drives the robot closer to the bar).
    public static  Pose SCORE_SPECIMEN_BAR_POSE = new Pose(39, 82, Math.toRadians(0));

    // Poses for the first retrieval motion.
    //public static  Pose RETRIEVE_SPECIMEN_POSE1 = new Pose(21.5, 126, Math.toRadians(155));
    //public static  Pose RETRIEVE_SPECIMEN_POSE1 = new Pose(22, 126, Math.toRadians(155));
    public static  Pose RETRIEVE_SPECIMEN_POSE1 = new Pose(14, 119, Math.toRadians(177)); // used 2 be 155deg
    //public static  Pose RETRIEVE_SPECIMEN_POSE1 = new Pose(21.5, 128, Math.toRadians(155));
    //public static  Pose RETRIEVE_SPECIMEN_POSE1 = new Pose(21.5, 127, Math.toRadians(155));
    //public static  Pose RETRIEVE_SPECIMEN_POSE1 = new Pose(21.5, 126.5, Math.toRadians(155));

    // Poses for the second retrieval motion.
    //public static  Pose RETRIEVE_SPECIMEN_POSE2 = new Pose(17.5, 128, Math.toRadians(180));
    //public static  Pose RETRIEVE_SPECIMEN_POSE2 = new Pose(17.4, 129, Math.toRadians(170));
    //public static  Pose RETRIEVE_SPECIMEN_POSE2 = new Pose(17.1, 129, Math.toRadians(170));
    //public static  Pose RETRIEVE_SPECIMEN_POSE2 = new Pose(19, 129, Math.toRadians(180));
    public static  Pose RETRIEVE_SPECIMEN_POSE2 = new Pose(19, 124, Math.toRadians(180));

    // Poses for the third retrieval motion.
    public static  Pose RETRIEVE_SPECIMEN_POSE3 = new Pose(43.3, 120.5, Math.toRadians(265));
    //public static  Pose RETRIEVE_SPECIMEN_POSE3 = new Pose(45, 117, Math.toRadians(265));

    // Poses for scoring additional specimen.
    public static  Pose SCORE_BUCKET_POSE = new Pose(17, 125.5, Math.toRadians(130));
    public static  Pose GET_OUT_OF_SCORE_BUCKET_POSE = new Pose(19, 123, Math.toRadians(130));

    // Parking Pose
    public static  Pose PARK_POSE1 = new Pose(60, 110, Math.toRadians(0));
    public static  Pose PARK_POSE2 = new Pose(70, 94, Math.toRadians(270));

    // ===== Timing Parameters (in seconds) =====

    public static  double SCORING_INITIAL_WAIT_SECONDS = 0.5; //3.0
    public static  double SCORING_SLIDES_WAIT_SECONDS = 0.0; //2.0

    // Sleep time (in milliseconds) between segments.
    public static  int SEGMENT_SLEEP_TIME_MS = 50;
    public static  double WAIT_PLS = 0.3; // in seconds

    public static  int GRAB_SPECIMEN_WAIT_MS = 250;

    /**
     * ROUTINE defines the full autonomous routine as an ordered list of step descriptors.
     * (Action codes include, for example, "CLOSE_CLAW", "OPEN_CLAW", "SLIDE_MEDIUM", etc.)
     */
    public static  List<AutonStepDescriptor> ROUTINE = new ArrayList<>(Arrays.asList(
            // --- Scoring the preloaded specimen ---
//            new AutonStepDescriptor("MEDIUM_CLOSED"),
//            new AutonStepDescriptor(START_POSE, SCORE_SPECIMEN_BAR_POSE),
//            new AutonStepDescriptor(SCORING_INITIAL_WAIT_SECONDS),
//            new AutonStepDescriptor("BOTTOM_OPEN"),                            // Open claw to release specimen.

            // --- Transition ---
            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMOPEN_TOPCLOSED"),
            new AutonStepDescriptor(START_POSE, RETRIEVE_SPECIMEN_POSE1), // Drive to pickup alignment.
            new AutonStepDescriptor((double) SEGMENT_SLEEP_TIME_MS / 1000.0), // Short pause.
            new AutonStepDescriptor("BUCKET_UP_PRE_DUNK"),
            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE1, SCORE_BUCKET_POSE),
            new AutonStepDescriptor("BUCKET_UP_DUNKING"),
            new AutonStepDescriptor(WAIT_PLS),
            new AutonStepDescriptor("BUCKET_UP_POST_DUNK"),
            new AutonStepDescriptor(WAIT_PLS),
            new AutonStepDescriptor("BUCKET_UP_PRE_DUNK"), // get out of the way
            new AutonStepDescriptor(SCORE_BUCKET_POSE, GET_OUT_OF_SCORE_BUCKET_POSE),
            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPOPEN"),
            new AutonStepDescriptor(GET_OUT_OF_SCORE_BUCKET_POSE, RETRIEVE_SPECIMEN_POSE2)

            // --- Score Motion 1 ---
//            new AutonStepDescriptor("HORI_OUT_OPEN"),
//            new AutonStepDescriptor("HORI_OUT_CLOSED"),
//            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPOPEN"),
//            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPCLOSED"),
//            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMOPEN_TOPCLOSED"),
//            new AutonStepDescriptor(WAIT_PLS),
//            new AutonStepDescriptor("BUCKET_UP_PRE_DUNK"),
//            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE1, SCORE_BUCKET_POSE),
//            new AutonStepDescriptor("BUCKET_UP_DUNKING"),
//            new AutonStepDescriptor(WAIT_PLS),
//            new AutonStepDescriptor("BUCKET_UP_POST_DUNK"),
//            new AutonStepDescriptor(WAIT_PLS),
//            new AutonStepDescriptor("BUCKET_UP_PRE_DUNK"), // get out of the way
//            new AutonStepDescriptor(SCORE_BUCKET_POSE, GET_OUT_OF_SCORE_BUCKET_POSE),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPOPEN"),
//            new AutonStepDescriptor(GET_OUT_OF_SCORE_BUCKET_POSE, RETRIEVE_SPECIMEN_POSE2),
//
//            // --- Score Motion 2 ---
//            new AutonStepDescriptor("HORI_MIDOUT_OPEN"),
//            new AutonStepDescriptor("HORI_MIDOUT_CLOSED"),
//            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPOPEN"),
//            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPCLOSED"),
//            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMOPEN_TOPCLOSED"),
//            new AutonStepDescriptor(WAIT_PLS),
//            new AutonStepDescriptor("BUCKET_UP_PRE_DUNK"),
//            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE1, SCORE_BUCKET_POSE),
//            new AutonStepDescriptor("BUCKET_UP_DUNKING"),
//            new AutonStepDescriptor(WAIT_PLS),
//            new AutonStepDescriptor("BUCKET_UP_POST_DUNK"),
//            new AutonStepDescriptor(WAIT_PLS),
//            new AutonStepDescriptor("BUCKET_UP_PRE_DUNK"), // get out of the way
//            new AutonStepDescriptor(SCORE_BUCKET_POSE, GET_OUT_OF_SCORE_BUCKET_POSE),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPOPEN"),
//            new AutonStepDescriptor(GET_OUT_OF_SCORE_BUCKET_POSE, RETRIEVE_SPECIMEN_POSE3),
//
//            // --- Score Motion 3 ---
//            new AutonStepDescriptor("HORI_OUT_OPEN_TWIST"),
//            new AutonStepDescriptor("HORI_OUT_CLOSED_TWIST"),
//            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPOPEN"),
//            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPCLOSED"),
//            new AutonStepDescriptor((double) GRAB_SPECIMEN_WAIT_MS / 1000.0),
//            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMOPEN_TOPCLOSED"),
//            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE3, RETRIEVE_SPECIMEN_POSE1),
//            new AutonStepDescriptor("BUCKET_UP_PRE_DUNK"),
//            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE1, SCORE_BUCKET_POSE),
//            new AutonStepDescriptor("BUCKET_UP_DUNKING"),
//            new AutonStepDescriptor(WAIT_PLS),
//            new AutonStepDescriptor("BUCKET_UP_POST_DUNK"),
//            new AutonStepDescriptor(WAIT_PLS),
//            new AutonStepDescriptor("BUCKET_UP_PRE_DUNK"), // get out of the way
//            new AutonStepDescriptor(SCORE_BUCKET_POSE, RETRIEVE_SPECIMEN_POSE1),
//            new AutonStepDescriptor(
//                    new ArrayList<AutonStepDescriptor>(Arrays.asList(
//                            new AutonStepDescriptor(RETRIEVE_SPECIMEN_POSE1, PARK_POSE1),
//                            new AutonStepDescriptor("HORI_TRANSFER_BOTTOMCLOSED_TOPOPEN", SCORING_SLIDES_WAIT_SECONDS)
//                    ))
//            ),
//
//            // Park ("Hang")
//            new AutonStepDescriptor(
//                    new ArrayList<AutonStepDescriptor>(Arrays.asList(
//                            new AutonStepDescriptor(PARK_POSE1, PARK_POSE2),
//                            new AutonStepDescriptor("LOW_HANG", SCORING_SLIDES_WAIT_SECONDS)
//                    ))
//            )
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