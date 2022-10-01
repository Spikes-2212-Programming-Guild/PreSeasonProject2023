package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.RobotMap;

public class Claw extends SubsystemBase {

    private static Claw instance;

    private final DoubleSolenoid solenoid;

    public static Claw getInstance() {
        if (instance == null) {
            instance = new Claw();
        }
        return instance;
    }

    private Claw() {
        solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.PCM.CLAW_SOLENOID_FORWARD,
                RobotMap.PCM.CLAW_SOLENOID_REVERSE);
    }

    public InstantCommand solenoidForward() {
        return new InstantCommand(() -> solenoid.set(DoubleSolenoid.Value.kForward));
    }

    public InstantCommand solenoidReverse() {
        return new InstantCommand(() -> solenoid.set(DoubleSolenoid.Value.kReverse));
    }
}
