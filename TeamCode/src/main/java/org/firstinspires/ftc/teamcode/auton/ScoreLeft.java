package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.utilities.TopClaw;


@Config
@Autonomous(name="ScoreLeft", group="Auton")
public class ScoreLeft extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Initialized: ", "");
        telemetry.update();

        ElapsedTime runtime = new ElapsedTime();
        SimpleMecanumDrive drivetrain = new SimpleMecanumDrive(hardwareMap);

        Slides slides = new Slides(hardwareMap);
        TopClaw topClaw = new TopClaw(hardwareMap);

        // initialize positions
        slides.slideToPosition(SlideState.BOTTOM);
        topClaw.close();
        waitForStart();
        runtime.reset();

        // preloaded specimen scoring
        drivetrain.move(1,0,0);
        slides.slideToPosition(SlideState.MEDIUM);
        sleep(500);
        slides.slideToPosition(SlideState.MEDIUM_SCORE);
        drivetrain.move(-1,0,0);
        sleep(400);
        slides.slideToPosition(SlideState.BOTTOM);

        // first push back three of them
        // push one
        drivetrain.move(1, 1,0);
        sleep(500);
        drivetrain.move(1, 0, 0);
        sleep(300);
        drivetrain.move(0, 1, 0);
        sleep(200);
        drivetrain.move(-1, 0, 0);
        sleep(600);

        // push two
        drivetrain.move(1, 0, 0);
        sleep(600);
        drivetrain.move(0, 1, 0);
        sleep(200);
        drivetrain.move(-1, 0, 0);
        sleep(600);

        // push three
        drivetrain.move(1, 0, 0);
        sleep(600);
        drivetrain.move(0, 1, 0);
        sleep(200);
        drivetrain.move(-1, 0, 0);
        sleep(600);

        // go back (park)
        drivetrain.move(-1, 1, 0);
        sleep(700);
        drivetrain.move(0, 0, 0);

        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.update();
    }
}