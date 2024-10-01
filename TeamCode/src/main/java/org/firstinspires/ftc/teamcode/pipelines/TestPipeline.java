package org.firstinspires.ftc.teamcode.pipelines;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Moments;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestPipeline extends OpenCvPipeline {

    private final Telemetry telemetry;

    // Define the yellow color range in HSV
    public static final Scalar LOWER_YELLOW = new Scalar(20, 100, 100);
    public static final Scalar UPPER_YELLOW = new Scalar(30, 255, 255);

    // Variables to store detected prism position
    public volatile Point prismCenter = new Point(-1, -1); // Initialize as invalid

    // Constructor
    public TestPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input) {
        // Convert input frame to HSV color space
        Mat hsvFrame = new Mat();
        Imgproc.cvtColor(input, hsvFrame, Imgproc.COLOR_RGB2HSV);

        // Apply Gaussian blur to reduce noise
        Mat blurredFrame = new Mat();
        Imgproc.GaussianBlur(hsvFrame, blurredFrame, new Size(5, 5), 0);

        // Threshold the image to isolate yellow regions
        Mat mask = new Mat();
        Core.inRange(blurredFrame, LOWER_YELLOW, UPPER_YELLOW, mask);

        // Find contours in the masked image
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        telemetry.addData("Number of contours found", contours.size());

        // Loop through contours to process and draw each one
        for (MatOfPoint contour : contours) {
            // Draw all contours in red color
            Imgproc.drawContours(input, contours, -1, new Scalar(255, 0, 0), 2);

            // Optionally check if the contour is rectangular-like (4 vertices)
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
            double epsilon = 0.02 * Imgproc.arcLength(contour2f, true);
            MatOfPoint2f approx2f = new MatOfPoint2f();
            Imgproc.approxPolyDP(contour2f, approx2f, epsilon, true);

            MatOfPoint approx = new MatOfPoint(approx2f.toArray());

            // Check if the contour has 4 vertices (rectangle)
            if (approx.toList().size() == 4) {
                double area = Imgproc.contourArea(approx);
                if (area > 500 && area < 5000) {  // Adjusted thresholds for your 320x240 image
                    // Calculate the bounding box
                    Rect boundingBox = Imgproc.boundingRect(approx);
                    
                    // Draw the bounding box around the detected prism
                    Imgproc.rectangle(input, boundingBox.tl(), boundingBox.br(), new Scalar(0, 255, 0), 2);

                    // Output the position of the prism to telemetry
                    Moments moments = Imgproc.moments(approx);
                    prismCenter.x = moments.m10 / moments.m00;
                    prismCenter.y = moments.m01 / moments.m00;
                    telemetry.addData("Prism detected at: ", "(" + prismCenter.x + ", " + prismCenter.y + ")");
                }
            }
        }

        telemetry.update();
        return input;
    }

    @Override
    public void init(Mat input) {
        // No specific initialization needed here, but could be added if necessary
        telemetry.addLine("Pipeline initialized");
        telemetry.update();
    }
}
