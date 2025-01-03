package mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotor intake;
    public enum intakeState {IN, OUT, STOP};
    public intakeState currentState = intakeState.STOP;
    public void init(HardwareMap hm) {
        intake = hm.get(DcMotor.class, "intake");
    }
    public void Loop(Gamepad gp1, Gamepad gp2) {
        switch (currentState) {
            case IN:
                intake.setPower(0.8);
                gp1.stopRumble();
                gp2.stopRumble();
                break;
            case OUT:
                intake.setPower(-0.6);
                gp1.rumble(Gamepad.RUMBLE_DURATION_CONTINUOUS);
                gp2.rumble(Gamepad.RUMBLE_DURATION_CONTINUOUS);
                break;
            case STOP:
                intake.setPower(0);
                gp1.stopRumble();
                gp2.stopRumble();
                break;
            default:
                intake.setPower(0);
                gp1.rumble(20);
                gp2.rumble(20);
                break;
        }
    }

    public void setState(intakeState state) {
        currentState = state;
    }
    public String getState() {return currentState.name();}
}
