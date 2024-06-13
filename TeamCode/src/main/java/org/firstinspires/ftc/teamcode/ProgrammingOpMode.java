package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
@TeleOp(name="Programming OpMode")
public class ProgrammingOpMode extends ManualControlOpMode {
    ArrayList<Double> LY1arr = new ArrayList<>();
    ArrayList<Double> LX1arr = new ArrayList<>();
    ArrayList<Double> RX1arr = new ArrayList<>();
    ArrayList<Boolean> A2arr = new ArrayList<>();
    ArrayList<Double> LY2arr = new ArrayList<>();
    ArrayList<ArrayList> mainArr = new ArrayList<>();
    ArrayList<Double> voltArr = new ArrayList<>();
    BufferedOutputStream bos;
    ObjectOutputStream oos;
    @Override
    public void runner() {
        CurvedGamepad cgp = new CurvedGamepad(gamepad1);
        double start = System.currentTimeMillis();
        double lx1 = cgp.lx;
        double ly1 = cgp.ly;
        double rx1 = cgp.rx;
        boolean a2 = gamepad2.a;
        double ly2 = gamepad2.left_stick_y;

        programRunner(lx1, ly1, rx1, a2, ly2);
        while (System.currentTimeMillis() - start < 10) {} // Making sure the delay is EXACTLY 100 ms

        LY1arr.add(ly1);
        LX1arr.add(lx1);
        RX1arr.add(rx1);
        A2arr.add(a2);
        LY2arr.add(ly2);
        voltArr.add(voltageSensor.getVoltage());

        /*
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         */

        if (gamepad1.share && (gamepad1.a || gamepad1.b) && (gamepad1.dpad_left || gamepad1.dpad_up || gamepad1.dpad_right || gamepad1.dpad_down)) {
            mainArr.add(LY1arr);
            mainArr.add(LX1arr);
            mainArr.add(RX1arr);
            mainArr.add(A2arr);
            mainArr.add(LY2arr);
            mainArr.add(voltArr);

            String name = "";
            String ending = "";

            if (gamepad1.a) name = "blue";
            if (gamepad1.b) name = "red";

            if (gamepad1.dpad_left) ending = "left";
            if (gamepad1.dpad_up) ending = "center";
            if (gamepad1.dpad_right) ending = "right";

            try {
                bos = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/controller_data.ser." + name + "." + ending));
                oos = new ObjectOutputStream(bos);
                oos.writeObject(mainArr);
                oos.close();
                LLiftMotor.setTargetPosition(0);
                RLiftMotor.setTargetPosition(0);
                sleep(1000);
                requestOpModeStop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
