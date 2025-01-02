package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Scissors;
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
        if (gamepad1.a) {
            manager.scissors.scissorsLeftIn();
        } else if (gamepad1.b) {
            manager.scissors.scissorsRightIn();
        } else if (gamepad1.x) {
            manager.scissors.scissorsLeftOut();
        } else if (gamepad1.y) {
            manager.scissors.scissorsRightOut();
        }
    }
}
