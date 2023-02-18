/*package robotx.modules;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import robotx.libraries.OmniDriveSystem;

/**
 * Created by Adi on 4/20/2018.
 */

/*public class MecanumDrive extends OmniDriveSystem {
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public MecanumDrive(OpMode op) {
        super(op);
    }

    public void init(){
        frontLeft = opMode.hardwareMap.dcMotor.get("frontLeft");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);                //reverse is necessary: up on Y stick makes frontLeft counterclockwise and frontRight clockwise
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight = opMode.hardwareMap.dcMotor.get("frontRight");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft = opMode.hardwareMap.dcMotor.get("backLeft");
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight = opMode.hardwareMap.dcMotor.get("backRight");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void updateMotors(){
        opMode.telemetry.addData("frontRight", getYPower() - getXPower() - getRotationPower());
        frontRight.setPower(getYPower() + getXPower() - getRotationPower());

        opMode.telemetry.addData("frontLeft", getYPower() + getXPower() + getRotationPower());
        frontLeft.setPower(getYPower() - getXPower() + getRotationPower());                                         //frontLeft and backRight have the same "getYpower + getXpower"

        opMode.telemetry.addData("backRight", getYPower() + getXPower() - getRotationPower());
        backRight.setPower(getYPower() - getXPower() - getRotationPower());

        opMode.telemetry.addData("backLeft", getYPower() - getXPower() + getRotationPower());
        backLeft.setPower(getYPower() + getXPower() + getRotationPower());
    }
}*/

package robotx.modules;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import robotx.libraries.XModule;


public final class MecanumTeleop extends XModule{

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public boolean slowMode = false;


    public MecanumTeleop(OpMode op){
        super(op);
    }

    public void init(){
        //front left motor
        frontLeft = opMode.hardwareMap.dcMotor.get("frontLeft");
        // frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);            //reverse this; want "forward direction for robot" to be "positive values" /or/ "up on the y-stick"
        //when moving forward, the two front wheels spin in opposite directions (left is counter-clockwise, right is clockwise)
        //front right motor
        frontRight = opMode.hardwareMap.dcMotor.get("frontRight");
        // frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //back left motor
        backLeft = opMode.hardwareMap.dcMotor.get("backLeft");
        // backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);             //reverse for same reason as above

        //back right motor
        backRight = opMode.hardwareMap.dcMotor.get("backRight");
        //backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void toggleSlow(){
        if (slowMode){
            slowMode = false;
        }

        else {
            slowMode = true;
        }
    }

    public void loop(){

        if (xGamepad2().start.wasPressed()){
            toggleSlow();
        }
        double xPow = -1 * controlRamp( xGamepad1().left_stick_x);
        double yPow = controlRamp(xGamepad1().left_stick_y);
        double rotPow = -1 *  controlRamp(xGamepad1().right_stick_x); //again, negate because negative is right

        /* note
            For mechanum drive to strafe to the right, the wheels on the right turn outwards // wheels on left turn inwards
            For mechanum drive to strafe to left, the wheels on left turn outwards // wheels on right turn inwards
        */

        // another note: positive y value is the FORWARDS direction for all wheels, not "clockwise" or "counterclockwise"; reversed earlier

        double flPow = yPow - xPow + rotPow;
        frontLeft.setPower(flPow);
        //+ yPow -> if the control stick is held in the up direction, move this wheel forwards
        //note: can just keep using yPow for forwards for all four wheels even when they spin in opposite directions to move forward; reversed some motors earlier
        //- xPow -> if pressing stick to the right (positive value), want this to spin backwards
        //+ rotPow -> if want to rotate to right (positive value), want this to spin forwards

        double frPow = yPow + xPow - rotPow;
        frontRight.setPower(frPow);
        //+ yPow -> if control stick is up, want to move forwards
        //+ xPow -> if control stick is right (positive value), want to spin forwards
        //- rotPow -> if control stick is right (positive value), want to spin backwards

        //double blPow = .75 * (yPow + xPow + rotPow);
        double blPow = yPow + xPow + rotPow;
        backLeft.setPower(blPow);
        //+ yPow -> if control stick is up, want to move forwards
        //+ xPow -> if control stick is right (positive value), want to spin forwards
        //+ rotPow -> if control stick is right (positive value), want to spin forwards

        //double brPow = .75 * (yPow - xPow - rotPow);
        double brPow = yPow - xPow - rotPow;
        backRight.setPower(brPow);
        //+ yPow -> if control stick is up, want to move forwards
        //- xPow -> if control stick is right (positive value), want to spin backwards
        //- rotPow -> if control stick is right (positive value), want to spin backwards

        if (xGamepad2().dpad_down.isDown()) {
            if (slowMode) {
                frontLeft.setPower(0.3/2);
                frontRight.setPower(0.3/2);
                backLeft.setPower(0.3/2);
                backRight.setPower(0.3/2);
            }
            else  {
                frontLeft.setPower(0.5);
                frontRight.setPower(0.5);
                backLeft.setPower(0.5);
                backRight.setPower(0.5);
            }
        }
        if (xGamepad2().dpad_up.isDown()){
            if (slowMode) {
                frontLeft.setPower(-0.3/2);
                frontRight.setPower(-0.3/2);
                backLeft.setPower(-0.3/2);
                backRight.setPower(-0.3/2);
            }
            else {
                frontLeft.setPower(-0.5);
                frontRight.setPower(-0.5);
                backLeft.setPower(-0.5);
                backRight.setPower(-0.5);
            }
        }
        if (xGamepad2().dpad_left.isDown()){
            if (slowMode) {
                frontLeft.setPower(-0.25);
                frontRight.setPower(0.25);
                backLeft.setPower(0.25);
                backRight.setPower(-0.25);
            }
            else {
                frontLeft.setPower(0.5);
                frontRight.setPower(-0.5);
                backLeft.setPower(-0.5);
                backRight.setPower(0.5);
            }
        }
        if (xGamepad2().dpad_right.isDown()){
            if (slowMode) {
                frontLeft.setPower(0.25);
                frontRight.setPower(-0.25);
                backLeft.setPower(-0.25);
                backRight.setPower(0.25);
            }
            else {
                frontLeft.setPower(-0.5);
                frontRight.setPower(0.5);
                backLeft.setPower(0.5);
                backRight.setPower(-0.5);
            }
        }
    }

    public double controlRamp(double input) {

        double output = input;
        double coeff = 1.0;
        if (xGamepad1().right_bumper.isDown())
        {
            coeff = coeff / 2.0;
        }

        else if (xGamepad1().left_bumper.isDown())
        {
            coeff = coeff / 4.0;
        }

        else
        {
            coeff = coeff ;
        }

        return output * coeff;
    }

}