package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.smartmotorcontrollersubsystem.SparkMaxGenericSubsystem;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.control.TrapezoidProfileSettings;
import com.spikes2212.dashboard.Namespace;
import frc.robot.RobotMap;

import java.util.List;
import java.util.function.Supplier;

/**
 * Controls the double-jointed arm that is responsible for placing the cubes.
 */
public class Arm extends SparkMaxGenericSubsystem {

    enum State {
        RESTING, PICKING, PLACING_ZERO, PLACING_ONE, PLACING_TWO;
    }

    private static Arm lowerInstance;
    private static Arm upperInstance;

    private final Namespace PIDNamespace = namespace.addChild("pid");
    private final Supplier<Double> kP = PIDNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kI = PIDNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kD = PIDNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> tolerance = PIDNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> waitTime = PIDNamespace.addConstantDouble("wait time", 0);
    private final PIDSettings pidSettings;

    private final Namespace feedForwardNamespace = namespace.addChild("feed forward");
    private final Supplier<Double> kV = feedForwardNamespace.addConstantDouble("kV", 0);
    private final FeedForwardSettings feedForwardSettings;

    private final Namespace trapezoidProfileNamespace = namespace.addChild("trapezoid profile");
    private final Supplier<Double> accelerationRate =
            trapezoidProfileNamespace.addConstantDouble("acceleration rate", 0);
    private final Supplier<Double> maxVelocity = trapezoidProfileNamespace.addConstantDouble("max velocity", 0);
    private final Supplier<Integer> curve = trapezoidProfileNamespace.addConstantInt("curve", 0);
    private final TrapezoidProfileSettings trapezoidProfileSettings;

    private final RelativeEncoder encoder;

    public static Arm getLowerInstance() {
        if (lowerInstance == null) {
            lowerInstance = new Arm("lower shaft",
                    new CANSparkMax(RobotMap.CAN.ARM_LOWER_SPARKMAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.ARM_LOWER_SPARKMAX_2, CANSparkMaxLowLevel.MotorType.kBrushless)
            );
        }
        return lowerInstance;
    }

    public static Arm getUpperInstance() {
        if (upperInstance == null) {
            upperInstance = new Arm("upper shaft", new CANSparkMax(RobotMap.CAN.ARM_UPPER_SPARKMAX,
                    CANSparkMaxLowLevel.MotorType.kBrushless));
        }
        return upperInstance;
    }

    private Arm(String namespaceName, CANSparkMax master, CANSparkMax... slaves) {
        super(namespaceName, master, slaves);
        this.pidSettings = new PIDSettings(kP, kI, kD, tolerance, waitTime);
        this.feedForwardSettings = new FeedForwardSettings(kV, () -> 0.0);
        this.trapezoidProfileSettings = new TrapezoidProfileSettings(accelerationRate, maxVelocity, curve);
        this.encoder = master.getEncoder();
        configureDashboard();
    }

    public double getEncoderPosition() {
        return encoder.getPosition();
    }

    @Override
    public void configureDashboard() {
        namespace.putNumber("encoder position", this::getEncoderPosition);
    }
}
