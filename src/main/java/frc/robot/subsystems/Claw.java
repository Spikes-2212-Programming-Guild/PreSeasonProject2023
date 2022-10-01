package frc.robot.subsystems;

import com.spikes2212.command.DashboardedSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.RobotMap;

public class Claw extends DashboardedSubsystem {

    private static Claw instance;

    private final DoubleSolenoid solenoid;

    public static Claw getInstance() {
        if (instance == null) {
            instance = new Claw();
        }
        return instance;
    }

    private Claw() {
        super(new RootNamespace("claw"));
        solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.PCM.CLAW_SOLENOID_FORWARD,
                RobotMap.PCM.CLAW_SOLENOID_REVERSE);
    }

    public InstantCommand solenoidForward() {
        return new InstantCommand(() -> solenoid.set(DoubleSolenoid.Value.kForward));
    }

    public InstantCommand solenoidReverse() {
        return new InstantCommand(() -> solenoid.set(DoubleSolenoid.Value.kReverse));
    }

    @Override
    public void configureDashboard() {
        namespace.putData("claw solenoid forward", solenoidForward());
        namespace.putData("claw solenoid reverse", solenoidReverse());
        namespace.putString("claw solenoid value", solenoid.get()::toString);
    }
}
