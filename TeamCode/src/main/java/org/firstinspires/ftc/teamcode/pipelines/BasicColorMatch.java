package org.firstinspires.ftc.teamcode.pipelines;

//import org.firstinspires.ftc1.teamcode.utilities.SubsystemManager;
//import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class BasicColorMatch extends OpenCvPipeline {
    private static final Scalar LOWER_YELLOW = new Scalar(0,50,0); // #b08618 in RGB
    private static final Scalar UPPER_YELLOW = new Scalar(236, 195, 77); // #ecbc4d in RGB
    private final Mat processedMat = new Mat();
    private final Mat mask = new Mat();
    private volatile double averageAngle = 0.0;
    private volatile double centerX = 0.0;
    private volatile double centerY = 0.0;
//    Telemetry telemetry;

    public BasicColorMatch() {
    }

    @Override
    public Mat processFrame(Mat input) {
//        telemetry.clearAll();
        Imgproc.cvtColor(input, processedMat, Imgproc.COLOR_RGB2YCrCb);

        Imgproc.GaussianBlur(processedMat, processedMat, new Size(9, 13), 5);

        Core.inRange(processedMat, LOWER_YELLOW, UPPER_YELLOW, mask);

        // Find contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f largestContour = null;
        double maxArea = 0;

        // Find the largest contour (assuming it's the quadrilateral)
        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area >= maxArea) {
                maxArea = area;
                largestContour = new MatOfPoint2f(contour.toArray());
            }
        }

        if (largestContour != null) {
            // Approximate the contour to a polygon
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            double epsilon = 0.02 * Imgproc.arcLength(largestContour, true);
            Imgproc.approxPolyDP(largestContour, approxCurve, epsilon, true);

            // Check if it's a quadrilateral
            if (approxCurve.total() == 4) {
                // Convert to MatOfPoint
                MatOfPoint quadrilateral = new MatOfPoint(approxCurve.toArray());

                // Print the four corner points
                for (Point p : quadrilateral.toArray()) {
                    log("Corner: " + p);
                }

                // Draw the rectangle by connecting the four points
                Point[] points = quadrilateral.toArray();

                Arrays.sort(points, (a, b) -> {
                    if (a.y != b.y) {
                        return Double.compare(a.y, b.y);
                    } else {
                        return Double.compare(a.x, b.x);
                    }
                });

                // After sorting, points[0] and points[1] are the top points, points[2] and points[3] are the bottom points
                Point topLeft = points[0].x < points[1].x ? points[0] : points[1];
                Point topRight = points[0].x > points[1].x ? points[0] : points[1];
                Point bottomLeft = points[2].x < points[3].x ? points[2] : points[3];
                Point bottomRight = points[2].x > points[3].x ? points[2] : points[3];

                points = new Point[]{topRight, topLeft, bottomLeft, bottomRight};

                // calculate center of mass
                double sumX = 0.0, sumY = 0.0;
                for (Point p : points) {
                    sumX += p.x;
                    sumY += p.y;
                }

                centerX = sumX / 4;
                centerY = sumY / 4;

                Scalar[] colors = new Scalar[] {new Scalar(255, 0, 0), new Scalar(0, 255, 0), new Scalar(0, 0, 255), new Scalar(255, 255, 0)};
                for (int i = 0; i < points.length; i++) {
                    Imgproc.line(input, points[i], points[(i + 1) % points.length], colors[i], 2);
                    Imgproc.putText(input, "("+points[i].x+"," + points[i].y+")", points[i], Imgproc.FONT_HERSHEY_SIMPLEX, 0.4, new Scalar(0,0,0), 1);
                }

                double thetaOne = Math.atan2(points[2].y - points[1].y, points[2].x - points[1].x);
                double thetaTwo = Math.atan2(points[3].y - points[0].y, points[3].x - points[0].x);

                double currentAverageAngle = Math.toDegrees((thetaOne + thetaTwo) / 2);

                if (Math.abs(points[2].y - points[1].y) > Math.abs(points[3].x-points[2].x) || Math.abs(points[3].y - points[0].y) > Math.abs(points[0].x-points[1].x)) {
                    averageAngle = 90 + currentAverageAngle;
                    log("Rotated 90 degrees");
                    log("Y diff: " + (points[2].y - points[1].y) + " X diff: " + (points[2].x - points[1].x));
                } else averageAngle = currentAverageAngle;

                Imgproc.putText(input,"angle: "+averageAngle,new Point(2, 30),Imgproc.FONT_HERSHEY_COMPLEX,1,new Scalar(255, 0, 0),2);

                // Convert YCrCb back to RGB
                Imgproc.cvtColor(processedMat, processedMat, Imgproc.COLOR_YCrCb2RGB);

            } else {
                log("Could not approximate a quadrilateral.");
                if (approxCurve.total() > 4) {
                    log("Too many points: " + approxCurve.total());
                } else {
                    log("Too few points: " + approxCurve.total());
                }
            }
        }

        log("Average angle: " + (Math.abs(averageAngle)));
        return input;
    }

    public double getAverageAngle() {
        return averageAngle;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void log(String s) {
//        System.err.println(s);
//        telemetry.addLine(s);
    }
}