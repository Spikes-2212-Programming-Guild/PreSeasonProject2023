package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Claw;

/**
 * Closes the claw to pick cubes.
 */
public class PickCube extends CommandBase {

    public PickCube(Claw claw) {
        addRequirements(claw);
        claw.closeSolenoid();
    }
}
