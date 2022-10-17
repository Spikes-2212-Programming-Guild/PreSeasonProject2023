package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Gripper;

/**
 * Closes the claw to pick cubes.
 *
 * @see Gripper
 */
public class PickCube extends InstantCommand {

    public PickCube(Gripper gripper) {
        addRequirements((Subsystem) gripper);
        gripper.closeSolenoid();
    }
}
