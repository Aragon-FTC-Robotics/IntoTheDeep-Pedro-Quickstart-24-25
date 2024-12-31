package testmechanisms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Wrist", group = "testing")
public class WristTestOpmode extends LinearOpMode {
    MultipleTelemetry dashboardTelemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public WristTest wrist = new WristTest();

    @Override
    public void waitForStart() {
        super.waitForStart();
    }

    @Override
    public void runOpMode() {
        wrist.init(hardwareMap);
        waitForStart();

        while(opModeIsActive() && !isStopRequested()) {
            wrist.Loop();
            dashboardTelemetry.addData("Last commanded position:", "%.3f", wrist.POS);
            dashboardTelemetry.update();
        }
    }
}
