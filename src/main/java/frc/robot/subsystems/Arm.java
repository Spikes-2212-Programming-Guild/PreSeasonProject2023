package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.spikes2212.command.genericsubsystem.smartmotorcontrollersubsystem.SparkMaxGenericSubsystem;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.control.TrapezoidProfileSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

import java.util.function.Supplier;

/**
 * Controls the double-jointed arm that is responsible for placing the cubes.
 */
public class Arm extends SparkMaxGenericSubsystem {


    private static final RootNamespace armSpeeds = new RootNamespace("arm speeds");

    public static final Supplier<Double> LOWER_SHAFT_MOVE_FORWARD_SPEED = armSpeeds.addConstantDouble("lower shaft move forward speed", 0.1);
    public static final Supplier<Double> LOWER_SHAFT_MOVE_BACKWARD_SPEED = armSpeeds.addConstantDouble("lower shaft move backward speed", -0.2);
    public static final Supplier<Double> LOWER_SHAFT_IDLE_SPEED =
            armSpeeds.addConstantDouble("lower shaft idle speed", -0.1);
    public static final Supplier<Double> UPPER_SHAFT_MOVE_UP_SPEED = armSpeeds.addConstantDouble("upper shaft move up speed", -0.1);
    public static final Supplier<Double> UPPER_SHAFT_MOVE_DOWN_SPEED = armSpeeds.addConstantDouble("upper shaft move down speed", 0.1);

    public static final Supplier<Double> UPPER_SHAFT_IDLE_SPEED = armSpeeds.addConstantDouble("upper shaft idle speed", -0.05);

    public enum State {
        //@TODO calculate
        RESTING(0, 0), PICKING(0, 0), PLACING_ZERO(0, 0), PLACING_ONE(0, 0), PLACING_TWO(0, 0);

        private final double alpha;
        private final double beta;

        State(double alpha, double beta) {
            this.alpha = alpha;
            this.beta = beta;
        }

        public double getAlpha() {
            return alpha;
        }

        public double getBeta() {
            return beta;
        }
    }

    private static Arm lowerInstance;
    private static Arm upperInstance;

    private final Namespace pidNamespace = namespace.addChild("pid");
    private final Supplier<Double> kP = pidNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kI = pidNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kD = pidNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> tolerance = pidNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> waitTime = pidNamespace.addConstantDouble("wait time", 0);
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
                    new CANSparkMax(RobotMap.CAN.ARM_LOWER_SPARKMAX_1, CANSparkMaxLowLevel.MotorType.kBrushless), false,
                    new CANSparkMax(RobotMap.CAN.ARM_LOWER_SPARKMAX_2, CANSparkMaxLowLevel.MotorType.kBrushless)
            );
            lowerInstance.namespace.putBoolean("slave following", lowerInstance.slaves.get(0)::isFollower);
            lowerInstance.namespace.putNumber("slave voltage", lowerInstance.slaves.get(0)::getOutputCurrent);
        }
        return lowerInstance;
    }

    public static Arm getUpperInstance() {
        if (upperInstance == null) {
            upperInstance = new Arm("upper shaft", new CANSparkMax(RobotMap.CAN.ARM_UPPER_SPARKMAX,
                    CANSparkMaxLowLevel.MotorType.kBrushless), true);
        }
        return upperInstance;
    }

    private Arm(String namespaceName, CANSparkMax master, boolean isUpper, CANSparkMax... slaves) {
        super(namespaceName, master, slaves);
        this.master.restoreFactoryDefaults();
        this.slaves.forEach(CANSparkMaxLowLevel::restoreFactoryDefaults);
        this.slaves.forEach(s -> s.follow(master));
        this.master.setIdleMode(CANSparkMax.IdleMode.kBrake);
        this.slaves.forEach(s -> s.setIdleMode(CANSparkMax.IdleMode.kBrake));
        if (isUpper) {
            master.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
            master.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, -10.8F);
            master.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
            master.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 0);
            namespace.putBoolean("soft limit forward", () ->
                    master.isSoftLimitEnabled(CANSparkMax.SoftLimitDirection.kForward));
            namespace.putBoolean("soft limit reverse", () ->
                    master.isSoftLimitEnabled(CANSparkMax.SoftLimitDirection.kReverse));
            namespace.putNumber("soft limit value forward", () ->
                    master.getSoftLimit(CANSparkMax.SoftLimitDirection.kForward));
            namespace.putNumber("soft limit value reverse", () ->
                    master.getSoftLimit(CANSparkMax.SoftLimitDirection.kReverse));
        }
        this.pidSettings = new PIDSettings(kP, kI, kD, tolerance, waitTime);
        this.feedForwardSettings = new FeedForwardSettings(kV, () -> 0.0);
        this.trapezoidProfileSettings = new TrapezoidProfileSettings(accelerationRate, maxVelocity, curve);
        this.encoder = master.getEncoder();
        configureDashboard();
    }

    public void resetEncoder() {
        encoder.setPosition(0);
    }

    public PIDSettings getPIDSettings() {
        return pidSettings;
    }

    public FeedForwardSettings getFeedForwardSettings() {
        return feedForwardSettings;
    }

    public TrapezoidProfileSettings getTrapezoidProfileSettings() {
        return trapezoidProfileSettings;
    }

    public double getEncoderPosition() {
        return encoder.getPosition();
    }

    @Override
    public void configureDashboard() {
        namespace.putNumber("encoder position", this::getEncoderPosition);
        namespace.putNumber("output voltage", this.master::getAppliedOutput);
        namespace.putNumber("output current", this.master::getOutputCurrent);
        namespace.putData("reset encoder", new InstantCommand(this::resetEncoder));
    }
}
