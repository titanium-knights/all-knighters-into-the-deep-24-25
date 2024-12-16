package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.Arm;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.topClaw;

@Config
@Autonomous(name="Score", group="Auton")
public class ScoreSlow extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Initialized: ", "");
        telemetry.update();

        ElapsedTime runtime = new ElapsedTime();
        SimpleMecanumDrive drivetrain = new SimpleMecanumDrive(hardwareMap);

        topClaw claw = new topClaw(hardwareMap);
        Slides slides = new Slides(hardwareMap);
        Arm arm = new Arm(hardwareMap);

        claw.close();
        arm.toInitPos();
        waitForStart();
        runtime.reset();

        sleep(10000);
        sleep(10);

        // primary movement
        drivetrain.move(.55, 0, 0);
        sleep(1000);
        drivetrain.move(0, -0.8, 0);
        sleep(1000);

        telemetry.addLine("move arm and forearm into position");
        telemetry.update();

        claw.close();
        arm.toScoreSpecimenPos();
        boolean slidesAtPosition = false;
        while (!slidesAtPosition) {
            slidesAtPosition = slides.slideToPosition(SlideState.MEDIUM);
        }
        sleep(2000);

        telemetry.addLine("Run into the bar");
        telemetry.update();
        drivetrain.move(0, -.55, 0);
        sleep(780);
        drivetrain.move(0,0,0);
        sleep(2000);

        slidesAtPosition = false;
        while (!slidesAtPosition && opModeIsActive()) {
            slidesAtPosition = slides.slideToPosition(SlideState.MEDIUMSCORE);
        }

        sleep(1000);
        claw.open();
        drivetrain.move(0, .55, 0);
        drivetrain.move(0, 0, 0);


        slidesAtPosition = false;
        while (!slidesAtPosition && opModeIsActive()) {
            slidesAtPosition = slides.slideToPosition(SlideState.BOTTOM);
        }

        sleep(100);
        drivetrain.move(0, .55, 0);
        sleep(800);
        drivetrain.move(0, 0, 0);
        sleep(100);
        drivetrain.move(.8, 0 ,0);
        sleep(1000);
        drivetrain.move(0,0,0.4);
        sleep(100);
        drivetrain.move(0,0,0);
        sleep(1000);

        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.update();
    }
}