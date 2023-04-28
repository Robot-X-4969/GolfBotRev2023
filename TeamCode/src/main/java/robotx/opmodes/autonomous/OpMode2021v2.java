package robotx.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.libraries.XOpMode;
import robotx.modules.GolfMotorServo;
import robotx.modules.MecanumTeleop;
import robotx.modules.MecanumDrive;

@TeleOp(name = "GolfBotOpMode", group = "Default")

public class OpMode2021v2 extends XOpMode {

    GolfMotorServo golfMotorServo;
    MecanumTeleop mecanumTeleop;
    MecanumDrive mecanumDrive;

    public void initModules() {                                                                 

        super.initModules();

        golfMotorServo = new GolfMotorServo(this);
        activeModules.add(golfMotorServo);

        mecanumTeleop = new MecanumTeleop(this);
        activeModules.add(mecanumTeleop);


        mecanumDrive = new MecanumDrive(this);
        activeModules.add(mecanumDrive);

    }

    public void init() {
        super.init();
    }
}

/*
Controls
GamePad 1:
Joysticks to move
B to reset robot orientation
Left bumper to toggle slow mode
Right bumper to toggle super slow mode

Gamepad 2:
intake x and y
lift a and b
duck motor right back and left back


}*/