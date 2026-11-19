package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.baseMovement;

@TeleOp
public class teleop extends OpMode {

    public static float StartX;
    public static float StartY;
    public static float StartHead;
    public static float[] leftGoalPos = new float[]{0, 0};
    public static float[] rightGoalPos = new float[]{0, 0};
    public static float[] targetGoalPos;


    float isIntaking = 1;
    float reverseFactor = 1;
    String warningString = "";
    baseMovement movement = new baseMovement();
    DcMotor intake;
    DcMotor flywheel;
    DcMotor FRmotor;
    DcMotor FLmotor;
    DcMotor BRmotor;
    DcMotor BLmotor;

    Servo gate;


    float targetSpeed = 1f;
    final float targetIncrement = 0.005f;
    ElapsedTime openTimer = new ElapsedTime();
    final float maxOpenTime = 1f;

    Gamepad prevGamepad1 = new Gamepad();

    GoBildaPinpointDriver imu;

    @Override
    public void init() {
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        FRmotor = hardwareMap.get(DcMotor.class, "FRmotor");
        FRmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FRmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FRmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRmotor.setDirection(DcMotorSimple.Direction.FORWARD);


        FLmotor = hardwareMap.get(DcMotor.class, "FLmotor");
        FLmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FLmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FLmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FLmotor.setDirection(DcMotorSimple.Direction.FORWARD);

        BRmotor = hardwareMap.get(DcMotor.class, "BRmotor");
        BRmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BRmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BRmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRmotor.setDirection(DcMotorSimple.Direction.FORWARD);

        BLmotor = hardwareMap.get(DcMotor.class, "BLmotor");
        BLmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BLmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BLmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLmotor.setDirection(DcMotorSimple.Direction.FORWARD);

        gate = hardwareMap.get(Servo.class, "gate");


        imu = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        Pose2D startPose = new Pose2D(DistanceUnit.INCH, StartX, StartY, AngleUnit.DEGREES, StartHead);
        imu.setPosition(startPose);

        targetGoalPos = rightGoalPos;
    }

    @Override
    public void loop() {
        toggleCheck();
            intake.setPower(isIntaking * reverseFactor);
        move();
        imu.update();
        turretSpin();
        manualFlywheel();

        if(gate.getPosition() >= 0.9){
                warningString +=
                "///    GATE IS OPEN    ///\n" +
                "=========================";
            if(openTimer.seconds() > maxOpenTime){
                openTimer.reset();
                gate.setPosition(0);
            }
        }

        else if(gate.getPosition() <= 0.1) warningString +=
                "|||   GATE IS CLOSED  |||\n" +
                "=========================";
        else warningString +=
                "--- GATE IS SWITCHING ---\n" +
                "=========================";
        telemetry.addLine(warningString);
        telemetry.addLine(String.valueOf(flywheel.getPower()));

        telemetry.update();

        prevGamepad1.copy(gamepad1);
    }

    private void toggleCheck(){
        if(gamepad1.left_bumper && !prevGamepad1.left_bumper) {
            if(isIntaking == 0) isIntaking = 1;
            else{
                isIntaking = 0;
            }
        }
        if(gamepad1.right_bumper && !prevGamepad1.right_bumper){
            reverseFactor *= -1;
        }

        if(isIntaking == 1 && reverseFactor == 1) warningString =
                "=========================\n" +
                ">>> INTAKE IS FORWARD >>>\n" +
                "=========================";
        else if(isIntaking == 1 && reverseFactor == -1) warningString =
                "=========================\n" +
                "<<< INTAKE IS REVERSE <<<\n" +
                "=========================";
        else warningString =
                "=========================\n" +
                "||| INTAKE IS STOPPED |||\n" +
                "=========================";
    }

    void manualFlywheel(){
        targetSpeed += targetIncrement * gamepad1.right_trigger;
        targetSpeed -= targetIncrement * gamepad1.left_trigger;
        if(targetSpeed > 1) targetSpeed = 1;
        if(targetSpeed < 0) targetSpeed = 0;
        flywheel.setPower(targetSpeed);

        if(gamepad1.a){
            gate.setPosition(1);
        }
        if(gamepad1.b){
            gate.setPosition(0);
        }
    }

    void move(){
        float forward = gamepad1.left_stick_y;
        float strafe = gamepad1.left_stick_x;
        float turn = gamepad1.right_stick_x;

        FRmotor.setPower(forward + strafe + turn);
        FLmotor.setPower(forward - strafe - turn);
        BRmotor.setPower(forward - strafe + turn);
        BLmotor.setPower(forward + strafe - turn);
    }

    void turretSpin(){
        if(gamepad1.dpad_left) {
            targetGoalPos = leftGoalPos;
        }else if(gamepad1.dpad_right){
            targetGoalPos = rightGoalPos;
        }

        double xDif = targetGoalPos[0] - imu.getPosX(DistanceUnit.INCH);
        double yDif = targetGoalPos[1] - imu.getPosY(DistanceUnit.INCH);

        double angleOffset = Math.tan(xDif/yDif) * Math.pow((Math.pow(xDif, 2) + Math.pow(yDif, 2)), 0.5) - imu.getHeading(AngleUnit.DEGREES);

//        double turretSet = turret.getPosition() + (angleOffset/360);
//        if(turretSet > 1){
//            turretSet--;
//        }
//        turret.setPosition(turretSet);
    }
}
