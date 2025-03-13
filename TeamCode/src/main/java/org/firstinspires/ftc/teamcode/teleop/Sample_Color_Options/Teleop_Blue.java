package org.firstinspires.ftc.teamcode.teleop.Sample_Color_Options;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.teleop.Teleop;

@TeleOp(name = "Blue Teleop", group = "User Control")
public class Teleop_Blue extends OpMode {

    Teleop teleop;
    @Override
    public void init(){
        teleop = new Teleop();
        teleop.init();
    }

    @Override
    public void loop(){
        teleop.loop();
    }

}
