import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

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
        CLIP, //delay to wrist move
        HIGHBUCKET, //slides up BEFORE
        SLIDESDOWN_STAGE_1, //extendo in
        RESETEXTENDO,
        RESETINTAKEWRIST_STAGE_1, RESETINTAKEWRIST_STAGE_2,
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
        //clip
        if (gp2.b) {
            wallPickup();
        }
        if (gp1.x) {
            intakeWristIn();
        }
        if (gp2.a) {
            closeClawWall();
        }

        if (gp2.x) {
            clippos();
        }
        if (gp2.y) {
            clip_down();
        }

        //intake
        if (gp1.y && !intaking) {
            intake(); //y
        }
        intakeCheck();

        if (gp2.dpad_up) {
            transfer(); //gp1 left_bumper, gp2 L&R stick button for nudge
        }
        if (gp2.left_stick_button && gp2.right_stick_button) {
            nudge();
        }

        //bucket
        highBucket(); //dpad_up
        if (gp2.dpad_down) {
            slidesDown(); //dpad_down
        }

        //reset
        if (gp1.left_trigger > 0.5) {
            resetIntakeWrist();
        }
        if (gp1.dpad_down) {
            resetExtendo(); //dpad_down
        }

        TimedActions();
    }

    public void TimedActions() {
        long elapsedMs = timer.time(TimeUnit.MILLISECONDS);

        switch (currentActionState) {
            //transfer
            case TRANSFER_STAGE_1:
                if (elapsedMs >= 500) {
                    extendo.setTargetPos(Extendo.MIN);
                    currentActionState = ActionState.TRANSFER_STAGE_2;
                    timer.reset();
                }
                break;
            case TRANSFER_STAGE_2:
                if (elapsedMs >= 500) {
                    intake.setState(Intake.intakeState.OUT);
                    currentActionState = ActionState.TRANSFER_STAGE_3;
                    timer.reset();
                }
                break;
            case TRANSFER_STAGE_3:
                if (elapsedMs >= 1000) {
                    intake.setState(Intake.intakeState.STOP);
                    bar.setState(Bar.BarState.TRANSFER);
                    wrist.setState(Wrist.wristState.TRANSFER);
                    currentActionState = ActionState.TRANSFER_STAGE_4;
                    timer.reset();
                }
                break;
            case TRANSFER_STAGE_4:
                if (elapsedMs >= 250) {
                    claw.setState(Claw.ClawState.OPEN);
                    currentActionState = ActionState.TRANSFER_STAGE_5;
                    timer.reset();
                }
                break;
            case TRANSFER_STAGE_5:
                if (elapsedMs >= 500) {
                    bar.setState(Bar.BarState.NEUTRAL);
                    currentActionState = ActionState.IDLE;
                }
                break;

            //high bucket
            case HIGHBUCKET:
                if (elapsedMs >= 700) {
                    bar.setState(Bar.BarState.BUCKET);
                    wrist.setState(Wrist.wristState.BUCKET);
                    currentActionState = ActionState.IDLE;
                }
                break;

            //clipping
            case CLIP:
                if (elapsedMs >= 220) {
                    claw.setState(Claw.ClawState.OPEN);
                    currentActionState = ActionState.IDLE;
                    extendo.setTargetPos(Extendo.MIN);
                }
                break;

            //reset extendo
            case RESETEXTENDO:
                if (elapsedMs >= 1000) {
                    extendo.DANGEROUS_RESET_ENCODERS();
                    currentActionState = ActionState.IDLE;
                }
                break;

            //reset intake wrist
            case RESETINTAKEWRIST_STAGE_1:
                if (elapsedMs >= 500) {
                    intakeWrist.setState(IntakeWrist.intakeWristState.IN);
                    currentActionState = ActionState.RESETINTAKEWRIST_STAGE_2;
                    timer.reset();
                }
                break;
            case RESETINTAKEWRIST_STAGE_2:
                if (elapsedMs >= 500) {
                    intakeWrist.setState(IntakeWrist.intakeWristState.OUT);
                    intake.setState(Intake.intakeState.IN);
                    currentActionState = ActionState.IDLE;
                }
                break;

            //nudge sample in intake
            case NUDGE1:
                if (elapsedMs >= 100) {
                    bar.setState(Bar.BarState.TRANSFER);
                    currentActionState = ActionState.NUDGE2;
                    timer.reset();
                }
                break;
            case NUDGE2:
                if (elapsedMs >= 100) {
                    bar.setState(Bar.BarState.NEUTRAL);
                    currentActionState = ActionState.NUDGE3;
                    timer.reset();
                }
                break;
            case NUDGE3:
                if (elapsedMs >= 100) {
                    bar.setState(Bar.BarState.TRANSFER);
                    currentActionState = ActionState.IDLE;
                    timer.reset();
                }
                break;

            default:
                currentActionState = ActionState.IDLE;
                break;
        }
    }

    private void wallPickup() {
        claw.setState(Claw.ClawState.OPEN);
        bar.setState(Bar.BarState.WALL);
        wrist.setState(Wrist.wristState.WALL);
        intakeWrist.setState(IntakeWrist.intakeWristState.OUT);

    }

    private void intakeWristIn() {
        intakeWrist.setState(IntakeWrist.intakeWristState.IN);
    }

    private void closeClawWall(){
        claw.setState(Claw.ClawState.CLOSE);
        bar.setState(Bar.BarState.NEUTRAL);
        wrist.setState(Wrist.wristState.TRANSFER);
    }

    private void intake() {
            intaking = true;
            intake.setState(Intake.intakeState.IN);
            intakeWrist.setState(IntakeWrist.intakeWristState.OUT);
    }

    private void resetIntakeWrist() {
        intake.setState(Intake.intakeState.STOP);
        intaking = false;
        currentActionState = ActionState.RESETINTAKEWRIST_STAGE_1;
        timer.reset();
    }

    private void transfer() {
        bar.setState(Bar.BarState.NEUTRAL);
        wrist.setState(Wrist.wristState.TRANSFER);
        claw.setState(Claw.ClawState.CLOSE);
        intakeWrist.setState(IntakeWrist.intakeWristState.IN);
        currentActionState = ActionState.TRANSFER_STAGE_1;
        timer.reset();
        intake.setState(Intake.intakeState.STOP);
        intaking = false;
    }

    private void nudge(){
        bar.setState(Bar.BarState.NEUTRAL);
        wrist.setState(Wrist.wristState.TRANSFER);
        currentActionState = ActionState.NUDGE1;
        timer.reset();

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

    private void highBucket() {
        slides.setTargetPos(Slides.HIGH);
        currentActionState = ActionState.HIGHBUCKET;
        timer.reset();
    }

    private void slidesDown() {
        extendo.setTargetPos(Extendo.MED);
        bar.setState(Bar.BarState.NEUTRAL);
        wrist.setState(Wrist.wristState.TRANSFER);
        slides.setTargetPos(Slides.GROUND);
        currentActionState = ActionState.SLIDESDOWN_STAGE_1;
        timer.reset();
    }

    private void resetExtendo() {
            extendo.setTargetPos(-700);
            currentActionState = ActionState.RESETEXTENDO;
            timer.reset();
    }

    public void clippos() {
        bar.setState(Bar.BarState.CLIP);
        wrist.setState(Wrist.wristState.CLIP);
        slides.setTargetPos(Slides.MED);
        intakeWrist.setState(IntakeWrist.intakeWristState.IN);
    }
    public void clip_down(){
        slides.setTargetPos(Slides.GROUND);
        extendo.setTargetPos(Extendo.MED);
        currentActionState = ActionState.CLIP;
        timer.reset();
    }
}