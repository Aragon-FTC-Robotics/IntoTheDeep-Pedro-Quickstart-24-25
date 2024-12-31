package mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class IntakeWrist {
    private Servo intakeWrist;
    public enum intakeWristState {IN, OUT};
    public intakeWristState currentState = intakeWristState.IN;
    public final double IN = 0.000;
    public final double OUT = 0.890;
    public void init(HardwareMap hm) {
        intakeWrist = hm.get(Servo.class, "intakeWrist");
    }
    public void Loop() {
        switch (currentState){
            case IN:
                setPosition(IN);
                break;
            case OUT:
                setPosition(OUT);
                break;
        }
    }
    private void setPosition(double pos) {
        intakeWrist.setPosition(pos);
    }
    public void setState(intakeWristState state) {
        currentState = state;
    }
    public String getState() {
        return currentState.name();
    }
}
