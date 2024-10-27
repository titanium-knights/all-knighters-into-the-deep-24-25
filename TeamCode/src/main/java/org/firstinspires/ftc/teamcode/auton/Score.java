package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.Arm;

@Config
@Autonomous(name="Score", group="Auton")
public class Score extends LinearOpMode {
    public static int crossTime = 1000;
    public static int crossBackTime = 1000;
    public static int straightenTime = 1000;

    private Claw claw;
    private Slides slides;
    private Arm arm;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Initialized: ", "Hopefully");
        telemetry.update();

        final double POWER = 0.85;
        ElapsedTime runtime = new ElapsedTime();
        SimpleMecanumDrive drivetrain = new SimpleMecanumDrive(hardwareMap);

        claw = new Claw(hardwareMap);
        slides = new Slides(hardwareMap);
        arm = new Arm(hardwareMap);

        claw.close();

        waitForStart();
        runtime.reset();

        drivetrain.move(0, POWER, 0);
        sleep(800);
        telemetry.addLine("move robot forward");
        telemetry.update();

        arm.toDropSpecimen();
        claw.goToFoldedPosition();
        sleep(200);
        telemetry.addLine("lift arm and claw rotator");
        telemetry.update();

        slides.changeToUpState();
        sleep(400);
        telemetry.addLine("move slides up");
        telemetry.update();

        drivetrain.move(0, POWER, 0);
        sleep(100);
        telemetry.addLine("move robot forward");
        telemetry.update();

        slides.changeToDownState();
        sleep(300);
        telemetry.addLine("move slides down");
        telemetry.update();

        claw.open();
        telemetry.addLine("release claw");
        telemetry.update();

        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.update();
    }
}
