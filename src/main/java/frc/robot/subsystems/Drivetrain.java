package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.util.PigeonWrapper;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

import java.util.function.Supplier;

public class Drivetrain extends TankDrivetrain {

    public static final double WHEEL_DIAMETER_IN_INCHES = 6;
    public static final double INCHES_TO_CM = 2.54;
    public static final double GEAR_RATIO = 1 / 11.161;
    public static final double DISTANCE_PER_PULSE = WHEEL_DIAMETER_IN_INCHES * INCHES_TO_CM * GEAR_RATIO * Math.PI;

    private static Drivetrain instance;

    private final Namespace cameraPIDNamespace = namespace.addChild("camera pid");
    private final Supplier<Double> kPCamera = cameraPIDNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kICamera = cameraPIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kDCamera = cameraPIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceCamera = cameraPIDNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> waitTimeCamera = cameraPIDNamespace.addConstantDouble("wait time", 0);
    private final PIDSettings cameraPIDSettings;

    private final Namespace zoomToTablePIDNamespace = namespace.addChild("zoom to table pid");
    private final Supplier<Double> kPZoom = zoomToTablePIDNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kIZoom = zoomToTablePIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kDZoom = zoomToTablePIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceZoom = zoomToTablePIDNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> waitTimeZoom = zoomToTablePIDNamespace.addConstantDouble("wait time", 0);
    private final PIDSettings zoomToTablePIDSettings;

    private final Namespace shortDrivePIDNamespace = namespace.addChild("zoom to table pid");
    private final Supplier<Double> kPShortDrive = shortDrivePIDNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kIShortDrive = shortDrivePIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kDShortDrive = shortDrivePIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceShortDrive = shortDrivePIDNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> waitTimeShortDrive = shortDrivePIDNamespace.addConstantDouble("wait time", 0);
    private final PIDSettings shortDrivePIDSettings;

    private final CANSparkMax left1;
    private final CANSparkMax left2;
    private final CANSparkMax right1;
    private final CANSparkMax right2;

    private final PigeonWrapper pigeon;

    private final RelativeEncoder leftEncoder;
    private final RelativeEncoder rightEncoder;

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain("drivetrain",
                    new CANSparkMax(RobotMap.CAN.DRIVETRAIN_LEFT_SPARKMAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.DRIVETRAIN_LEFT_SPARKMAX_2, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.DRIVETRAIN_RIGHT_SPARKMAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.DRIVETRAIN_RIGHT_SPARKMAX_2, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new PigeonWrapper(RobotMap.CAN.PIGEON_TALON)
            );
        }
        return instance;
    }

    private Drivetrain(String namespaceName, CANSparkMax left1, CANSparkMax left2,
                       CANSparkMax right1, CANSparkMax right2, PigeonWrapper pigeon) {
        super(namespaceName, new MotorControllerGroup(left1, left2), new MotorControllerGroup(right1, right2));
        this.cameraPIDSettings = new PIDSettings(kPCamera, kICamera, kDCamera,
                toleranceCamera, waitTimeCamera);
        this.zoomToTablePIDSettings = new PIDSettings(kPZoom, kIZoom, kDZoom,
                toleranceZoom, waitTimeZoom);
        this.shortDrivePIDSettings = new PIDSettings(kPShortDrive, kIShortDrive, kDShortDrive,
                toleranceShortDrive, waitTimeShortDrive);
        this.left1 = left1;
        this.left2 = left2;
        this.right1 = right1;
        this.right2 = right2;
        this.pigeon = pigeon;
        this.leftEncoder = left1.getEncoder();
        this.rightEncoder = right1.getEncoder();
        leftEncoder.setPositionConversionFactor(DISTANCE_PER_PULSE);
        rightEncoder.setPositionConversionFactor(DISTANCE_PER_PULSE);
        configureDashboard();
    }

    public void resetPigeon() {
        pigeon.reset();
    }

    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    public double getYaw() {
        double yaw = pigeon.getYaw() % 360;
        if (yaw > 180) yaw -= 360;
        if (yaw < -180) yaw += 360;
        return yaw;
    }

    public double getPitch() {
        return pigeon.getPitch();
    }

    public double getLeftEncoderPosition() {
        return leftEncoder.getPosition();
    }

    public double getRightEncoderPosition() {
        return -rightEncoder.getPosition();
    }

    public double getPitchAceleration() {
        return pigeon.getZAxisAcceleration();
    }

    public double getCurrent() {
        return left1.getOutputCurrent();
    }

    public PIDSettings getCameraPIDSettings() {
        return cameraPIDSettings;
    }

    public PIDSettings getZoomToTablePIDSettings() {
        return zoomToTablePIDSettings;
    }

    public PIDSettings getShortDrivePIDSettings() {
        return shortDrivePIDSettings;
    }

    @Override
    public void configureDashboard() {
        namespace.putData("reset", new InstantCommand(() -> {
            resetEncoders();
            resetPigeon();
        }) {
            @Override
            public boolean runsWhenDisabled() {
                return true;
            }
        });
        namespace.putNumber("pigeon yaw", this::getYaw);
        namespace.putNumber("left neo 1 encoder value", this::getLeftEncoderPosition);
        namespace.putNumber("left neo 2 encoder value", left2.getEncoder()::getPosition);
        namespace.putNumber("right neo 1 encoder value", this::getRightEncoderPosition);
        namespace.putNumber("right neo 2 encoder value", right2.getEncoder()::getPosition);
    }
}
