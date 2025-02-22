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
            manager.bottomClaw.rightWristHalfUpPosition();
            //manager.bottomClaw.openClaw();
            //manager.bottomClaw.neutralClawRotatorPosition();
            telemetry.addData("open", true);
        } else if (gamepad1.right_bumper){
            manager.bottomClaw.rightWristDownPosition();
            //manager.bottomClaw.closeClaw();
            //manager.bottomClaw.orthogonalClawRotatorPosition();
            telemetry.addData("closed", true);
        } else if (gamepad1.a) {
            manager.horizontalSlides.manualForward(0.7);
        } else if (gamepad1.b) {
            manager.horizontalSlides.manualBack(0.7);
        } else {
            manager.horizontalSlides.stop();
        }
        telemetry.addData("scissors encoder value", manager.horizontalSlides.getEncoder());
    }
}
