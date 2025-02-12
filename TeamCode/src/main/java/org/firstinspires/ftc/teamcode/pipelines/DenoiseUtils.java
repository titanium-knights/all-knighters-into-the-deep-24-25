package org.firstinspires.ftc.teamcode.pipelines;

import org.opencv.core.Mat;

/**
 * Utility class for downscaling and fast box blurring in pure Java.
 */
public class DenoiseUtils {

    /**
     * Downscale an image by a given factor.
     * 
     * @param src          The source Mat (e.g. RGBA or RGB).
     * @param scaleFactor  The factor by which to shrink the image (e.g. 0.5 for half-size).
     * @return             A new Mat that is downscaled.
     */
    public static Mat downscale(Mat src, double scaleFactor) {
        int srcRows = src.rows();
        int srcCols = src.cols();
        int channels = src.channels();

        // Compute target size
        int dstRows = (int) Math.round(srcRows * scaleFactor);
        int dstCols = (int) Math.round(srcCols * scaleFactor);

        Mat dst = new Mat(dstRows, dstCols, src.type());

        // Get data arrays
        byte[] srcData = new byte[srcRows * srcCols * channels];
        src.get(0, 0, srcData);

        byte[] dstData = new byte[dstRows * dstCols * channels];

        // Simple nearest-neighbor downscale
        for (int y = 0; y < dstRows; y++) {
            for (int x = 0; x < dstCols; x++) {
                // Corresponding source pixel
                int srcY = (int) (y / scaleFactor);
                int srcX = (int) (x / scaleFactor);

                int srcIndex = (srcY * srcCols + srcX) * channels;
                int dstIndex = (y * dstCols + x) * channels;

                // Copy pixel
                for (int c = 0; c < channels; c++) {
                    dstData[dstIndex + c] = srcData[srcIndex + c];
                }
            }
        }

        dst.put(0, 0, dstData);
        return dst;
    }

    /**
     * Fast box blur using two 1D passes (horizontal then vertical).
     * This significantly speeds up compared to a naive 2D loop.
     * 
     * @param src     The source Mat (assumed 8UC3 or 8UC4, but method works similarly).
     * @param radius  The "radius" of the blur kernel. A radius of 1 => 3x3 kernel.
     * @return        A new Mat that is blurred.
     */
    public static Mat fastBoxBlur(Mat src, int radius) {
        // 1) Convert to array
        int rows = src.rows();
        int cols = src.cols();
        int channels = src.channels();

        byte[] srcData = new byte[rows * cols * channels];
        src.get(0, 0, srcData);

        // 2) Create intermediate and output arrays
        int size = rows * cols * channels;
        byte[] tmpData = new byte[size];
        byte[] dstData = new byte[size];

        // 3) Horizontal pass
        horizontalPass(srcData, tmpData, rows, cols, channels, radius);

        // 4) Vertical pass
        verticalPass(tmpData, dstData, rows, cols, channels, radius);

        // 5) Put data back into a Mat
        Mat dst = new Mat(rows, cols, src.type());
        dst.put(0, 0, dstData);
        return dst;
    }

    /**
     * Perform a horizontal blur pass using a sliding window.
     */
    private static void horizontalPass(byte[] srcData, byte[] dstData,
                                       int rows, int cols, int channels, int radius) {
        int windowSize = 2 * radius + 1;

        for (int y = 0; y < rows; y++) {
            // For each row, we do a sliding sum for each channel
            int rowStart = y * cols * channels;

            // Initialize sum for the first window
            int[] sum = new int[channels];
            for (int i = 0; i < windowSize && i < cols; i++) {
                int index = rowStart + i * channels;
                for (int c = 0; c < channels; c++) {
                    sum[c] += (srcData[index + c] & 0xFF);
                }
            }

            // First pixel in row
            int outIndex = rowStart;
            for (int c = 0; c < channels; c++) {
                dstData[outIndex + c] = (byte) ((sum[c] / windowSize) & 0xFF);
            }

            // Slide over the row
            for (int x = 1; x < cols; x++) {
                // Remove left pixel if it's outside the new window
                int leftPos = x - radius - 1;
                if (leftPos >= 0) {
                    int leftIndex = rowStart + leftPos * channels;
                    for (int c = 0; c < channels; c++) {
                        sum[c] -= (srcData[leftIndex + c] & 0xFF);
                    }
                }

                // Add the right pixel if it's inside the new window
                int rightPos = x + radius;
                if (rightPos < cols) {
                    int rightIndex = rowStart + rightPos * channels;
                    for (int c = 0; c < channels; c++) {
                        sum[c] += (srcData[rightIndex + c] & 0xFF);
                    }
                }

                // Write out average
                outIndex = rowStart + x * channels;
                for (int c = 0; c < channels; c++) {
                    dstData[outIndex + c] = (byte) ((sum[c] / windowSize) & 0xFF);
                }
            }
        }
    }

    /**
     * Perform a vertical blur pass using a sliding window.
     */
    private static void verticalPass(byte[] srcData, byte[] dstData,
                                     int rows, int cols, int channels, int radius) {
        int windowSize = 2 * radius + 1;

        // For each column
        for (int x = 0; x < cols; x++) {
            // We'll do a sliding sum for each channel
            int[] sum = new int[channels];

            // Initialize sum with the first window
            for (int i = 0; i < windowSize && i < rows; i++) {
                int index = (i * cols + x) * channels;
                for (int c = 0; c < channels; c++) {
                    sum[c] += (srcData[index + c] & 0xFF);
                }
            }

            // First pixel in column
            int outIndex = x * channels;
            for (int c = 0; c < channels; c++) {
                dstData[outIndex + c] = (byte) ((sum[c] / windowSize) & 0xFF);
            }

            // Slide down the column
            for (int y = 1; y < rows; y++) {
                int topPos = y - radius - 1;
                if (topPos >= 0) {
                    int topIndex = (topPos * cols + x) * channels;
                    for (int c = 0; c < channels; c++) {
                        sum[c] -= (srcData[topIndex + c] & 0xFF);
                    }
                }

                int bottomPos = y + radius;
                if (bottomPos < rows) {
                    int bottomIndex = (bottomPos * cols + x) * channels;
                    for (int c = 0; c < channels; c++) {
                        sum[c] += (srcData[bottomIndex + c] & 0xFF);
                    }
                }

                outIndex = (y * cols + x) * channels;
                for (int c = 0; c < channels; c++) {
                    dstData[outIndex + c] = (byte) ((sum[c] / windowSize) & 0xFF);
                }
            }
        }
    }
}
