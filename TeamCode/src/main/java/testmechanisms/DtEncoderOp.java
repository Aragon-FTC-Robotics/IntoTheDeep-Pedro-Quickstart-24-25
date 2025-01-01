package testmechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "Dt encoder test", group = "testing")
public class DtEncoderOp extends OpMode {
    DcMotor a,b,c,d;

    @Override
    public void init(){
        a = hardwareMap.get(DcMotor.class, "rightFront");
        b = hardwareMap.get(DcMotor.class, "leftFront");
        c = hardwareMap.get(DcMotor.class, "rightBack");
        d = hardwareMap.get(DcMotor.class, "leftBack");
    }
    @Override
    public void loop() {
        DcMotor[] motors = {a, b, c, d};
        String[] labels = {"rightFront", "leftFront", "rightBack", "leftBack"};

        for (int i = 0; i < motors.length; i++) {
            telemetry.addData(labels[i] + motors[i].getPortNumber() +" Posiiton", motors[i].getCurrentPosition());
        }
        telemetry.update();
    }
    
}
