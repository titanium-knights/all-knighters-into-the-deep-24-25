package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline;
import org.firstinspires.ftc.teamcode.teleop.Teleop;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;
//import org.firstinspires.ftc.teamcode.pipelines.ConfidenceOrientationVectorPipeline.DetectionResultScaledData;

@Config
public class Webcam {
    OpenCvWebcam cam;
    ConfidenceOrientationVectorPipeline pipeline; // daniel plainview would be proud
    int cameraMonitorViewId;

    public static int stream = 0;

    Teleop.Strategy strategy;
    public Webcam(HardwareMap hmap, ConfidenceOrientationVectorPipeline.Color color, Teleop.Strategy strategy) {
        this.cameraMonitorViewId = hmap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hmap.appContext.getPackageName());
        this.cam = OpenCvCameraFactory.getInstance().createWebcam(hmap.get(WebcamName.class, CONFIG.webcam), cameraMonitorViewId);
        this.pipeline = new ConfidenceOrientationVectorPipeline(color);
        if (stream == 1) FtcDashboard.getInstance().startCameraStream(cam, 0);

        cam.setPipeline(pipeline);
        cam.setMillisecondsPermissionTimeout(5000);

        cam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                /*
                 * Tell the webcam to start streaming images to us! Note that you must make sure
                 * the resolution you specify is supported by the camera. If it is not, an exception
                 * will be thrown.
                 *
                 * Keep in mind that the SDK's UVC driver (what OpenCvWebcam uses under the hood) only
                 * supports streaming from the webcam in the uncompressed YUV image format. This means
                 * that the maximum resolution you can stream at and still get up to 30FPS is 480p (640x480).
                 * Streaming at e.g. 720p will limit you to up to 10FPS and so on and so forth.
                 *
                 * Also, we specify the rotation that the webcam is used in. This is so that the image
                 * from the camera sensor can be rotated such that it is always displayed with the image upright.
                 * For a front facing camera, rotation is defined assuming the user is looking at the screen.
                 * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
                 * away from the user.
                 */
                cam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });
    }

    public ConfidenceOrientationVectorPipeline.DetectionResultScaledData bestDetectionCoordsAngle() {
        // casting this cause i mean generalizability is always sweet
        return ((ConfidenceOrientationVectorPipeline)pipeline).bestDetectionCoordsAngle();
    }

    public double getFps() {
        return cam.getFps();
    }

    public void setStrategy(Teleop.Strategy strategy){
        pipeline.setStrategy(strategy);
    }
}
