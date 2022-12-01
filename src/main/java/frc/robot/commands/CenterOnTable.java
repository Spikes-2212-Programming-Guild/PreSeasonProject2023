package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.dashboard.RootNamespace;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class CenterOnTable extends DriveArcadeWithPID {

    private final Vision vision;

    public CenterOnTable(Drivetrain drivetrain, Vision vision) {
        super(drivetrain, () -> -vision.getLimelightYaw(), 0, 0, drivetrain.getCameraPIDSettings());
        this.vision = vision;
    }

    @Override
    public void execute() {
        if (vision.limelightHasTarget()) {
            super.execute();
        }
        else {
            drivetrain.arcadeDrive(0, 0.3);
        }
    }
}
