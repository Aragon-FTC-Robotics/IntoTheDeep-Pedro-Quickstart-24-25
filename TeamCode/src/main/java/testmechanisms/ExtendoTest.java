package testmechanisms;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ExtendoTest {
    private PIDController controller; //from arcrobotics
    private double pid, power;
    public static double p=0.010, i=0, d=0.0001, f=0;
    private DcMotorEx extendo;
    public static int targetPos = 0;
    public void init(HardwareMap hm) {
        controller = new PIDController(p,i,d);
        extendo = hm.get(DcMotorEx.class, "extendo");
        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void Loop() {
        controller.setPID(p,i,d);
        pid = controller.calculate(extendo.getCurrentPosition(), targetPos);
        double ff = f;
        power = pid + ff;
        extendo.setPower(power);
    }
    public int getTargetPos() {return targetPos;}
    public int getPos() {return extendo.getCurrentPosition();}
}
