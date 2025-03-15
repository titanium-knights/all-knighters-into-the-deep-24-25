package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.utilities.BottomClaw;
import org.firstinspires.ftc.teamcode.utilities.SubsystemManager;

@Config
@TeleOp(name = "Testing Teleop", group = "User Control")
public class TestingTeleop extends OpMode {

    public static String additionalColor = "blue";

    public static ConfidenceOrientationVectorPipeline.Color color = additionalColor.equals("blue") ? ConfidenceOrientationVectorPipeline.Color.BLUE : ConfidenceOrientationVectorPipeline.Color.RED;
    private SubsystemManager manager;

    @Override
    public void init() {
        manager = new SubsystemManager(hardwareMap, ConfidenceOrientationVectorPipeline.Color.RED);
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
        } else if (gamepad1.dpad_left) {
            manager.bottomClaw.rotate(BottomClaw.TESTING_THETA);
        } else if (gamepad1.dpad_up) {
            manager.swiper.up();
        } else if (gamepad1.dpad_down) {
            manager.swiper.down();
        } else if (gamepad1.left_stick_y > 0.1f){
            manager.drive.move(0, 0.3, 0);
        } else if (gamepad1.left_stick_y < -0.1f){
            manager.drive.move(0, 0.1, 0);
        }
        ConfidenceOrientationVectorPipeline.DetectionResultScaledData drsd = manager.webcam.bestDetectionCoordsAngle();
        telemetry.addData("x-coord: ", drsd.getX());
        telemetry.addData("y-coord: ", drsd.getY());
        telemetry.addData("theta: ", drsd.getTheta());
        telemetry.addData("size:", drsd.size);

        telemetry.addData("claw rotation value", manager.bottomClaw.getClawRotatorPosition());
        telemetry.update();
    }
}
