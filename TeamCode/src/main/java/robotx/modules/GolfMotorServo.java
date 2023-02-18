package robotx.modules;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import robotx.libraries.XModule;

public class GolfMotorServo extends XModule {

    DcMotor liftMotor;
    DcMotor dropperMotor;

    Servo liftServo;
    Servo dropperServo;

    double power = .7;
    double power2 = .4;

    public boolean slowMode = false;
    boolean closed2 = true;
    boolean closed = true;

    public GolfMotorServo (OpMode op){
        super(op);
    }

    public void init() {
        liftMotor = opMode.hardwareMap.dcMotor.get("pickup");
        dropperMotor = opMode.hardwareMap.dcMotor.get("putter");

        liftServo = opMode.hardwareMap.servo.get("pickupServo");
        dropperServo = opMode.hardwareMap.servo.get("putterServo");
    }

    public void toggleSlow(){
        if (slowMode){
            slowMode = false;
        }

        else {
            slowMode = true;
        }
    }
    public void LiftServo() {
        if (!closed) {
            liftServo.setPosition(1);
            closed = true;
        } else {
            liftServo.setPosition(0.7);
            closed = false;
        }
    }
    public void DropperServo() {
        if (!closed2) {
            dropperServo.setPosition(0.5);
            closed2 = true;
        } else {
            dropperServo.setPosition(0.07);
            closed2 = false;
        }
    }
    public void loop() {
        if (xGamepad2().start.wasPressed()){
            toggleSlow();
        }

        if (xGamepad2().x.isDown()) {
            if (slowMode){
                liftMotor.setPower(power2 / 2);
            }
            else {
                liftMotor.setPower(power2);
            }
        }

        else if (xGamepad2().y.isDown()) {
            if (slowMode){
                liftMotor.setPower(-power2 / 2);
            }
            else {
                liftMotor.setPower(-power2);
            }
        }

        else {
            liftMotor.setPower(0.0);
        }

        if (xGamepad2().a.isDown()) {
            dropperMotor.setPower(power);
        }

        else if (xGamepad2().b.isDown()) {
            dropperMotor.setPower(-power);
        }

        else {
            dropperMotor.setPower(0.0);
        }

        if (xGamepad2().right_stick_button.wasPressed()){
            LiftServo();
        }
        if (xGamepad2().right_bumper.wasPressed()){
            DropperServo();
        }
    }
}
