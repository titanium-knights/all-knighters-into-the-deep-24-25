package org.firstinspires.ftc.teamcode.pipelines;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.teleop.Teleop;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// We use static imports for convenience
import static org.firstinspires.ftc.teamcode.pipelines.DenoiseUtils.downscale;

/**
 * Pipeline that:
 *  1) Downscales the image for faster processing
 *  2) Applies a 1D fast box blur to denoise (but only every Nth frame)
 *  3) Converts to HSV, thresholds for yellow
 *  4) Finds contours, computes bounding boxes + confidence
 *  5) Sorts by confidence, draws the top 5 bounding boxes on the *original* image scale
 */
public class ConfidenceOrientationVectorPipeline extends OpenCvPipeline {
    // You can tweak these
    public static final double SCALE_FACTOR = 0.5; // Downscale 50%
    public static final int BLUR_RADIUS = 1;       // Radius=1 => 3x3 kernel
    public static final int SKIP_FRAMES = 3;       // Denoise every 3rd frame

    // Define the yellow color range in HSV
    public static final Scalar LOWER_YELLOW = new Scalar(14, 141, 215);
    public static final Scalar UPPER_YELLOW = new Scalar(35, 252, 255);

    // Define the blue color range in HSV
    public static final Scalar LOWER_BLUE = new Scalar(111, 79, 59);
    public static final Scalar UPPER_BLUE = new Scalar(115, 255, 255);

    // Define the red color range in HSV
    public static final Scalar LOWER_RED_1 = new Scalar(0,119,113);
    public static final Scalar UPPER_RED_1 = new Scalar(5, 244, 255);
    public static final Scalar LOWER_RED_2 = new Scalar(173, 118, 0);
    public static final Scalar UPPER_RED_2 = new Scalar(179, 255, 255);

    // Class to hold the result of each detection: bounding box + confidence
    public static class DetectionResult {
        public RotatedRect rect;  // in the *downscaled* coordinate space
        public double confidence;

        public Point[] points;
        public boolean pickupable;

        public DetectionResult(RotatedRect rect, double confidence, Point[] points, boolean pickupable) {
            this.rect = rect;
            this.confidence = confidence;
            this.points = points;
            this.pickupable = pickupable;
//            this.points = points;
        }
    }

    // List of detection results (each has a RotatedRect + confidence)
    private final List<DetectionResult> detectionResults = new ArrayList<>();
    private DetectionResult bestDetectionResult;

    enum Mode {
        PROD,
        DEBUG
    }

    private final Mode mode = Mode.PROD;

    // Keep track of frames to decide when to denoise
    private int frameCount = 0;

    public enum Color {
        RED,
        BLUE
    }

    Color color;
    Teleop.Strategy strategy;

    // Constructor
    public ConfidenceOrientationVectorPipeline(Color color, Teleop.Strategy strategy) {
    }

    Mat canvas, down, processed, hsvImage, yellow_mask, color_mask, blue_mask, red_mask_1, red_mask_2, mask, hierarchy;


