package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeBucketScore;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickup;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickupAutomated;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSamplePickupTwist90;
import org.firstinspires.ftc.teamcode.teleop.state.BeforeSpecimenScore;
import org.firstinspires.ftc.teamcode.teleop.state.BucketScore;
import org.firstinspires.ftc.teamcode.teleop.state.Init;
import org.firstinspires.ftc.teamcode.teleop.state.Neutral;
import org.firstinspires.ftc.teamcode.teleop.state.SamplePickup;
import org.firstinspires.ftc.teamcode.teleop.state.SampleTransfer;
import org.firstinspires.ftc.teamcode.teleop.state.SampleTransferAutomated;
import org.firstinspires.ftc.teamcode.teleop.state.SpecimenScore;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;
import org.firstinspires.ftc.teamcode.utilities.Webcam;

import java.util.Arrays;
import java.util.stream.Collectors;

@TeleOp(name = "Camera Testing Teleop", group = "User Control")
public class TeleopCamera extends OpMode {
    private Webcam webcam;

    @Override
    public void init() {
        webcam = new Webcam(hardwareMap);
    }

    @Override
    public void loop() {
    }
}