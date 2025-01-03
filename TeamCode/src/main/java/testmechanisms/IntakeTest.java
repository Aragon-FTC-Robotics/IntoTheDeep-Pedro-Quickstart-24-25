package testmechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class IntakeTest {
    DcMotor intake;
    public static double POWER = 0.00;
    public void init(HardwareMap hm) {
        intake = hm.get(DcMotor.class, "intake");
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void Loop() {
        intake.setPower(POWER);
    }
}
