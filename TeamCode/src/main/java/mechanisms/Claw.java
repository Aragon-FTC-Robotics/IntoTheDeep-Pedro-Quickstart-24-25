package mechanisms;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private Servo claw;
    public enum ClawState {CLOSE, OPEN}
    public ClawState currentState = ClawState.CLOSE;

    public final double OPEN = 0.737;
    public final double CLOSE = 0.851;

    public void init(HardwareMap hm) {
        claw = hm.get(Servo.class, "claw");
    }

    public void Loop() {
        switch(currentState) {
            case OPEN:
                setPos(OPEN);
                break;
            case CLOSE:
                setPos(CLOSE);
                break;
            default:
                setPos(OPEN);
                break;
        }
    }


    private void setPos(double pos) {
        claw.setPosition(pos);
    }

    public void setState (ClawState state) {
        currentState = state;
    }

    public String getState(){
        return currentState.name();
    }
}
