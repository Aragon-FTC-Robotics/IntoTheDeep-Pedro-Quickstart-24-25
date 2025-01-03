package testmechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class SlidesTest {
    private PIDController controller; //from arcrobotics
    private double pid, power;
    public static double p=0.0055, i=0, d=0.0001, f=0;
    private DcMotorEx slideLeft;
    private DcMotorEx slideRight;
    public static int targetPos = 0;
    public void init(HardwareMap hm) {
        controller = new PIDController(p,i,d);
        slideLeft = hm.get(DcMotorEx.class, "slideLeft");
        slideRight = hm.get(DcMotorEx.class, "slideRight");

        slideLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        slideRight.setDirection(DcMotorSimple.Direction.FORWARD);


        slideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void Loop() {
        controller.setPID(p,i,d);
        pid = controller.calculate(slideLeft.getCurrentPosition(), targetPos);
        double ff = f;
        power = pid + ff;
        slideLeft.setPower(power);
        slideRight.setPower(power);
    }
    public int getTargetPos() {return targetPos;}
    public int getLPos() {return slideLeft.getCurrentPosition();}
    public int getRPos() {return slideRight.getCurrentPosition();}
}
