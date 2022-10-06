package subsystems;

import com.spikes2212.command.DashboardedSubsystem;
import com.spikes2212.command.genericsubsystem.smartmotorcontrollersubsystem.SparkMaxGenericSubsystem;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.control.TrapezoidProfileSettings;
import com.spikes2212.dashboard.Namespace;

import java.util.function.Supplier;

public class Arm extends DashboardedSubsystem {

    private final SparkMaxGenericSubsystem lowerShaft;
    private final SparkMaxGenericSubsystem higherShaft;

    private final Namespace lowerShaftNamespace = namespace.addChild("lower shaft");

    private final Supplier<Double> kPLowerShaft = lowerShaftNamespace.addConstantDouble("kP lower shaft", 0);
    private final Supplier<Double> kILowerShaft = lowerShaftNamespace.addConstantDouble("kI lower shaft", 0);
    private final Supplier<Double> kDLowerShaft = lowerShaftNamespace.addConstantDouble("kD lower shaft", 0);
    private final Supplier<Double> toleranceLowerShaft =
            lowerShaftNamespace.addConstantDouble("tolerance lower shaft", 0);
    private final Supplier<Double> waitTimeLowerShaft =
            lowerShaftNamespace.addConstantDouble("wait time lower shaft", 0);

    private final Supplier<Double> kSLowerShaft = lowerShaftNamespace.addConstantDouble("kS lower shaft", 0);
    private final Supplier<Double> kVLowerShaft = lowerShaftNamespace.addConstantDouble("kV lower shaft", 0);
    private final Supplier<Double> kALowerShaft = lowerShaftNamespace.addConstantDouble("kA lower shaft", 0);
    private final Supplier<Double> kGLowerShaft = lowerShaftNamespace.addConstantDouble("kG lower shaft", 0);

    private final Supplier<Double> accelerationLowerShaft =
            lowerShaftNamespace.addConstantDouble("acceleration lower shaft", 0);
    private final Supplier<Double> maxVelocityLowerShaft =
            lowerShaftNamespace.addConstantDouble("max velocity lower shaft", 0);
    private final Supplier<Integer> curveLowerShaft =
            lowerShaftNamespace.addConstantInt("curve lower shaft", 0);

    private final PIDSettings lowerShaftPIDSettings =
            new PIDSettings(kPLowerShaft, kILowerShaft, kDLowerShaft, toleranceLowerShaft, waitTimeLowerShaft);
    private final FeedForwardSettings lowerShaftFeedForwardSettings =
            new FeedForwardSettings(kSLowerShaft, kVLowerShaft, kALowerShaft, kGLowerShaft);
    private final TrapezoidProfileSettings lowerShaftTrapezoidProfileSettings =
            new TrapezoidProfileSettings(accelerationLowerShaft, maxVelocityLowerShaft, curveLowerShaft);

    private final Namespace higherShaftNamespace = namespace.addChild("higher shaft");
    private final Supplier<Double> kPHigherShaft = higherShaftNamespace.addConstantDouble("kP higher shaft", 0);
    private final Supplier<Double> kIHigherShaft = higherShaftNamespace.addConstantDouble("kI higher shaft", 0);
    private final Supplier<Double> kDHigherShaft = higherShaftNamespace.addConstantDouble("kD higher shaft", 0);
    private final Supplier<Double> toleranceHigherShaft =
            higherShaftNamespace.addConstantDouble("tolerance higher shaft", 0);
    private final Supplier<Double> waitTimeHigherShaft =
            higherShaftNamespace.addConstantDouble("wait time higher shaft", 0);

    private final Supplier<Double> kSHigherShaft = higherShaftNamespace.addConstantDouble("kS higher shaft", 0);
    private final Supplier<Double> kVHigherShaft = higherShaftNamespace.addConstantDouble("kV higher shaft", 0);
    private final Supplier<Double> kAHigherShaft = higherShaftNamespace.addConstantDouble("kA higher shaft", 0);
    private final Supplier<Double> kGHigherShaft = higherShaftNamespace.addConstantDouble("kG higher shaft", 0);

    private final Supplier<Double> accelerationHigherShaft =
            lowerShaftNamespace.addConstantDouble("acceleration higher shaft", 0);
    private final Supplier<Double> maxVelocityHigherShaft =
            lowerShaftNamespace.addConstantDouble("max velocity higher shaft", 0);
    private final Supplier<Integer> curveHigherShaft =
            lowerShaftNamespace.addConstantInt("curve higher shaft", 0);

    private final PIDSettings higherShaftPIDSettings =
            new PIDSettings(kPHigherShaft, kIHigherShaft, kDHigherShaft, toleranceHigherShaft, waitTimeHigherShaft);
    private final FeedForwardSettings higherShaftFeedForwardSettings =
            new FeedForwardSettings(kSHigherShaft, kVHigherShaft, kAHigherShaft, kGHigherShaft);
    private final TrapezoidProfileSettings higherShaftTrapezoidProfileSettings =
            new TrapezoidProfileSettings(accelerationHigherShaft, maxVelocityHigherShaft, curveHigherShaft);


    public static Arm getInstance() {
        return null;
    }

    public Arm(Namespace namespace, SparkMaxGenericSubsystem higherShaft, SparkMaxGenericSubsystem lowerShaft) {
        super(namespace);
        this.lowerShaft = lowerShaft;
        this.higherShaft = higherShaft;
    }

    public enum State {
        Pickup(0, 0), PLACE_FIRST_CUBE(0, 0), PLACE_SECOND_CUBE(0, 0),
        PLACE_THIRD_CUBE(0, 0);

        private final double alpha;
        private final double beta;

        State(double alpha, double beta) {
            this.alpha = alpha;
            this.beta = beta;
        }

        public double getLowerShaftTicks() {
            return 0;
        }

        public double getHigherShaftTicks() {
            return 0;
        }
    }

    @Override
    public void configureDashboard() {

    }

    public SparkMaxGenericSubsystem getLowerShaft() {
        return lowerShaft;
    }

    public SparkMaxGenericSubsystem getHigherShaft() {
        return higherShaft;
    }

    public PIDSettings getLowerShaftPIDSettings() {
        return lowerShaftPIDSettings;
    }

    public FeedForwardSettings getLowerShaftFeedForwardSettings() {
        return lowerShaftFeedForwardSettings;
    }

    public PIDSettings getHigherShaftPIDSettings() {
        return higherShaftPIDSettings;
    }

    public FeedForwardSettings getHigherShaftFeedForwardSettings() {
        return higherShaftFeedForwardSettings;
    }

    public TrapezoidProfileSettings getLowerShaftTrapezoidProfileSettings() {
        return lowerShaftTrapezoidProfileSettings;
    }

    public TrapezoidProfileSettings getHigherShaftTrapezoidProfileSettings() {
        return higherShaftTrapezoidProfileSettings;
    }
}
