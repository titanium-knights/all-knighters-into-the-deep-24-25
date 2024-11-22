package org.firstinspires.ftc.teamcode.pipelines;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestPipeline extends OpenCvPipeline {

    private final Telemetry telemetry;

    // Define the yellow color range in HSV
    public static final Scalar LOWER_YELLOW = new Scalar(20, 100, 100);
    public static final Scalar UPPER_YELLOW = new Scalar(30, 255, 255);

    // Constructor
    public TestPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input) {
        // Convert input frame to HSV for color-based filtering
        Mat hsv = new Mat();
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

        // Create a mask for yellow colors
        Mat yellowMask = new Mat();
        Core.inRange(hsv, LOWER_YELLOW, UPPER_YELLOW, yellowMask);

        // Apply morphological transformations to clean up the mask
        Mat morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(yellowMask, yellowMask, Imgproc.MORPH_CLOSE, morphKernel);
        Imgproc.morphologyEx(yellowMask, yellowMask, Imgproc.MORPH_OPEN, morphKernel);

        // Detect edges on the masked region
        Mat edges = new Mat();
        Imgproc.Canny(yellowMask, edges, 50, 150);

        // Find contours from the masked edges
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Filter contours to find a hexagon or square shape
        Mat output = input.clone();
        for (MatOfPoint contour : contours) {
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());

            // Approximate contour to reduce points and smooth it out
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            Imgproc.approxPolyDP(contour2f, approxCurve, Imgproc.arcLength(contour2f, true) * 0.04, true);

            int vertices = (int) approxCurve.total();
            if (vertices == 6 || vertices == 4) {  // Looking for hexagons or squares
                // Convert back to MatOfPoint for drawing
                MatOfPoint approxCurveInt = new MatOfPoint(approxCurve.toArray());

                // Check if the contour has the correct aspect ratio and area to filter out noise
                Rect boundingRect = Imgproc.boundingRect(approxCurveInt);
                double aspectRatio = (double) boundingRect.width / boundingRect.height;

                // If the shape has an aspect ratio close to 1, it's likely a square or hexagon
                if (vertices == 6 || (vertices == 4 && Math.abs(aspectRatio - 1) < 0.2)) {
                    Scalar color = new Scalar(0, 255, 0);  // Green color for detected shapes
                    Imgproc.drawContours(output, Collections.singletonList(approxCurveInt), -1, color, 2);

                    telemetry.addLine("Detected a hexagon or square shape.");
                }
            }
        }

        telemetry.update();
        return output;  // Return the frame with detected shapes highlighted
    }

    @Override
    public void init(Mat input) {
        telemetry.addLine("Pipeline initialized");
        telemetry.update();
    }
}