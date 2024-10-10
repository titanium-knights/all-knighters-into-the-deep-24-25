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
        // Convert the input frame to grayscale for edge detection
        Mat gray = new Mat();
        Imgproc.cvtColor(input, gray, Imgproc.COLOR_RGB2GRAY);

        // Apply Canny edge detection
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 100, 200);

        // Detect "yellowness" in the input frame
        Mat hsv = new Mat();
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);  // Convert to HSV for easier color detection

        // Define a broad range of "yellow-like" colors
        Scalar lowerYellow = new Scalar(15, 100, 50);  // Lower range for yellows and dark browns
        Scalar upperYellow = new Scalar(45, 255, 255); // Upper range for yellows

        // Create a mask for yellow-like colors
        Mat yellowMask = new Mat();
        Core.inRange(hsv, lowerYellow, upperYellow, yellowMask);

        // Combine the edges with the yellow mask
        Mat maskedEdges = new Mat();
        Core.bitwise_and(edges, yellowMask, maskedEdges);

        // Find contours from the masked edges
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(maskedEdges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Collect all points from all contours into one list
        List<Point> allPoints = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            allPoints.addAll(contour.toList());
        }

        // Remove outliers from the collected points
        List<Point> filteredPoints = removeOutliers(allPoints, 2.0);  // You can adjust the threshold factor

        // Calculate the convex hull of the filtered points
        MatOfInt hullIndices = new MatOfInt();
        Imgproc.convexHull(new MatOfPoint(filteredPoints.toArray(new Point[0])), hullIndices);

        // Create a list for the hull points
        List<Point> hullPoints = new ArrayList<>();
        for (int index : hullIndices.toArray()) {
            hullPoints.add(filteredPoints.get(index));
        }

        // Draw the convex hull on the original image with a specific color (e.g., green)
        MatOfPoint hull = new MatOfPoint();
        hull.fromList(hullPoints);
        Scalar hullColor = new Scalar(0, 255, 0); // Green color for hull
        Imgproc.drawContours(input, Collections.singletonList(hull), -1, hullColor, 2); // Draw the convex hullList.of(hull), -1, hullColor, 2); // Draw the convex hull

        telemetry.addLine("Convex hull of filtered edges drawn on original frame.");
        
        return input;  // Return the original frame with the convex hull drawn
    }

    /**
     * Method to remove outlier points based on distance from the mean point.
     * @param points The list of points to filter.
     * @param thresholdFactor The number of standard deviations to use as a threshold.
     * @return A filtered list of points without outliers.
     */
    private List<Point> removeOutliers(List<Point> points, double thresholdFactor) {
        if (points.isEmpty()) return points;

        // Calculate the mean point
        double meanX = points.stream().mapToDouble(point -> point.x).average().orElse(0);
        double meanY = points.stream().mapToDouble(point -> point.y).average().orElse(0);
        Point meanPoint = new Point(meanX, meanY);

        // Calculate the distances from the mean point
        List<Double> distances = new ArrayList<>();
        for (Point point : points) {
            double distance = Math.sqrt(Math.pow(point.x - meanPoint.x, 2) + Math.pow(point.y - meanPoint.y, 2));
            distances.add(distance);
        }

        // Calculate mean and standard deviation of distances
        double meanDistance = distances.stream().mapToDouble(d -> d).average().orElse(0);
        double stdDev = Math.sqrt(distances.stream().mapToDouble(d -> Math.pow(d - meanDistance, 2)).average().orElse(0));

        // Filter points that are within the specified threshold factor of the standard deviation
        List<Point> filteredPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            if (distances.get(i) <= meanDistance + thresholdFactor * stdDev) {
                filteredPoints.add(points.get(i));
            }
        }

        return filteredPoints;
    }

    @Override
    public void init(Mat input) {
        // No specific initialization needed here, but could be added if necessary
        telemetry.addLine("Pipeline initialized");
        telemetry.update();
    }
}
