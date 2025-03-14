package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;

@TeleOp(name = "Blue Teleop Auto", group = "User Control")
public class BlueTeleop extends OpMode {
    GeneralTeleop teleop;

    @Override
    public void init() {
        teleop = new GeneralTeleop();
        teleop.color = ConfidenceOrientationVectorPipeline.Color.BLUE;
        teleop.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        teleop.loop(telemetry, gamepad1, gamepad2);
    }
}
