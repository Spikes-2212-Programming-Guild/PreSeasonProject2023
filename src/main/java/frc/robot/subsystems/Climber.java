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

    private final DoubleSolenoid frontSolenoid1;
    private final DoubleSolenoid frontSolenoid2;
    private final DoubleSolenoid backSolenoid;

    public static Climber getInstance() {
        if (instance == null) {
            instance = new Climber("climber",
                    new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.PCM.CLIMBER_FRONT_SOLENOID_1_FORWARD,
                            RobotMap.PCM.CLIMBER_FRONT_SOLENOID_1_REVERSE),
                    new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.PCM.CLIMBER_FRONT_SOLENOID_2_FORWARD,
                            RobotMap.PCM.CLIMBER_FRONT_SOLENOID_2_REVERSE),
                    new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.PCM.CLIMBER_BACK_SOLENOID_FORWARD,
                            RobotMap.PCM.CLIMBER_BACK_SOLENOID_REVERSE)
            );
        }
        return instance;
    }

    private Climber(String namespaceName, DoubleSolenoid frontSolenoid1, DoubleSolenoid frontSolenoid2, DoubleSolenoid backSolenoid) {
        super(namespaceName);
        this.frontSolenoid1 = frontSolenoid1;
        this.frontSolenoid2 = frontSolenoid2;
        this.backSolenoid = backSolenoid;
    }

    public InstantCommand openFrontSolenoids() {
        return new InstantCommand(() ->
        {
            frontSolenoid1.set(DoubleSolenoid.Value.kForward);
            frontSolenoid2.set(DoubleSolenoid.Value.kForward);
        });
    }

    public InstantCommand closeFrontSolenoids() {
        return new InstantCommand(() ->
        {
            frontSolenoid1.set(DoubleSolenoid.Value.kReverse);
            frontSolenoid2.set(DoubleSolenoid.Value.kReverse);
        });
    }

    public InstantCommand openBackSolenoids() {
        return new InstantCommand(() -> backSolenoid.set(DoubleSolenoid.Value.kForward));
    }

    public InstantCommand closeBackSolenoids() {
        return new InstantCommand(() -> backSolenoid.set(DoubleSolenoid.Value.kReverse));
    }

    @Override
    public void configureDashboard() {
        namespace.putData("front solenoids forward", openFrontSolenoids());
        namespace.putData("front solenoids reverse", closeFrontSolenoids());
        namespace.putData("back solenoids forward", openBackSolenoids());
        namespace.putData("back solenoids reverse", closeBackSolenoids());
        namespace.putString("front solenoid 1 value", frontSolenoid1.get()::toString);
        namespace.putString("front solenoid 2 value", frontSolenoid2.get()::toString);
        namespace.putString("back solenoid 1 value", backSolenoid.get()::toString);
    }
}
