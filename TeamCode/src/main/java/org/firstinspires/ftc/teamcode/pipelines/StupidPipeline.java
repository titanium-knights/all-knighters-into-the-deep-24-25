package org.firstinspires.ftc.teamcode.pipelines;

//import org.firstinspires.ftc1.teamcode.utilities.SubsystemManager;
//import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StupidPipeline extends OpenCvPipeline {

    public StupidPipeline() {
    }

    @Override
    public Mat processFrame(Mat input) {
        return input;
    }

    public double getAverageAngle() {
        return 0.0;
    }

    public double getCenterX() {
        return 0.0;
    }

    public double getCenterY() {
        return 0.0;
    }

    public void log(String s) {
//        System.err.println(s);
//        telemetry.addLine(s);
    }
}