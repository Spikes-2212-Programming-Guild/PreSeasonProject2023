package frc.robot.subsystems;

import com.spikes2212.command.DashboardedSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

/**
 * Controls the claw that is responsible for picking up cubes.
 */
public class Claw extends DashboardedSubsystem {

    private static Claw instance;

    private final DoubleSolenoid solenoid;

    public static Claw getInstance() {
        if (instance == null) {
            instance = new Claw("claw", new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.PCM.CLAW_SOLENOID_FORWARD,
                    RobotMap.PCM.CLAW_SOLENOID_REVERSE));
        }
        return instance;
    }

    private Claw(String namespaceName, DoubleSolenoid solenoid) {
        super(namespaceName);
        this.solenoid = solenoid;
    }

    public InstantCommand solenoidForward() {
        return new InstantCommand(() -> solenoid.set(DoubleSolenoid.Value.kForward));
    }

    public InstantCommand solenoidReverse() {
        return new InstantCommand(() -> solenoid.set(DoubleSolenoid.Value.kReverse));
    }

    @Override
    public void configureDashboard() {
        namespace.putData("solenoid forward", solenoidForward());
        namespace.putData("solenoid reverse", solenoidReverse());
        namespace.putString("solenoid value", solenoid.get()::toString);
    }
}
