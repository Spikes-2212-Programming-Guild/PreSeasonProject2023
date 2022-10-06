package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.smartmotorcontrollersubsystem.MoveSmartMotorControllerSubsystem;
import com.spikes2212.command.genericsubsystem.commands.smartmotorcontrollersubsystem.MoveSmartMotorControllerSubsystemTrapezically;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import subsystems.Arm;

public class MoveArm extends ParallelCommandGroup {

    public MoveArm(Arm arm, Arm.State state) {
        addCommands(moveLowerShaft(arm, state), moveHigherShaft(arm, state));
    }

    public Command moveLowerShaft(Arm arm, Arm.State state) {
        return new MoveSmartMotorControllerSubsystemTrapezically(arm.getLowerShaft(), arm.getLowerShaftPIDSettings(),
                arm.getLowerShaftFeedForwardSettings(), state::getLowerShaftTicks,
                arm.getLowerShaftTrapezoidProfileSettings());
    }

    public Command moveHigherShaft(Arm arm, Arm.State state) {
        return new MoveSmartMotorControllerSubsystemTrapezically(arm.getHigherShaft(), arm.getHigherShaftPIDSettings(),
                arm.getHigherShaftFeedForwardSettings(), state::getHigherShaftTicks,
                arm.getHigherShaftTrapezoidProfileSettings());
    }
}
