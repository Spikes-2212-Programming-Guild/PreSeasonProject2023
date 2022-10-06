package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Claw;

/**
 * Opens the claw to drop cubes.
 *
 * @see Claw
 */

public class DropCube extends InstantCommand {

    public DropCube(Claw claw) {
        addRequirements(claw);
        claw.openSolenoid();
    }
}
