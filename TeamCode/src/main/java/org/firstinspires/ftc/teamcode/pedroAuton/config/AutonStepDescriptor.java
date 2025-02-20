package org.firstinspires.ftc.teamcode.pedroAuton.config;
import java.util.List;

import com.pedropathing.localization.Pose;

public class AutonStepDescriptor {
    
    // ===== Step Script Definitions =====

    /**
     * StepType enumerates the types of steps.
     */
    public enum StepType {
        PATH,      // A path between two poses.
        STATE,    // A mechanism action.
        SLEEP,     // A pause (sleep).   // A composite step running multiple sub-steps concurrently.
        PARALLEL,
    }

    /**
     * AutonStepDescriptor holds the configuration for one step.
     * - For PATH steps: provide start and end poses.
     * - For ACTION steps: provide an action code and optional timeout (in seconds).
     * - For SLEEP steps: provide a duration (in seconds).
     */
    public StepType type;
    // For PATH steps:
    public Pose startPose;
    public Pose endPose;
    // For STATE steps:
    public String autonStateCode;
    // For SLEEP steps:
    public double duration; // seconds
    // For STATE steps, a timeout after which the step is forced complete.
    public double timeout; // seconds

    public List<AutonStepDescriptor> steps;

    // Constructor for PATH steps.
    public AutonStepDescriptor(Pose startPose, Pose endPose) {
        this.type = StepType.PATH;
        this.startPose = startPose;
        this.endPose = endPose;
    }

    // Constructor for STATE steps without timeout.
    public AutonStepDescriptor(String autonStateCode) {
        this.type = StepType.STATE;
        this.autonStateCode = autonStateCode;
    }

    // Constructor for STATE steps with timeout.
    public AutonStepDescriptor(String autonStateCode, double timeout) {
        this.type = StepType.STATE;
        this.autonStateCode = autonStateCode;
        this.timeout = timeout;
    }

    // Constructor for SLEEP steps.
    public AutonStepDescriptor(double duration) {
        this.type = StepType.SLEEP;
        this.duration = duration;
    }

    public AutonStepDescriptor(List<AutonStepDescriptor> steps) {
        this.type = StepType.PARALLEL;
        this.steps = steps;
    }
}