package mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Bar {
    private Servo barServoRight;
    public enum BarState {TRANSFER, WALL, BUCKET, CLIP, NEUTRAL}
    public BarState currentState = BarState.NEUTRAL;

    public final double TRANSFER = 0.818;
    public final double WALL = 0.864;
    public final double BUCKET = 0.262;
    public final double CLIP = 0.403;
    public final double NEUTRAL = 0.723;

    public void init(HardwareMap hm) {
        barServoRight = hm.get(Servo.class, "bar");
        barServoRight.setDirection(Servo.Direction.REVERSE);
    }

    public void Loop() {
        switch(currentState) {
            case TRANSFER:
                setPos(TRANSFER);
                break;
            case BUCKET:
                setPos(BUCKET);
                break;
            case WALL:
                setPos(WALL);
                break;
            case CLIP:
                setPos(CLIP);
                break;
            case NEUTRAL:
                setPos(NEUTRAL);
                break;
            default:
                setPos(NEUTRAL);
                break;
        }
    }

    private void setPos(double pos) {
        barServoRight.setPosition(pos);
    }

    public void setState(BarState state) {
        this.currentState = state;
    }

    public String getState(){
        return currentState.name();
    }
}
