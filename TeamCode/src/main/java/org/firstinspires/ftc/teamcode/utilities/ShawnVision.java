package org.firstinspires.ftc.teamcode.utilities;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.pipelines.TestPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class ShawnVision {
    OpenCvCamera camera;
    public TestPipeline pipeline;
    Telemetry telemetry;

    public ShawnVision(HardwareMap hmap, Telemetry telemetry, String color) {
        int cameraMonitorViewId = hmap.appContext.getResources().getIdentifier("cameraMonitorViewId",
                "id", hmap.appContext.getPackageName());

        WebcamName webcamName = hmap.get(WebcamName.class, "Webcam 1");
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        pipeline = new TestPipeline(telemetry);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
                FtcDashboard.getInstance().startCameraStream(camera, 0);
                camera.setPipeline(pipeline);
            }

            @Override
            public void onError(int errorCode) {
            }
        });
    }

    public ShawnVision(HardwareMap hmap, Telemetry telemetry){
        this(hmap, telemetry, "red");
    }

    public int getPosition(){
//        TestPipeline.Locations location = pipeline.getLocation();
        telemetry.addLine("Vision failed -- auto returning 1");
        return 1;
    }
}