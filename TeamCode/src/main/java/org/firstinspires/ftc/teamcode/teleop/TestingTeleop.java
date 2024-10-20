package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.Arm;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Slides;

@TeleOp(name="testing Teleop", group="default")
public class TestingTeleop extends OpMode {

    private SimpleMecanumDrive drive;
    private Claw claw;
    private Slides slides;
    private Arm arm;

    final float STICK_MARGIN = 0.5f;
    final double normalPower = 0.85;

    @Override
    public void init() {
        // initialize util classes for hardware
        drive = new SimpleMecanumDrive(hardwareMap);
        claw = new Claw(hardwareMap);
        slides = new Slides(hardwareMap);
        arm = new Arm(hardwareMap);
    }

    @Override
    public void loop() {
        if (time < 2.23) {
            telemetry.addData("arm to init", arm.getPosition());
            arm.setDirectionTowardsInit();
            claw.goToFoldedPosition();
        }
        if (time > 2.35 && time < 4.78) {
            telemetry.addData("arm to pickup", arm.getPosition());
            arm.setDirectionAwayFromInit();
            claw.goToDropPosition();
        }
        if (time > 4.78 && time < 6.23) {
            telemetry.addData("stop", arm.getPosition());
            arm.stop();
        }
        telemetry.update();
    }
}