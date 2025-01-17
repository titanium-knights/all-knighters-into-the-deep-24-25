package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

@TeleOp(name = "Testing Teleop", group = "User Control")
public class TestingTeleop extends OpMode {


    private SubsystemManager manager;

    @Override
    public void init() {
        manager = new SubsystemManager(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.left_bumper) {
            manager.topClaw.open();
            telemetry.addData("open", true);
        } else if (gamepad1.right_bumper){
            manager.topClaw.close();
            telemetry.addData("closed", true);
        }
        telemetry.addData("scissors encoder value", manager.topClaw.getPosition());
    }
}
