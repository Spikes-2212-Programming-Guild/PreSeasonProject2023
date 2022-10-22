package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Gripper;

/**
 * Closes the gripper to pick up cubes.
 *
 * @see Gripper
 */
public class CloseGripper extends InstantCommand {

    public CloseGripper(Gripper gripper) {
        addRequirements(gripper);
        gripper.closeSolenoid();
    }
}
