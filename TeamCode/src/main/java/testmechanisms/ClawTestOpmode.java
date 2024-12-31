package testmechanisms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Claw", group = "testing")
public class ClawTestOpmode extends LinearOpMode{
    MultipleTelemetry dashboardTelemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public ClawTest claw = new ClawTest();

    @Override
    public void waitForStart() {
        super.waitForStart();
    }

    @Override
    public void runOpMode() {
        claw.init(hardwareMap);

        waitForStart();

        while(opModeIsActive() && !isStopRequested()) {
            claw.Loop();
            dashboardTelemetry.addData("Last commanded position:", "%.3f", claw.POS);
            dashboardTelemetry.update();
        }
    }
}
