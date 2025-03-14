package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.utilities.BottomClaw;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

@TeleOp(name = "Testing Teleop", group = "User Control")
public class TestingTeleop extends OpMode {


    private SubsystemManager manager;

    @Override
    public void init() {
        manager = new SubsystemManager(hardwareMap, ConfidenceOrientationVectorPipeline.Color.RED, Teleop.Strategy.SAMPLE);
    }

    @Override
    public void loop() {
        if (gamepad1.y) manager.drive.move(0,-0.2,0);
        else manager.drive.move(0,0,0);
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
            manager.bottomClaw.rotate(BottomClaw.TESTING_THETA);
        } else if (gamepad1.b) {
            manager.topClaw.close();
        } else if (gamepad1.x) {
            manager.topClaw.open();
        }
        telemetry.addData("claw rotation value", manager.bottomClaw.getClawRotatorPosition());
        telemetry.update();
    }
}
