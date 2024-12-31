package testmechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
@Config
public class ClawTest {
    Servo claw;
    public static double POS = 0.737;
    public void init(HardwareMap hm) {
        claw = hm.get(Servo.class, "claw");
    }
    public void Loop() {
        claw.setPosition(POS);
    }
}
