package testmechanisms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Extendo", group = "testing")
public class ExtendoTestOpmode extends LinearOpMode {
    MultipleTelemetry dashboardTelemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public ExtendoTest extendo = new ExtendoTest();
    @Override
    public void waitForStart() {
        super.waitForStart();
    }
    @Override
    public void runOpMode() {
        extendo.init(hardwareMap);

        waitForStart();

        while(opModeIsActive() && !isStopRequested()) {
            extendo.Loop();
            dashboardTelemetry.addData("pos", extendo.getPos());
            dashboardTelemetry.addData("target", extendo.getTargetPos());
            dashboardTelemetry.update();
        }
    }
}
