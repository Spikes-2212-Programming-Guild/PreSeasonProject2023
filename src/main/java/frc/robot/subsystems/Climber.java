package frc.robot.subsystems;

import com.spikes2212.command.DashboardedSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

/**
 * Controls the three {@link DoubleSolenoid}s that make the robot climb.
 */
public class Climber extends DashboardedSubsystem {

    private static Climber instance;

    private final DoubleSolenoid frontSolenoid;
    private final DoubleSolenoid backSolenoid;

    public static Climber getInstance() {
        if (instance == null) {
            instance = new Climber("climber",
                    new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.PCM.CLIMBER_FRONT_SOLENOID_FORWARD,
                            RobotMap.PCM.CLIMBER_FRONT_SOLENOID_REVERSE),
                    new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.PCM.CLIMBER_BACK_SOLENOID_FORWARD,
                            RobotMap.PCM.CLIMBER_BACK_SOLENOID_REVERSE)
            );
        }
        return instance;
    }

    private Climber(String namespaceName, DoubleSolenoid frontSolenoid, DoubleSolenoid backSolenoid) {
        super(namespaceName);
        this.frontSolenoid = frontSolenoid;
        this.backSolenoid = backSolenoid;
    }

    public InstantCommand openFrontSolenoid() {
        return new InstantCommand(() ->
        {
            frontSolenoid.set(DoubleSolenoid.Value.kForward);
        });
    }

    public InstantCommand closeFrontSolenoid() {
        return new InstantCommand(() ->
        {
            frontSolenoid.set(DoubleSolenoid.Value.kReverse);
        });
    }

    public InstantCommand openBackSolenoid() {
        return new InstantCommand(() -> backSolenoid.set(DoubleSolenoid.Value.kForward));
    }

    public InstantCommand closeBackSolenoid() {
        return new InstantCommand(() -> backSolenoid.set(DoubleSolenoid.Value.kReverse));
    }

    @Override
    public void configureDashboard() {
        namespace.putData("front solenoid forward", openFrontSolenoid());
        namespace.putData("front solenoid reverse", closeFrontSolenoid());
        namespace.putData("back solenoid forward", openBackSolenoid());
        namespace.putData("back solenoid reverse", closeBackSolenoid());
        namespace.putString("front solenoid 1 value", frontSolenoid.get()::toString);
        namespace.putString("back solenoid 1 value", backSolenoid.get()::toString);
    }
}
