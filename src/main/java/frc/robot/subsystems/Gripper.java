package frc.robot.subsystems;

import com.spikes2212.command.DashboardedSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
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

    private final DigitalInput limit;

    public static Gripper getInstance() {
        if (instance == null) {
            instance = new Gripper("gripper", new DoubleSolenoid(PneumaticsModuleType.CTREPCM,
                    RobotMap.PCM.GRIPPER_SOLENOID_FORWARD, RobotMap.PCM.GRIPPER_SOLENOID_REVERSE),
                    new DigitalInput(RobotMap.DIO.GRIPPER_LIMIT));
        }
        return instance;
    }

    private Gripper(String namespaceName, DoubleSolenoid solenoid, DigitalInput limit) {
        super(namespaceName);
        this.solenoid = solenoid;
        this.limit = limit;
        configureDashboard();
    }

    public InstantCommand openSolenoid() {
        return new InstantCommand(() -> {
            solenoid.set(DoubleSolenoid.Value.kForward);
            namespace.putString("solenoid value", solenoid.get()::toString);
        });
    }

    public InstantCommand closeSolenoid() {
        return new InstantCommand(() -> {
            solenoid.set(DoubleSolenoid.Value.kReverse);
            namespace.putString("solenoid value", solenoid.get()::toString);
        });
    }

    public boolean getLimit() {
        return limit.get();
    }

    @Override
    public void configureDashboard() {
        namespace.putData("open solenoid", openSolenoid());
        namespace.putData("close solenoid", closeSolenoid());
        namespace.putBoolean("limit", this::getLimit);
        namespace.putString("solenoid value", solenoid.get()::toString);
    }
}
