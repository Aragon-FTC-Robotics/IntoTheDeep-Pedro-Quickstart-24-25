package testmechanisms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp(name="IntakeWrist", group = "testing")
public class IntakeWristTestOpmode extends LinearOpMode {
    MultipleTelemetry dashboardTelemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public IntakeWristTest intakeWrist = new IntakeWristTest();
    @Override
    public void waitForStart() {
        super.waitForStart();
    }
    @Override
    public void runOpMode() {
        intakeWrist.init(hardwareMap);

        waitForStart();

        while(opModeIsActive() && !isStopRequested()) {
            intakeWrist.Loop();
            dashboardTelemetry.addData("Last commanded position:", "%.3f", intakeWrist.POS);
            dashboardTelemetry.update();
        }
    }
}
