package frc.robot.subsystems;

import com.spikes2212.command.DashboardedSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

/**
 * Controls the gripper that is responsible for holding cubes.
 */
public class Gripper extends DashboardedSubsystem {

    private static Gripper instance;

    private final DoubleSolenoid solenoid;

    public static Gripper getInstance() {
        if (instance == null) {
            instance = new Gripper("gripper", new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.PCM.CLAW_SOLENOID_FORWARD,
                    RobotMap.PCM.CLAW_SOLENOID_REVERSE));
        }
        return instance;
    }

    private Gripper(String namespaceName, DoubleSolenoid solenoid) {
        super(namespaceName);
        this.solenoid = solenoid;
        configureDashboard();
    }

    public InstantCommand openSolenoid() {
        return new InstantCommand(() -> solenoid.set(DoubleSolenoid.Value.kForward));
    }

    public InstantCommand closeSolenoid() {
        return new InstantCommand(() -> solenoid.set(DoubleSolenoid.Value.kReverse));
    }

    @Override
    public void configureDashboard() {
        namespace.putData("open solenoid", openSolenoid());
        namespace.putData("close solenoid", closeSolenoid());
        namespace.putString("solenoid value", solenoid.get()::toString);
    }
}
