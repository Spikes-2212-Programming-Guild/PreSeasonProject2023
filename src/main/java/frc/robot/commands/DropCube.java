package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Gripper;

/**
 * Opens the claw to drop cubes.
 *
 * @see Gripper
 */

public class DropCube extends InstantCommand {

    public DropCube(Gripper gripper) {
        addRequirements((Subsystem) gripper);
        gripper.openSolenoid();
    }
}
