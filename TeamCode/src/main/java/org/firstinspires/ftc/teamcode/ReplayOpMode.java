package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class ReplayOpMode extends ManualControlOpMode {
    int index = 0;
    int tagID = 4;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    String ending = "";
    double fx = 1425.99;
    double fy = 1425.99;
    double cx = 631.634;
    double cy = 306.469;
    double tagsize = 0.508;
    ArrayList<Double> LY1arr = new ArrayList<>();
    ArrayList<Double> LX1arr = new ArrayList<>();
    ArrayList<Double> RX1arr = new ArrayList<>();
    ArrayList<Boolean> A2arr = new ArrayList<>();
    ArrayList<Double> LY2arr = new ArrayList<>();
    ArrayList<Double> voltArr = new ArrayList<>();
    ArrayList<ArrayList> mainArr = new ArrayList<>();

    public void replayInit() {
        /*
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(1280,720, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode)
            {}
        });

        while (!isStarted() && !isStopRequested()) {
            if (camera.getFps() != 0) {
                AprilTagDetection TagOfInterest = null;
                String oldEnding = ending;
                List<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();
                for (AprilTagDetection Tag : currentDetections) {
                    if (Tag.id == tagID) {
                        TagOfInterest = Tag;
                        if (Tag.pose.x <= 0)
                            ending = "center";
                        else
                            ending = "right";
                    }
                }
                if (TagOfInterest == null)
                    ending = "left";

                telemetry.addData("position", ending);
                if (TagOfInterest != null)
                    telemetry.addData("xPos", TagOfInterest.pose.x);
            } else
                telemetry.addLine("Camera is opening...");
            telemetry.update();

            sleep(20);
        }
        telemetry.addLine("Chose to go: " + ending);
        telemetry.update();
        camera.closeCameraDevice();
        */
        ending = "left";

        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/controller_data.ser." + suffix() + "." + ending)));
            mainArr = (ArrayList) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        LY1arr = mainArr.get(0);
        LX1arr = mainArr.get(1);
        RX1arr = mainArr.get(2);
        A2arr = mainArr.get(3);
        LY2arr = mainArr.get(4);
        voltArr = mainArr.get(5);
    }
    public void runner() {
        double start = System.currentTimeMillis();

        if (index == mainArr.get(0).size()) {
            LLiftMotor.setTargetPosition(0);
            RLiftMotor.setTargetPosition(0);
            sleep(1000);
            requestOpModeStop();
        }
        programRunner(LX1arr.get(index), LY1arr.get(index), RX1arr.get(index), A2arr.get(index), LY2arr.get(index));
        while (System.currentTimeMillis() - start < 10) {}
        /*
        double quotient = 1.5 * (voltArr.get(index)/voltageSensor.getVoltage());
        long millis = (long) quotient;
        int nanos = (int)(1E6 * (quotient - Math.floor(quotient)));

        telemetry.addData("millis", millis);
        telemetry.addData("nanos", nanos);

        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        index++;
    }

    public abstract String suffix();
}