    @Override
    public Mat processFrame(Mat input) {
        // Increment frame counter
        frameCount++;

        // Clear previous detection results
        detectionResults.clear();
        bestDetectionResult = null;

        // 1) Make a copy of the original for final drawing
        canvas = input.clone();

        // 2) Downscale for faster processing
        down = downscale(input, SCALE_FACTOR);

        // 3) Denoise only on every SKIP_FRAMES-th frame
        processed = new Mat();
        if (frameCount % SKIP_FRAMES == 0) {
            Imgproc.GaussianBlur(down, processed, new Size(3, 3), 0, 0);
        } else {
            processed = down;
        }

        // 4) Convert (processed) image to HSV color space
        hsvImage = new Mat();
        Imgproc.cvtColor(processed, hsvImage, Imgproc.COLOR_RGB2HSV);

        // 5a) Threshold for yellow
        yellow_mask = new Mat();
        Core.inRange(hsvImage, LOWER_YELLOW, UPPER_YELLOW, yellow_mask);

        // 5b) Threshold for specified color
        color_mask = new Mat();
        if (color == Color.BLUE){
            Core.inRange(hsvImage, LOWER_BLUE, UPPER_BLUE, blue_mask);
        } else if (color == Color.RED){
            Core.inRange(hsvImage, LOWER_RED_1, UPPER_RED_1, red_mask_1);
        }



        mask = new Mat();
        Core.bitwise_or(yellow_mask, color_mask, mask);

        // 6) Find contours in downscaled space
        List<MatOfPoint> contours = new ArrayList<>();
        hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        if (contours.isEmpty()) {
            return canvas;
        }



        // 7) Compute bounding boxes + confidence in downscaled space
        for (MatOfPoint contour : contours) {
            double contourArea = Imgproc.contourArea(contour);
            boolean pickupable = contourArea >= 30000;

            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
            RotatedRect rect = Imgproc.minAreaRect(contour2f);

            // Confidence = ratio of contour area to bounding box area
            double boxArea = rect.size.width * rect.size.height;
            double confidence = (boxArea > 0) ? (contourArea / boxArea) : 0;
            // Clamp
            confidence = Math.max(0, Math.min(1, confidence));

            Point[] points = new Point[4];
            rect.points(points);

            // Determine orientation angle in original scale
            double angle;
            double AB = distance(points[0], points[1]);
            double BC = distance(points[1], points[2]);
            if (AB > BC) {
                if (points[0].x == points[1].x) {
                    angle = 90;
                } else {
                    angle = Math.atan((points[1].y - points[0].y) / (points[1].x - points[0].x)) * 180 / Math.PI;
                }
            } else {
                if (points[1].x == points[2].x) {
                    angle = 90;
                } else {
                    angle = Math.atan((points[2].y - points[1].y) / (points[2].x - points[1].x))  * 180 / Math.PI;
                }
            }

            rect.angle = angle;
            detectionResults.add(new DetectionResult(rect, confidence, points, pickupable));
        }

        // 8) Sort descending by confidence
        detectionResults.sort(Comparator.comparing((DetectionResult dr) -> !dr.pickupable).thenComparing((DetectionResult dr) -> -dr.confidence));


        if (detectionResults.isEmpty()) {
            bestDetectionResult = null;
        } else {
            bestDetectionResult = detectionResults.get(0);
        }

        if (mode == Mode.PROD){
            // 9) Draw top 5 on the original canvas
            int limit = Math.min(detectionResults.size(), 5);
            for (int i = 0; i < limit; i++) {
                DetectionResult result = detectionResults.get(i);
                RotatedRect scaledRect = result.rect;
                double confidence = result.confidence;

                // Scale the RotatedRect to original coordinates for drawing
                RotatedRect rectInOriginal = scaleRotatedRect(scaledRect, 1.0 / SCALE_FACTOR);

                // Draw bounding box
                Point[] points = new Point[4];
                rectInOriginal.points(points);

                MatOfPoint box = new MatOfPoint(points);
                List<MatOfPoint> boxList = new ArrayList<>();
                boxList.add(box);
                Imgproc.drawContours(canvas, boxList, 0, new Scalar(0,255,0), 2);




                // If width < height, adjust angle by +90
//                if (rectInOriginal.size.width < rectInOriginal.size.height) {
//                    angle = 90 + angle;
//                }

                // Draw orientation line
                Point center = rectInOriginal.center;
                int length = 50;
                double radians = Math.toRadians(rectInOriginal.angle);
                Point end = new Point(
                        center.x + length * Math.cos(radians),
                        center.y + length * Math.sin(radians)
                );
                Imgproc.line(canvas, center, end, new Scalar(255,0,0), 2);

            }
        }

        // Return the annotated original image
        return canvas;
    }

    public double distance(Point a, Point b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    public static class DetectionResultScaledData {
        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getTheta() {
            return theta;
        }

        public double getConfidence() {
            return confidence;
        }

        double x, y; // coordinates of center of rotated rectangle
        double theta; // angle of rotated rectangle
        double confidence; // confidence of detection

        public Point[] points;
        public DetectionResultScaledData(DetectionResult dr) {
            RotatedRect r = scaleRotatedRect(dr.rect, 1.0 / SCALE_FACTOR);
            this.x = r.center.x;
            this.y = r.center.y;
            this.theta = r.angle;
            this.confidence = dr.confidence;
//            this.points = dr.points;
        }

        public DetectionResultScaledData(double x, double y, double theta, double confidence) {
            this.x = x;
            this.y = y;
            this.theta = theta;
            this.confidence = confidence;
        }
    }

    // Returns the coordinates and angle (scaled to original size) of the best detected bounding box
    public DetectionResultScaledData bestDetectionCoordsAngle() {
        if (bestDetectionResult == null) {
            return new DetectionResultScaledData(-1, -1, -1, -1);
        }
        return new DetectionResultScaledData(bestDetectionResult);
    }

    @Override
    public void init(Mat input) {
    }

    /**
     * Scale a RotatedRect from one coordinate space to another.
     * E.g. if scale=2.0, it doubles the coordinates.
     */
    private static RotatedRect scaleRotatedRect(RotatedRect rr, double scale) {
        // Scale center
        Point center = new Point(rr.center.x * scale, rr.center.y * scale);
        // Scale size
        Size size = new Size(rr.size.width * scale, rr.size.height * scale);
        // Angle stays the same
        return new RotatedRect(center, size, rr.angle);
    }

    /**
     * Get the list of detected orientation vectors (each with a confidence value).
     * Sorted by descending confidence in the last frame processed.
     */
    public List<DetectionResult> getDetectionResults() {
        return detectionResults;
    }
}