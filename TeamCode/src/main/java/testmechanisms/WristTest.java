package testmechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class WristTest {
    Servo wrist;
    public static double POS = 0.000;

    public void init(HardwareMap hm) {
        wrist = hm.get(Servo.class, "wrist");
    }

    public void Loop() {
        wrist.setPosition(POS);
    }
}
