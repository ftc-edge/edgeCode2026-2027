package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.baseMovement;

@TeleOp
public class teleop extends OpMode {
    float isIntaking = 1;
    float reverseFactor = 1;
    String warningString = "";
    baseMovement movement = new baseMovement();
    DcMotor intake;

    @Override
    public void init() {
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        movement.init();
    }

    @Override
    public void loop() {
        toggleCheck();
        intake.setPower(isIntaking * reverseFactor);

        telemetry.addLine(warningString);
        telemetry.update();
    }

    private void toggleCheck(){
        if(gamepad1.left_bumper) {
            if(isIntaking == 0) isIntaking = 1;
            else{
                isIntaking = 0;
            }
        }
        if(gamepad1.right_bumper){
            reverseFactor *= -1;
        }

        if(isIntaking == 1 && reverseFactor == 1) warningString =
                "=========================\n" +
                ">>> INTAKE IS FORWARD >>>\n" +
                "=========================\n";
        else if(isIntaking == 1 && reverseFactor == -1) warningString =
                "=========================\n" +
                "<<< INTAKE IS REVERSE <<<\n" +
                "=========================\n";
        else warningString =
                "=========================\n" +
                "||| INTAKE IS STOPPED |||\n" +
                "=========================\n";
    }
}
