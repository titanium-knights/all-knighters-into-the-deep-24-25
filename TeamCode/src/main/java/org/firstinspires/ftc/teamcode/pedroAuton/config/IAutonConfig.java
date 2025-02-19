package org.firstinspires.ftc.teamcode.pedroAuton.config;

import com.pedropathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonStepDescriptor;
import java.util.List;

/**
 * IAutonConfig defines the interface for an autonomous configuration.
 * Any auton config must supply:
 *   - A starting pose.
 *   - A full list (script) of step descriptors (motions, actions, sleeps, or composite steps).
 *   - Global timing parameters (e.g. pause time between segments).
 */
public interface IAutonConfig {
    /**
     * @return the starting Pose for the robot.
     */
    Pose getStartPose();

    /**
     * @return the ordered list of step descriptors for the autonomous routine.
     */
    List<AutonStepDescriptor> getRoutine();

    /**
     * @return the pause time (in milliseconds) to wait between motion segments.
     */
    int getSegmentSleepTimeMs();
}