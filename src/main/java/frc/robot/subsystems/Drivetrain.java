package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.util.PigeonWrapper;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

import java.util.function.Supplier;

public class Drivetrain extends TankDrivetrain {

    public static final double MILLIMETER_TO_CENTIMETER = 0.1;
    public static final double DISTANCE_PER_PULSE = -1; // @todo

    private static Drivetrain instance;

    private final Namespace cameraPIDNamespace = namespace.addChild("camera pid");
    private final Supplier<Double> kPCamera = cameraPIDNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kICamera = cameraPIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kDCamera = cameraPIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceCamera = cameraPIDNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> waitTimeCamera = cameraPIDNamespace.addConstantDouble("wait time", 0);
    private final PIDSettings pidSettingsCamera;

    private final Namespace drivePIDNamespace = namespace.addChild("drive pid");
    private final Supplier<Double> kPDrive = cameraPIDNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kIDrive = cameraPIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kDDrive = cameraPIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> toleranceDrive = cameraPIDNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> waitTimeDrive = cameraPIDNamespace.addConstantDouble("wait time", 0);
    private final PIDSettings pidSettingsDrive;

    private final CANSparkMax left1;
    private final CANSparkMax left2;
    private final CANSparkMax right1;
    private final CANSparkMax right2;

    private final Ultrasonic ultrasonic;
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
                    new PigeonWrapper(RobotMap.CAN.PIGEON_TALON),
                    new Ultrasonic(RobotMap.CAN.ULTRASONIC_CHANNEL_1, RobotMap.CAN.ULTRASONIC_CHANNEL_2)
            );
        }
        return instance;
    }

    private Drivetrain(String namespaceName, CANSparkMax left1, CANSparkMax left2,
                       CANSparkMax right1, CANSparkMax right2, PigeonWrapper pigeon, Ultrasonic ultrasonic) {
        super(namespaceName, new MotorControllerGroup(left1, left2), new MotorControllerGroup(right1, right2));
        this.pidSettingsCamera = new PIDSettings(kPCamera, kICamera, kDCamera,
                toleranceCamera, waitTimeCamera);
        this.pidSettingsDrive = new PIDSettings(kPDrive, kIDrive, kDDrive,
                toleranceDrive, waitTimeDrive);
        this.left1 = left1;
        this.left2 = left2;
        this.right1 = right1;
        this.right2 = right2;
        this.pigeon = pigeon;
        this.ultrasonic = ultrasonic;
        Ultrasonic.setAutomaticMode(true);
        this.leftEncoder = left1.getEncoder();
        this.rightEncoder = right1.getEncoder();
        leftEncoder.setPositionConversionFactor(DISTANCE_PER_PULSE);
        rightEncoder.setPositionConversionFactor(DISTANCE_PER_PULSE);
        configureDashboard();
    }

    public Namespace getCameraPIDNamespace() {
        return cameraPIDNamespace;
    }

    public Supplier<Double> getkPCamera() {
        return kPCamera;
    }

    public Supplier<Double> getkICamera() {
        return kICamera;
    }

    public Supplier<Double> getkDCamera() {
        return kDCamera;
    }

    public Supplier<Double> getToleranceCamera() {
        return toleranceCamera;
    }

    public Supplier<Double> getWaitTimeCamera() {
        return waitTimeCamera;
    }

    public PIDSettings getPidSettingsCamera() {
        return pidSettingsCamera;
    }

    public Namespace getDrivePIDNamespace() {
        return drivePIDNamespace;
    }

    public Supplier<Double> getkPDrive() {
        return kPDrive;
    }

    public Supplier<Double> getkIDrive() {
        return kIDrive;
    }

    public Supplier<Double> getkDDrive() {
        return kDDrive;
    }

    public Supplier<Double> getToleranceDrive() {
        return toleranceDrive;
    }

    public Supplier<Double> getWaitTimeDrive() {
        return waitTimeDrive;
    }

    public PIDSettings getPidSettingsDrive() {
        return pidSettingsDrive;
    }

    public CANSparkMax getLeft1() {
        return left1;
    }

    public CANSparkMax getLeft2() {
        return left2;
    }

    public CANSparkMax getRight1() {
        return right1;
    }

    public CANSparkMax getRight2() {
        return right2;
    }

    public Ultrasonic getUltrasonic() {
        return ultrasonic;
    }

    public PigeonWrapper getPigeon() {
        return pigeon;
    }

    public RelativeEncoder getLeftEncoder() {
        return leftEncoder;
    }

    public RelativeEncoder getRightEncoder() {
        return rightEncoder;
    }

    public double getYaw() {
        double yaw = pigeon.getYaw() % 360;
        if (yaw > 180) yaw -= 360;
        if (yaw < -180) yaw += 360;
        return yaw;
    }

    public void resetPigeon() {
        pigeon.reset();
    }

    public double getUltrasonicDistanceInCM() {
        return ultrasonic.getRangeMM() * MILLIMETER_TO_CENTIMETER;
    }

    public double getLeftEncoderPosition() {
        return leftEncoder.getPosition();
    }

    public double getRightEncoderPosition() {
        return rightEncoder.getPosition();
    }

    @Override
    public void configureDashboard() {
        namespace.putData("reset pigeon", new InstantCommand(this::resetPigeon) {
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
        namespace.putNumber("ultrasonic distance value", this::getUltrasonicDistanceInCM);
    }
}
