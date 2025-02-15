package org.firstinspires.ftc.teamcode.auton.paths;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.auton.AutonMethods;
import org.firstinspires.ftc.teamcode.utilities.HorizontalSlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;

@Autonomous(name="specimenPark", group="Linear OpMode")
@Config
public class specimenPark extends AutonMethods {
    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        telemetry.addData("Initialized: ", "");
        telemetry.update();

        ElapsedTime runtime = new ElapsedTime();

        // initialize positions
        slides.slideToPosition(SlideState.BOTTOM);
//        scissors.scissorsToPosition(ScissorsState.IN);
        topClaw.close();
        waitForStart();
        runtime.reset();

//        scissors.scissorsToPosition(ScissorsState.IN);
        attachSpecimenToHigherBar();
        backOne();
        rightTwo();

        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.update();
    }
}
