package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public abstract class CommonOpMode extends LinearOpMode {
    protected DcMotor leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor;

    protected DcMotor LLiftMotor, RLiftMotor;
    protected Servo bigArm, gripperServo, droneLauncherServo;
    protected VoltageSensor voltageSensor;

    protected double servoPos = 0;
    //OpenCvCamera camera;

    @Override
    public void runOpMode() throws InterruptedException {
        //int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        replayInit();

        waitForStart();

        leftFrontMotor = hardwareMap.get(DcMotor.class, "leftFrontMotor");
        leftBackMotor = hardwareMap.get(DcMotor.class, "leftBackMotor");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "rightFrontMotor");
        rightBackMotor = hardwareMap.get(DcMotor.class, "rightBackMotor");
        LLiftMotor = hardwareMap.get(DcMotor.class, "LLiftMotor");
        RLiftMotor = hardwareMap.get(DcMotor.class, "RLiftMotor");
        bigArm = hardwareMap.get(Servo.class, "bigArm");
        gripperServo = hardwareMap.get(Servo.class, "gripperServo");
        droneLauncherServo = hardwareMap.get(Servo.class, "droneLauncherServo");

        voltageSensor = hardwareMap.voltageSensor.iterator().next();
        
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);
        RLiftMotor.setDirection(DcMotor.Direction.REVERSE);
        bigArm.setDirection(Servo.Direction.REVERSE);

        LLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        LLiftMotor.setTargetPosition(0);
        RLiftMotor.setTargetPosition(0);

        LLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        LLiftMotor.setPower(1);
        RLiftMotor.setPower(1);

        gripperServo.setPosition(0);
        droneLauncherServo.setPosition(0);

        gamepad1.reset();
        gamepad1.rumble(500);

        while(opModeIsActive()) {
            double start = System.currentTimeMillis();

            telemetry.addData("Voltage", voltageSensor.getVoltage());

            telemetry.update();

            runner();
            //while (System.currentTimeMillis() - start < 100) {}
        }
    }

    protected abstract void runner();
    protected abstract void replayInit();
}
