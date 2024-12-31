import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import mechanisms.*;

public class ActionHandler {
    private Slides slides;
    private Extendo extendo;
    private Bar bar;
    private Wrist wrist;
    private Intake intake;
    private Claw claw;
    private IntakeWrist intakeWrist;
    private Colorsensor colorSensor;

    private boolean intaking = false;
    private String alliance;

    private ElapsedTime timer = new ElapsedTime();
    private ElapsedTime intakeTimer = new ElapsedTime();
    private boolean waitingForSecondCheck = false;

    private ActionState currentActionState = ActionState.IDLE;

    enum ActionState {
        IDLE,
        TRANSFER_STAGE_1, //intakewrist in BEFORE, stage1: claw close when wait is done
        TRANSFER_STAGE_2, //flywheel out
        TRANSFER_STAGE_3, //barwrist transfer, flywheel stop
        TRANSFER_STAGE_4, //clawopen
        TRANSFER_STAGE_5,
        CLIP2_STAGE_1, //delay to wrist move
        CLIP2_STAGE_2, //delay to claw open
        HIGHBUCKET_STAGE_1, //slides up BEFORE
        HIGHBUCKET_STAGE_2, //barwrist bucket
        //        HIGHBUCKET_STAGE_3 //claw open and extendo medium
        //slides up -> stage 1 -> barwrist -> stage2 ... etc
        //each case statement has a delay and then runs an action after....
        SLIDESDOWN_STAGE_1, //extendo in
        INTAKERESET_STAGE_1, //flywehl out
        INTAKERESET_STAGE_2, //wheel STOP
        RESETSTATE,
        NUDGE1, NUDGE2, NUDGE3
    }

    public void init(Slides s, Extendo e, Bar b, Wrist w, Intake f, Claw c, IntakeWrist iw, Colorsensor cs, String alliance) {
        slides = s;
        extendo = e;
        bar = b;
        wrist = w;
        intake = f;
        claw = c;
        intakeWrist = iw;
        colorSensor = cs;
        this.alliance = alliance;
    }

    public void Loop(Gamepad gp1, Gamepad gp2) {
        wallPickup(gp2);
        Intake(gp1);
    }

    public void handleTimedActions(){

    }

    private void wallPickup(Gamepad gp2){
        if (gp2.b) {
            claw.setState(Claw.ClawState.OPEN);
            bar.setState(Bar.BarState.WALL);
            wrist.setState(Wrist.wristState.WALL);
            intakeWrist.setState(IntakeWrist.intakeWristState.OUT);
        }

        if (gp2.x) {
            intakeWrist.setState(IntakeWrist.intakeWristState.IN);
        }

        if (gp2.a) {
            claw.setState(Claw.ClawState.CLOSE);
            bar.setState(Bar.BarState.NEUTRAL);
            wrist.setState(Wrist.wristState.TRANSFER);
        }
    }

    private void Intake(Gamepad gp1) {
        if (gp1.y && !intaking) {
            intaking = true;
            intake.setState(Intake.intakeState.IN);
            intakeWrist.setState(IntakeWrist.intakeWristState.OUT);
        }
        intakeCheck();
    }





    public void intakeCheck() {
        if (intaking) {
            // Wait for 300ms before checking again
            if (intakeTimer.milliseconds() >= 300) {
                if (!waitingForSecondCheck) {
                    // First check: Determine if the color is correct
                    boolean correctColor = (alliance.equals("red") && (colorSensor.sensorIsRed() || colorSensor.sensorIsYellow()))
                            || (alliance.equals("blue") && (colorSensor.sensorIsBlue() || colorSensor.sensorIsYellow()));

                    if (correctColor) {
                        // Found the correct color, initiate second check
                        waitingForSecondCheck = true;
                        intakeTimer.reset(); // Reset timer for second check
                    } else {
                        // Handle wrong color immediately
                        boolean wrongColor = (alliance.equals("red") && colorSensor.sensorIsBlue())
                                || (alliance.equals("blue") && colorSensor.sensorIsRed());

                        if (wrongColor) {
                            intake.setState(Intake.intakeState.OUT); // Reverse flywheel to eject
                            intaking = false; // Stop intaking
                        }
                        intakeTimer.reset(); // Reset timer for the next cycle
                    }
                } else {
                    // Second check after 300ms
                    boolean correctColor = (alliance.equals("red") && (colorSensor.sensorIsRed() || colorSensor.sensorIsYellow()))
                            || (alliance.equals("blue") && (colorSensor.sensorIsBlue() || colorSensor.sensorIsYellow()));

                    if (correctColor) {
                        // Confirmed correct color, stop flywheel and intaking
                        intake.setState(Intake.intakeState.STOP);
                        intaking = false;
                    }

                    // Reset state and timer for the next loop
                    waitingForSecondCheck = false;
                    intakeTimer.reset();
                }
            }
        }
    }
}
