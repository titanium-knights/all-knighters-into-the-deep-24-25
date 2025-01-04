package org.firstinspires.ftc.teamcode.auton.paths;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.ScissorsState;
import org.firstinspires.ftc.teamcode.utilities.SlideState;
import org.firstinspires.ftc.teamcode.auton.AutonMethods;

@Autonomous(name="specimenPushSpecimen", group="Linear OpMode")
@Config
public class specimenPushSpecimen extends AutonMethods {
    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        telemetry.addData("Initialized: ", "");
        telemetry.update();

        ElapsedTime runtime = new ElapsedTime();

        // initialize positions
        slides.slideToPosition(SlideState.BOTTOM);
        scissors.scissorsToPosition(ScissorsState.IN);
        topClaw.close();
        waitForStart();
        runtime.reset();

        scissors.scissorsToPosition(ScissorsState.IN);

        // preloaded specimen scoring

        attachSpecimenToHigherBar();
        backHalf();
        rightOne();
        forwardOne();
        rightOneEighth();
        backTwo();

        // move to pushing position
//        diagonalBackRightOne();
//        diagonalForwardRightOne();

        // first push back three of them
        // push one
//        backTwo();
//
//        forwardTwo();
//        rightOneEighth();

        // turn around (now it's facing backwards)


        // scoring specimen mechanism
        // pickup from wall
//        pickUpFromWall();

        // turn, backup, then forward to score


        // then reverse the process

        // pickup from wall

        // turn + backup, then turn + forward, then forward to score

        // then reverse the process

        // go back (park)


        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.update();
    }
}
