package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Gripper;

/**
 * Opens the gripper to drop cubes.
 *
 * @see Gripper
 */

public class OpenGripper extends InstantCommand {

    public OpenGripper(Gripper gripper) {
        addRequirements(gripper);
        gripper.openSolenoid();
    }
}
