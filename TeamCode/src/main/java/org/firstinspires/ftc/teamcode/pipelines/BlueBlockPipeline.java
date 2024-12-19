package org.firstinspires.ftc.teamcode.pipelines;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class BlueBlockPipeline extends OpenCvPipeline {

    private final Telemetry telemetry;

    // Define the blue color range in HSV
    public static final Scalar LOWER_BLUE = new Scalar(90, 30, 30);  // Adjusted for blue detection
    public static final Scalar UPPER_BLUE = new Scalar(150, 255, 255);

    // Variables to store the detected orientation angle
    public volatile double orientationAngle = Double.NaN;

    // Constructor
    public BlueBlockPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input) {
        // Convert the input frame to HSV for color filtering
        Mat hsv = new Mat();
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

        // Create a mask for the blue region
        Mat blueMask = new Mat();
        Core.inRange(hsv, LOWER_BLUE, UPPER_BLUE, blueMask);

        // Apply the mask to filter out the blue block
        Mat filtered = new Mat();
        Core.bitwise_and(input, input, filtered, blueMask);

        // Convert the filtered image to grayscale for edge detection
        Mat gray = new Mat();
        Imgproc.cvtColor(filtered, gray, Imgproc.COLOR_RGB2GRAY);

        // Apply Gaussian blur to reduce noise
        Imgproc.GaussianBlur(gray, gray, new org.opencv.core.Size(5, 5), 0);

        // Detect edges using Canny edge detection
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 50, 150);

        // Detect contours from the edges
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Calculate the orientation angle based on the longest detected line
        double angleSum = 0;
        int angleCount = 0;

        for (MatOfPoint contour : contours) {
            // Get the bounding box of the contour
            Point[] points = contour.toArray();
            for (int i = 0; i < points.length - 1; i++) {
                // Compute angle between consecutive points
                Point p1 = points[i];
                Point p2 = points[i + 1];
                double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x) * (180 / Math.PI);
                angleSum += angle;
                angleCount++;
            }
        }

        // Calculate the average orientation angle
        if (angleCount > 0) {
            orientationAngle = angleSum / angleCount;
            telemetry.addData("Orientation Angle", "%.2f degrees", orientationAngle);
        } else {
            telemetry.addLine("No contours detected.");
        }

        // Draw the contours on the input frame for visualization
        Imgproc.drawContours(input, contours, -1, new Scalar(0, 255, 0), 2);  // Green color for contours

        return input;  // Return the processed frame
    }

    @Override
    public void init(Mat input) {
        telemetry.addLine("BlueBlockPipeline initialized.");
        telemetry.update();
    }
}
