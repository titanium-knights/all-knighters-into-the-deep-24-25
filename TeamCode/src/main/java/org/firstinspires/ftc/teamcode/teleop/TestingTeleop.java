package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Scissors;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

@TeleOp(name = "Testing Teleop", group = "User Control")
public class TestingTeleop extends OpMode {

    private Scissors scissors;
    private SubsystemManager manager;

    @Override
    public void init() {
        manager = new SubsystemManager(hardwareMap);
        scissors = manager.scissors;
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            scissors.moveToIdlePosition();
        } else if (gamepad1.b) {
            scissors.moveToFullyExtended();
        } else if (gamepad1.x) {
            scissors.moveToLoadingPosition();
        }
    }
}
