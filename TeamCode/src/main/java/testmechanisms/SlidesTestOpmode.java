package testmechanisms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Slides", group = "testing")
public class SlidesTestOpmode extends LinearOpMode {
    MultipleTelemetry dashboardTelemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public SlidesTest slides = new SlidesTest();
    @Override
    public void waitForStart() {
        super.waitForStart();
    }
    @Override
    public void runOpMode() {
        slides.init(hardwareMap);

        waitForStart();

        while(opModeIsActive() && !isStopRequested()) {
            slides.Loop();
            dashboardTelemetry.addData("pos L", slides.getLPos());
            dashboardTelemetry.addData("pos R", slides.getRPos());
            dashboardTelemetry.addData("target", slides.getTargetPos());
            dashboardTelemetry.update();
        }
    }
}
