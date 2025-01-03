package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Scissors;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;
import org.firstinspires.ftc.teamcode.utilities.ScissorsState;

@TeleOp(name = "Testing Teleop", group = "User Control")
public class TestingTeleop extends OpMode {


    private SubsystemManager manager;

    @Override
    public void init() {
        manager = new SubsystemManager(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            manager.scissors.scissorsToPosition(ScissorsState.IN);
            telemetry.addData("state in", true);
        }
        if (gamepad1.b) {
            manager.scissors.scissorsToPosition(ScissorsState.OUT);
            telemetry.addData("state out", false);
        }
        if (gamepad1.x) {
            manager.scissors.manualDown(-0.7);
        } else if (gamepad1.y) {
            manager.scissors.manualUp(0.5);
        } else {
            manager.scissors.stop();
        }
        telemetry.addData("encoder value", manager.scissors.getEncoder());
    }
}
