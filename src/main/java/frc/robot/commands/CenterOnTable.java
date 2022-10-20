package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class CenterOnTable extends DriveArcadeWithPID {
    public CenterOnTable(Drivetrain drivetrain, Vision vision) {
        super(drivetrain, vision::limelightYaw, 0, 0, drivetrain.getCameraPIDSettings());
    }
}
