import com.pedropathing.follower.*;
import com.pedropathing.localization.*;
import com.pedropathing.pathgen.*;
import com.pedropathing.util.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import mechanisms.*;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous(name = "0+4", group = "Auto")
public class Auto_0_4 extends OpMode {
    private Bar bar;
    private Claw claw;
    private Extendo extendo;
    private Intake intake;
    private IntakeWrist intakeWrist;
    private Slides slides;
    private Wrist wrist;


    private Follower follower;
    private Timer pathTime, totalTime;
    private int pathState = 0;


    private final Pose STARTPOSE = new Pose(7.065,83.368, Math.toRadians(-90));
    private final Pose PRELOADPOSE = new Pose(12.845, 124.217, Math.toRadians(-55));
    private final Pose INTAKE1POSE = new Pose(45.73, 103.536, Math.toRadians(90));
    private final Pose INTAKE2POSE = new Pose(45.73, 111.757, Math.toRadians(90));
    private final Pose INTAKE3POSE = new Pose(45.73, 123.318, Math.toRadians(90));
    private final Pose BUCKETPOSE = new Pose(15.157, 126.786, Math.toRadians(-45));
    private final Pose ASCENTPOSE = new Pose(60.631, 94.801, Math.toRadians(-90));
    private final Pose ASCENTCONTROL1 = new Pose(63.072, 114.326);

    private Path scorePreload, park;
    private PathChain grab1, grab2, grab3, score1, score2, score3;

