package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

// Moving:
@TeleOp
public class JustMoveCodeCC extends OpMode {
    DcMotor FRmotor;
    DcMotor FLmotor;
    DcMotor BRmotor;
    DcMotor BLmotor;
    DcMotor flywheel;
    DcMotor intakeMotor;
    CRServo intakeServo;
    private static final double SHOOT_POWER = 1.0; // Tune from 0.0 to 1.0 on the robot.

    float LeftStick_x;
    float LeftStick_y;
    float RightStick_x;



        boolean Up;
    boolean A;
    boolean B;
    boolean Left;
    boolean Right;
    boolean Right_Bumper;

    public void init(){
        FRmotor = hardwareMap.get(DcMotor.class, "FRmotor");
        FRmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FRmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FRmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRmotor.setDirection(DcMotorSimple.Direction.REVERSE);


        FLmotor = hardwareMap.get(DcMotor.class, "FLmotor");
        FLmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FLmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FLmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FLmotor.setDirection(DcMotorSimple.Direction.FORWARD);

        BRmotor = hardwareMap.get(DcMotor.class, "BRmotor");
        BRmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BRmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BRmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRmotor.setDirection(DcMotorSimple.Direction.REVERSE);

        BLmotor = hardwareMap.get(DcMotor.class, "BLmotor");
        BLmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BLmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BLmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLmotor.setDirection(DcMotorSimple.Direction.FORWARD);

        CRServo intakeServo = hardwareMap.get(CRServo.class, "intakeServo");

        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        flywheel.setPower(0.0);
        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);


        float LeftStick_x = gamepad1.left_stick_x;
        float LeftStick_y = gamepad1.left_stick_y;
        float RightStick_x = gamepad1.right_stick_x;

        /*
        I see what you're thinking with this, but this would make the robot not use its motors. Computers don't use logic,
        they just use math. What you're trying to do here is make these variables a stand in for the gamepad variation.
        What would actually happen is that each of these would get set to 0 forever. Any time "=" or "==" is used, it's
        equal to the VALUE OF the variable on the other side of the sign, not the variable itself. Does that make sense?
        I personally avoid these kinds of variables since I think they're redundant, but it's up to yall if you want to use them.
        Just be careful about these kinds of things, two variables that are supposed to represent the same thing can get tricky.
         */
        float LeftTrigger = gamepad1.left_trigger;
        boolean A = gamepad1.a;
        boolean B = gamepad1.b;
        boolean Up = gamepad1.dpad_up;
        boolean Left = gamepad1.dpad_left;
        boolean right = gamepad1.dpad_right;
        boolean Right_Bumber = gamepad1.right_bumper;

    }

    public void Drive(){
        FLmotor.setPower(LeftStick_x + LeftStick_y + RightStick_x);
        FRmotor.setPower(-LeftStick_x + LeftStick_y - RightStick_x);
        BLmotor.setPower(LeftStick_x + LeftStick_y + RightStick_x);
        BRmotor.setPower(-LeftStick_x + LeftStick_y - RightStick_x);
    }

    public void shoot(){
        if (Right_Bumper) {
            flywheel.setPower(1.0);
            intakeServo.setPower(.25);
        } else {
            flywheel.setPower(0);
        }
    }
    public void Intake() {
        if (gamepad1.x) {
            intakeMotor.setPower(1.0);
            intakeServo.setPower(.25);
        } else {
            intakeMotor.setPower(0);
        }

        intakeMotor.setPower(0);
    }

    public void loop(){
        Drive();
        shoot();
        Intake();
    }

}
