package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

class CurvedGamepad extends Gamepad {
    public double lx;
    public double ly;
    public double rx;

    final double CURVE = 2.5;
    final double TURN_MAX = 0.75;

    public CurvedGamepad(Gamepad gamepad) {
        this.lx = gamepad.left_stick_x < 0 ? -(Math.pow(-gamepad.left_stick_x, CURVE)) : Math.pow(gamepad.left_stick_x, CURVE);
        this.ly = gamepad.left_stick_y < 0 ? -(Math.pow(-gamepad.left_stick_y, CURVE)) : Math.pow(gamepad.left_stick_y, CURVE);
        this.rx = (gamepad.right_stick_x < 0 ? -(Math.pow(-gamepad.right_stick_x, CURVE)) : Math.pow(gamepad.right_stick_x, CURVE)) * TURN_MAX;
    }
}

@TeleOp(name = "Driving OpMode")
public class ManualControlOpMode extends CommonOpMode {
    
    int lPos = 0;
    int tick = 0;
    @Override
    public void runner() {
        CurvedGamepad cgp = new CurvedGamepad(gamepad1);
        CurvedGamepad cgp2 = new CurvedGamepad(gamepad2);

        LLiftMotor.setPower(1);
        RLiftMotor.setPower(1);

        double denominator = Math.max(Math.abs(cgp.ly) + Math.abs(cgp.lx) + Math.abs(cgp.rx), 1);

        leftFrontMotor.setPower((cgp.ly + cgp.lx + cgp.rx)/denominator);
        leftBackMotor.setPower((cgp.ly - cgp.lx + cgp.rx)/denominator);
        rightFrontMotor.setPower((cgp.ly + cgp.lx - cgp.rx)/denominator);
        rightBackMotor.setPower((cgp.ly - cgp.lx - cgp.rx)/denominator);

        lPos += (int)(gamepad2.left_stick_y * 30);

        if (lPos <-6000)
            lPos = -6000;
        else if (lPos > -150) {
            LLiftMotor.setPower(0.2);
            RLiftMotor.setPower(0.2);
            lPos = -150;
        }

        telemetry.addData("lpos", lPos);
        telemetry.addData("DPad", cgp2.dpad_up || cgp2.dpad_down);
        telemetry.addData("gly2", gamepad2.left_stick_y);

        if (LLiftMotor.getCurrentPosition() <= -2000) // more negative: more height
            servoPos = 0.5; // higher the value: more swing to back
        else
            servoPos = 0.065;

        bigArm.setPosition(servoPos);

        if (gamepad2.left_bumper && gamepad2.right_bumper)
            droneLauncherServo.setPosition(1);
        if (gamepad2.a) {
            gripperServo.setPosition(0.4);
            if (lPos == -150)
                lPos = 0;
            if (lPos == 0)
                tick = 1;
        }
        else
            gripperServo.setPosition(0);

        if (tick >= 1 && tick <20){
            lPos = 0;
            tick++;
        }
        else
            tick = 0;

        LLiftMotor.setTargetPosition(lPos);
        RLiftMotor.setTargetPosition(lPos);
    }
    public void programRunner(double lx1, double ly1, double rx1, boolean a2, double ly2) {
        // Don't judge me riley
        LLiftMotor.setPower(1);
        RLiftMotor.setPower(1);

        double denominator = Math.max(Math.abs(ly1) + Math.abs(lx1) + Math.abs(rx1), 1);

        leftFrontMotor.setPower((ly1 + lx1 + rx1)/denominator);
        leftBackMotor.setPower((ly1 - lx1 + rx1)/denominator);
        rightFrontMotor.setPower((ly1 + lx1 - rx1)/denominator);
        rightBackMotor.setPower((ly1 - lx1 - rx1)/denominator);

        lPos += (int)(ly2 * 30);

        if (lPos < -6000)
            lPos = -6000;
        else if (lPos > -150) {
            LLiftMotor.setPower(0.05);
            RLiftMotor.setPower(0.05);
            lPos = -150;
        }

        if (LLiftMotor.getCurrentPosition() <= -2000)
            servoPos = 0.4; // the lower the value, the further it goes out
        else
            servoPos = 0.065;

        bigArm.setPosition(servoPos);

        if (a2) {
            gripperServo.setPosition(0.4);
            if (lPos == -150) {
                LLiftMotor.setPower(0.2);
                RLiftMotor.setPower(0.2);
                lPos = 0;
            }
        } else
            gripperServo.setPosition(0);


        LLiftMotor.setTargetPosition(lPos);
        RLiftMotor.setTargetPosition(lPos);
    }
    public void replayInit() {}
}
