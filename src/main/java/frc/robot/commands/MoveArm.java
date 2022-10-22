package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.smartmotorcontrollersubsystem.MoveSmartMotorControllerSubsystemTrapezically;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.State;

public class MoveArm extends ParallelCommandGroup {

    public MoveArm(Arm lowerShaft, Arm upperShaft, State state) {
        double alpha = state.getAlpha();
        double beta = state.getBeta();
        addRequirements(lowerShaft, upperShaft);
        addCommands(
                new MoveSmartMotorControllerSubsystemTrapezically(lowerShaft, lowerShaft.getPIDSettings(),
                        lowerShaft.getFeedForwardSettings(), () -> alpha,
                        lowerShaft.getTrapezoidProfileSettings()),
                new MoveSmartMotorControllerSubsystemTrapezically(upperShaft, upperShaft.getPIDSettings(),
                        upperShaft.getFeedForwardSettings(), () -> (beta - alpha),
                        upperShaft.getTrapezoidProfileSettings())
        );
    }
}
