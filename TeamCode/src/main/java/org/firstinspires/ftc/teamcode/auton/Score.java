package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.Arm;
import org.firstinspires.ftc.teamcode.utilities.ArmState;

@Config
@Autonomous(name="Score", group="Auton")
public class Score extends LinearOpMode {
    public static int crossTime = 1000;
    public static int armTime = 2150;
    public static int straightenTime = 1000;

    private Claw claw;
    private Slides slides;
    private Arm arm;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Initialized: ", "");
        telemetry.update();

        ElapsedTime runtime = new ElapsedTime();
        SimpleMecanumDrive drivetrain = new SimpleMecanumDrive(hardwareMap);

        claw = new Claw(hardwareMap);
        slides = new Slides(hardwareMap);
        arm = new Arm(hardwareMap);

        claw.close();

        waitForStart();
        runtime.reset();
        sleep(3000);
        sleep(10);
        telemetry.addLine("move arm and forearm into position");
        telemetry.update();

        drivetrain.move(0,-.55,0);
        sleep(250);
        drivetrain.move(0,0,0);

       // claw.toSpecimenPosition();
        claw.close();
        //arm.runToPosition(ArmState.SPECIMEN);
        arm.toScoreSpecimenPos();
        sleep(2000);

        telemetry.addLine("Run into the bar");
        telemetry.update();
        drivetrain.move(0, -.55, 0);
        sleep(780);
        drivetrain.move(0,0,0);
        sleep(2000);
        claw.open();
       // claw.toDropPositionAuton();
        sleep(1000);
        drivetrain.move(0, .55, 0);
//        sleep(730);
        drivetrain.move(0, 0, 0);

       // arm.setPower(-1);
        arm.toInitPos();
//        claw.toFoldedPosition();
        claw.close();
        sleep(2650);
        //arm.setPower(0);

        sleep(1000);
//        arm.setPower(1);
//        sleep(2650);
        //arm.runToPosition(ArmState.INIT);
        //arm.setPower(0);

//        slides.changeToUpState(-.7);
//        sleep(300);
//        telemetry.addLine("move slides up");
//        telemetry.update();

//        slides.stop();
//        sleep(2000);
//        telemetry.addLine("move slides up");
//        telemetry.update();
//
//        drivetrain.move(0, POWER, 0);
//        sleep(10);
//        telemetry.addLine("move robot forward");
//        telemetry.update();
//
//        drivetrain.move(0, 0, 0);

////        slides.changeToDownState(.3);
//        sleep(50);
//        telemetry.addLine("move slides down");
//        telemetry.update();
//
//        claw.open();
//        telemetry.addLine("release claw");
//        telemetry.update();

//        drivetrain.move(0, -POWER, 0);
//        sleep(10);
//        telemetry.addLine("move robot backwards");
//        telemetry.update();
//
//        drivetrain.move(0, 0, 0);

////        arm.toFoldedPosition();
//        claw.toFoldedPosition();
//        sleep(400);
//        telemetry.addLine("revert to init position");
//        telemetry.update();
//
//        telemetry.addData("Status", "Run Time: " + runtime);
//        telemetry.update();
    }
}
