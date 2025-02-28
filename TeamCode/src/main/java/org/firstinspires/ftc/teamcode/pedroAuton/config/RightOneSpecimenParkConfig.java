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

    // Sleep time (in milliseconds) between segments.
    public static final int TEMP_PAUSE = 50;

    public static final Pose START_POSE = new Pose(62, 10, 0);
    public static final Pose SCORE_SPECIMEN_BAR_POSE = new Pose(62, 39, 0);

    public static final Pose ALIGN_TO_PICKUP_1 = new Pose(40, 20, 40);
    public static final Pose ALIGN_TO_PICKUP_2 = new Pose(30, 20, 40);
    public static final Pose ALIGN_TO_PICKUP_3 = new Pose(20, 20, 40);

    public static final Pose PASS_TO_HUMAN_1 = new Pose(40, 20, 130);
    public static final Pose PASS_TO_HUMAN_2 = new Pose(30, 20, 130);
    public static final Pose PASS_TO_HUMAN_3 = new Pose(20, 20, 130);

    public static final int LONG_WAIT = 1;

    /**
     * ROUTINE defines the full autonomous routine as an ordered list of step descriptors.
     * (Action codes include, for example, "CLOSE_CLAW", "OPEN_CLAW", "SLIDE_MEDIUM", etc.)
     */
    public static final List<AutonStepDescriptor> ROUTINE = new ArrayList<>(Arrays.asList(
            // --- Scoring the preloaded specimen ---
            new AutonStepDescriptor(
                    new ArrayList<AutonStepDescriptor>(Arrays.asList(
                            new AutonStepDescriptor(START_POSE, SCORE_SPECIMEN_BAR_POSE),  // Drive toward the bar.                        // Ensure claw is closed.        // Wait for initial positioning.
                            new AutonStepDescriptor("PREPARED_TO_SCORE_SPECIMEN", TEMP_PAUSE)          // Adjust slides.
                    ))
            ),
            new AutonStepDescriptor(TEMP_PAUSE),
            new AutonStepDescriptor(
                    new ArrayList<AutonStepDescriptor>(Arrays.asList(
                            new AutonStepDescriptor(SCORE_SPECIMEN_BAR_POSE, ALIGN_TO_PICKUP_1),  // Drive toward the bar.                        // Ensure claw is closed.        // Wait for initial positioning.
                            new AutonStepDescriptor("PREPARED_TO_PICKUP_SPEICMEN")
                    ))
            ),
            new AutonStepDescriptor(TEMP_PAUSE),           // Wait for initial positioning.
            new AutonStepDescriptor("PICKUP_OUT_OPEN"),
            new AutonStepDescriptor("PICKUP_OUT_CLOSED"),
            new AutonStepDescriptor("PICKUP_REACH_IN"),
            new AutonStepDescriptor(ALIGN_TO_PICKUP_1, PASS_TO_HUMAN_1),
            new AutonStepDescriptor("PICKUP_REACH_OUT"),
            new AutonStepDescriptor("PICKUP_OUT_OPEN"),
            new AutonStepDescriptor(PASS_TO_HUMAN_1, ALIGN_TO_PICKUP_2),

            new AutonStepDescriptor(TEMP_PAUSE),           // Wait for initial positioning.
            new AutonStepDescriptor("PICKUP_OUT_OPEN"),
            new AutonStepDescriptor("PICKUP_OUT_CLOSED"),
            new AutonStepDescriptor("PICKUP_REACH_IN"),
            new AutonStepDescriptor(ALIGN_TO_PICKUP_2, PASS_TO_HUMAN_2),
            new AutonStepDescriptor("PICKUP_REACH_OUT"),
            new AutonStepDescriptor("PICKUP_OUT_OPEN"),
            new AutonStepDescriptor(PASS_TO_HUMAN_2, ALIGN_TO_PICKUP_2),


            new AutonStepDescriptor(TEMP_PAUSE),           // Wait for initial positioning.
            new AutonStepDescriptor("PICKUP_OUT_OPEN"),
            new AutonStepDescriptor("PICKUP_OUT_CLOSED"),
            new AutonStepDescriptor("PICKUP_REACH_IN"),
            new AutonStepDescriptor(ALIGN_TO_PICKUP_3, PASS_TO_HUMAN_3),
            new AutonStepDescriptor("PICKUP_REACH_OUT"),
            new AutonStepDescriptor("PICKUP_OUT_OPEN"),

            new AutonStepDescriptor(LONG_WAIT),
            new AutonStepDescriptor("PICKUP_OUT_CLOSED"),
            new AutonStepDescriptor("TRANSFER1"),
            new AutonStepDescriptor("TRANSFER2"),

            new AutonStepDescriptor(
                    new ArrayList<AutonStepDescriptor>(Arrays.asList(
                            new AutonStepDescriptor(PASS_TO_HUMAN_3, SCORE_SPECIMEN_BAR_POSE),
                            new AutonStepDescriptor("PREPARED_TO_SCORE_SPEICMEN")
                    ))
            ),

            new AutonStepDescriptor(
                    new ArrayList<AutonStepDescriptor>(Arrays.asList(
                            new AutonStepDescriptor(SCORE_SPECIMEN_BAR_POSE, PASS_TO_HUMAN_3),
                            new AutonStepDescriptor("PICKUP_OUT_OPEN")
                    ))
            ),

            new AutonStepDescriptor(LONG_WAIT),
            new AutonStepDescriptor("PICKUP_OUT_CLOSED"),
            new AutonStepDescriptor("TRANSFER1"),
            new AutonStepDescriptor("TRANSFER2"),

            new AutonStepDescriptor(
                    new ArrayList<AutonStepDescriptor>(Arrays.asList(
                            new AutonStepDescriptor(PASS_TO_HUMAN_3, SCORE_SPECIMEN_BAR_POSE),
                            new AutonStepDescriptor("PREPARED_TO_SCORE_SPEICMEN")
                    ))
            ),

            new AutonStepDescriptor(
                    new ArrayList<AutonStepDescriptor>(Arrays.asList(
                            new AutonStepDescriptor(SCORE_SPECIMEN_BAR_POSE, PASS_TO_HUMAN_3),
                            new AutonStepDescriptor("PICKUP_OUT_OPEN")
                    ))
            ),

            new AutonStepDescriptor(LONG_WAIT),
            new AutonStepDescriptor("PICKUP_OUT_CLOSED"),
            new AutonStepDescriptor("TRANSFER1"),
            new AutonStepDescriptor("TRANSFER2"),

            new AutonStepDescriptor(
                    new ArrayList<AutonStepDescriptor>(Arrays.asList(
                            new AutonStepDescriptor(PASS_TO_HUMAN_3, SCORE_SPECIMEN_BAR_POSE),
                            new AutonStepDescriptor("PREPARED_TO_SCORE_SPEICMEN")
                    ))
            ),

            new AutonStepDescriptor(
                    new ArrayList<AutonStepDescriptor>(Arrays.asList(
                            new AutonStepDescriptor(SCORE_SPECIMEN_BAR_POSE, PASS_TO_HUMAN_3),
                            new AutonStepDescriptor("PICKUP_OUT_OPEN")
                    ))
            ),

            new AutonStepDescriptor(LONG_WAIT),
            new AutonStepDescriptor("PICKUP_OUT_CLOSED"),
            new AutonStepDescriptor("TRANSFER1"),
            new AutonStepDescriptor("TRANSFER2"),

            new AutonStepDescriptor(PASS_TO_HUMAN_3, SCORE_SPECIMEN_BAR_POSE)


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
        return TEMP_PAUSE;
    }
}