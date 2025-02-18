package org.firstinspires.ftc.teamcode.pedroAuton;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonCompiler;
import org.firstinspires.ftc.teamcode.pedroAuton.config.AutonCompiler.IAutonStep;
import org.firstinspires.ftc.teamcode.pedroAuton.config.IAutonConfig;
import org.firstinspires.ftc.teamcode.pedroAuton.config.RightOneSpecimenParkConfig;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

/**
 * RightOneSpecimenPark is an autonomous OpMode.
 * It imports the AutonCompiler (from the config layer) and the unique config
 * (RightOneSpecimenParkConfig) associated with this auton.
 * The OpMode compiles the config into executable steps and runs them sequentially.
 */
@Autonomous(name = "Right Park", group = "Pedro Autons")
public class RightOneSpecimenPark extends OpMode {

    private Follower follower;
    private SubsystemManager subsystemManager;
    private Timer opmodeTimer;

    // The unique configuration for this auton.
    private IAutonConfig config;

    // The list of compiled steps.
    private java.util.List<IAutonStep> steps;
    private int currentStepIndex = 0;

    @Override
    public void init() {
        opmodeTimer = new Timer();

        // Initialize hardware subsystems.
        subsystemManager = new SubsystemManager(hardwareMap, telemetry);
        subsystemManager.arm.toInitPos();
        subsystemManager.topClaw.close();

        Constants.setConstants(FConstants.class, LConstants.class);

        // Initialize the follower.
        follower = new Follower(hardwareMap);

        // Import the unique configuration.
        config = new RightOneSpecimenParkConfig();

        // Set the starting pose.
        follower.setStartingPose(config.getStartPose());

        // Use the compiler utility to compile the config into executable steps.
        steps = AutonCompiler.compile(config);
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        currentStepIndex = 0;
        if (!steps.isEmpty()) {
            steps.get(0).init(follower, subsystemManager);
        }
    }

    @Override
    public void loop() {
        // Always update the follower.
        follower.update();

        // Process the current step.
        if (currentStepIndex < steps.size()) {
            IAutonStep currentStep = steps.get(currentStepIndex);
            if (currentStep.update()) {  // If the current step is complete...
                currentStepIndex++;
                if (currentStepIndex < steps.size()) {
                    steps.get(currentStepIndex).init(follower, subsystemManager);
                }
            }
        }
        telemetry.addData("Current Step", currentStepIndex);
        telemetry.update();
    }

    @Override
    public void stop() {
        // Cleanup if needed.
    }
}