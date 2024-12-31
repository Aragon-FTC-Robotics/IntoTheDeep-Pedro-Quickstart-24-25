package testmechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
@Config
public class IntakeWristTest {
    Servo intakeWrist;
    public static double POS = 0; // This variable is modified by FTCDashboard
    public void init(HardwareMap hm) {
        intakeWrist = hm.get(Servo.class, "intakeWrist");
    }
    public void Loop() {
        intakeWrist.setPosition(POS);
    }
}
