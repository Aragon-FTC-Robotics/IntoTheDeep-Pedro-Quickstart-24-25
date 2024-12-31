package testmechanisms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Bar", group = "testing")
public class BarTestOpmode extends LinearOpMode {
    MultipleTelemetry dashboardTelemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public BarTest bar = new BarTest();

    @Override
    public void waitForStart() {
        super.waitForStart();
    }

    @Override
    public void runOpMode() {
        bar.init(hardwareMap);

        waitForStart();

        while(opModeIsActive() && !isStopRequested()) {
            bar.Loop();
            dashboardTelemetry.addData("Last commanded position:", "%.3f", bar.POS);
            dashboardTelemetry.update();
        }
    }
}