    private void buildPaths() {
        //Start -> Preload bucket pos
        scorePreload = new Path(new BezierLine(new Point(STARTPOSE), new Point(PRELOADPOSE)));
        scorePreload.setLinearHeadingInterpolation(STARTPOSE.getHeading(), PRELOADPOSE.getHeading());

        //Preload bucket pos -> Intake 1
        grab1 = follower.pathBuilder()
                .addPath(
                        new BezierLine(new Point(PRELOADPOSE), new Point(INTAKE1POSE))
                )
                .setLinearHeadingInterpolation(PRELOADPOSE.getHeading(), INTAKE1POSE.getHeading())
                .build();

        //Intake 1 -> bucket
        score1 = follower.pathBuilder()
                .addPath(
                        new BezierLine(new Point(INTAKE1POSE), new Point(BUCKETPOSE))
                )
                .setLinearHeadingInterpolation(INTAKE1POSE.getHeading(), BUCKETPOSE.getHeading())
                .build();

        //Bucket -> intake 2
        grab2 = follower.pathBuilder()
                .addPath(
                        new BezierLine(new Point(BUCKETPOSE), new Point(INTAKE2POSE))
                )
                .setLinearHeadingInterpolation(BUCKETPOSE.getHeading(), INTAKE2POSE.getHeading())
                .build();

        //intake 2 -> bucket
        score2 = follower.pathBuilder()
                .addPath(
                        new BezierLine(new Point(INTAKE2POSE), new Point(BUCKETPOSE))
                )
                .setLinearHeadingInterpolation(INTAKE2POSE.getHeading(), BUCKETPOSE.getHeading())
                .build();

        //bucket -> intake 3
        grab3 = follower.pathBuilder()
                .addPath(
                        new BezierLine(new Point(BUCKETPOSE), new Point(INTAKE3POSE))
                )
                .setLinearHeadingInterpolation(BUCKETPOSE.getHeading(), INTAKE3POSE.getHeading())
                .build();

        //intake 3 -> bucket
        score3 = follower.pathBuilder()
                .addPath(
                        new BezierLine(new Point(INTAKE3POSE), new Point(BUCKETPOSE))
                )
                .setLinearHeadingInterpolation(INTAKE3POSE.getHeading(), BUCKETPOSE.getHeading())
                .build();

        //Bucket -> ascent (bezier curve)
        park = new Path(new BezierCurve(
                new Point(BUCKETPOSE),
                new Point(ASCENTCONTROL1),
                new Point(ASCENTPOSE)
            )
        );
    }
    private void updatePaths() {
        switch (pathState) {
            case 0:
                slides.setTargetPos(slides.HIGH);
                bar.setState(Bar.BarState.BUCKET);
                wrist.setState(Wrist.wristState.BUCKET);
                claw.setState(Claw.ClawState.CLOSE);
                follower.followPath(scorePreload); //Start -> preload score
                setPathState(1);
                break;
            case 1:
                if ((Math.abs(PRELOADPOSE.getX() - follower.getPose().getX()) <= 1) && (Math.abs(PRELOADPOSE.getY() - follower.getPose().getY()) <= 1)) {
                    claw.setState(Claw.ClawState.OPEN);
                    follower.followPath(grab1, true); //preload score -> samp1
                    setPathState(2);
                }
                break;
            case 2:
                if ((Math.abs(INTAKE1POSE.getX() - follower.getPose().getX()) <= 0.3) && (Math.abs(INTAKE1POSE.getY() - follower.getPose().getY()) <= 0.3)) {
                    slides.setTargetPos(slides.GROUND);
                    bar.setState(Bar.BarState.NEUTRAL);
                    wrist.setState(Wrist.wristState.NEUTRAL);
                    claw.setState(Claw.ClawState.CLOSE);
                    extendo.setTargetPos(Extendo.MAX);
                    intake.setState(Intake.intakeState.IN);
                    intakeWrist.setState(IntakeWrist.intakeWristState.OUT);

                    setPathState(3);
                }
                break;
            case 3:
                if (pathTime.getElapsedTimeSeconds() > 2) {
                    intake.setState(Intake.intakeState.STOP);
                    extendo.setTargetPos(Extendo.MIN);
                    intakeWrist.setState(IntakeWrist.intakeWristState.IN);
                    follower.followPath(score1, true); //Samp 1 -> bucket
                    setPathState(4);
                }
                break;
            case 4:
                if (pathTime.getElapsedTimeSeconds() > 2) {
                    bar.setState(Bar.BarState.TRANSFER);
                    wrist.setState(Wrist.wristState.TRANSFER);
                    setPathState(5);
                }
                break;
            case 5:
                if (pathTime.getElapsedTimeSeconds() > 2) {
                    claw.setState(Claw.ClawState.OPEN);
                    setPathState(6);
                }
                break;
            case 6:
                if (pathTime.getElapsedTimeSeconds() > 2) {
                    slides.setTargetPos(Slides.HIGH);
                    bar.setState(Bar.BarState.BUCKET);
                    wrist.setState(Wrist.wristState.BUCKET);
                    setPathState(7);
                }
                break;
            case 7:
                if (pathTime.getElapsedTimeSeconds() > 2) {
                    claw.setState(Claw.ClawState.CLOSE);
                    follower.followPath(grab2, true);
                    setPathState(8);
                }
                break;


        }
    }
    private void setPathState(int n) {
        pathState = n;
        pathTime.resetTimer();
    }
    @Override
    public void init() {
        pathTime = new Timer();
        totalTime = new Timer();
        totalTime.resetTimer();
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(STARTPOSE);
        buildPaths();

        bar = new Bar();
        claw = new Claw();
        extendo = new Extendo();
        intake = new Intake();
        intakeWrist = new IntakeWrist();
        slides = new Slides();
        wrist = new Wrist();

        bar.init(hardwareMap);
        claw.init(hardwareMap);
        extendo.init(hardwareMap);
        intake.init(hardwareMap);
        intakeWrist.init(hardwareMap);
        slides.init(hardwareMap);
        wrist.init(hardwareMap);

        claw.setState(Claw.ClawState.CLOSE);
        bar.setState(Bar.BarState.NEUTRAL);
        wrist.setState(Wrist.wristState.NEUTRAL);

    }
    @Override
    public void start() {
        totalTime.resetTimer();
        setPathState(0);
    }
    @Override
    public void loop() {
        follower.update();
        updatePaths();
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }
}
