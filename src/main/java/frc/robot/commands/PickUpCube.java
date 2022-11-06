package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gripper;
import frc.robot.subsystems.Vision;

public class PickUpCube extends SequentialCommandGroup {

    public static final double DRIVE_SPEED = 0.3;

    public PickUpCube(Drivetrain drivetrain, Gripper gripper, Vision vision) {
        addRequirements(drivetrain, gripper);
        addCommands(
                new CenterOnCube(drivetrain, vision),
                driveForward(drivetrain, gripper),
                new CloseGripper(gripper)
                );
    }

    public CommandBase driveForward(Drivetrain drivetrain, Gripper gripper) {
        return new DriveArcade(drivetrain, DRIVE_SPEED, 0);
                //.withInterrupt(gripper::getLimit);
    }
}
