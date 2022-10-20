package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class CenterOnCube extends DriveArcadeWithPID {

    public CenterOnCube(Drivetrain drivetrain, Vision vision) {
        super(drivetrain, vision::photonvisionYaw, 0, 0, drivetrain.getCameraPIDSettings());
    }


}
