package testmechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
@Config
public class BarTest {
    Servo bar;
    public static double POS = 0.723;
    public void init(HardwareMap hm) {
        bar = hm.get(Servo.class, "bar");
    }
    public void Loop() {
        bar.setPosition(POS);
    }
}
