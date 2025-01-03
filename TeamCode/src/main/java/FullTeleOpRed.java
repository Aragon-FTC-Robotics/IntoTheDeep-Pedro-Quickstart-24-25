import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import mechanisms.*;

@TeleOp(name="\uD83D\uDFE5 Red Teleop", group="Linear OpMode")
public class FullTeleOpRed extends LinearOpMode {
    public Bar bar = new Bar();
    public Claw claw = new Claw();
    public Colorsensor colorsensor = new Colorsensor();
    public Slides slides = new Slides();
    public Drivetrain drivetrain = new Drivetrain();
    public Extendo extendo = new Extendo();
    public Intake intake = new Intake();
    public IntakeWrist intakeWrist = new IntakeWrist();
    public Wrist wrist = new Wrist();
    public ActionHandler actionHandler = new ActionHandler();
    public ElapsedTime timer = new ElapsedTime();
    public Gamepad gp1;
    public Gamepad gp2;
    double looptime = 0.000;
    double highestTime = 0.000;
    @Override
    public void waitForStart() {
        super.waitForStart();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        bar.init(hardwareMap);
        claw.init(hardwareMap);
        colorsensor.init(hardwareMap);
        drivetrain.init(hardwareMap);
        extendo.init(hardwareMap);
        intake.init(hardwareMap);
        intakeWrist.init(hardwareMap);
        slides.init(hardwareMap);
        wrist.init(hardwareMap);
        actionHandler.init(slides,extendo,bar,wrist,intake,claw,intakeWrist,colorsensor, "red");
        timer.reset();

        gp1 = gamepad1;
        gp2 = gamepad2;

        waitForStart();
        timer.reset();
        highestTime = 0.000;
        while(opModeIsActive() && !isStopRequested()) {
            looptime = timer.milliseconds();
            bar.Loop();
            colorsensor.Loop();
            claw.Loop();
            drivetrain.Loop(gp1, gp2); //Gamepad inputs handled by class
            extendo.Loop();
            intake.Loop(gp1, gp2); //Gamepad needed to rumble
            intakeWrist.Loop();
            slides.Loop();
            wrist.Loop();
            actionHandler.Loop(gp1, gp2); // :)
            if (looptime > highestTime) {
                highestTime = looptime;
            }
            telemetry.update();
            timer.reset();
        }
    }
}