package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Gripper;

/**
 * Closes the gripper to pick cubes.
 *
 * @see Gripper
 */
public class CloseGripper extends InstantCommand {

    public CloseGripper(Gripper gripper) {
        addRequirements((Subsystem) gripper);
        gripper.closeSolenoid();
    }
}
