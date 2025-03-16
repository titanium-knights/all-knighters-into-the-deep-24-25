package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;

@TeleOp(name = "Blue Sample Teleop Auto", group = "User Control")
public class BlueTeleop extends OpMode {
    GeneralTeleop teleop;

    Telemetry telemetryA;

    @Override
    public void init() {
        teleop = new GeneralTeleop();
        teleop.color = ConfidenceOrientationVectorPipeline.Color.BLUE;
        telemetryA = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        teleop.init(hardwareMap, telemetryA);
    }

    @Override
    public void loop() {
        teleop.loop(telemetryA, gamepad1, gamepad2);
    }
}
