package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

import java.util.function.Supplier;

public class DriveToTable extends SequentialCommandGroup {

    private static final RootNamespace namespace = new RootNamespace("drive to table");

    private static final Supplier<Double> leftSetpoint = namespace.addConstantDouble("left setpoint", 0);
    private static final Supplier<Double> rightSetpoint = namespace.addConstantDouble("right setpoint", 0);

    private final Drivetrain drivetrain;

    public DriveToTable(Drivetrain drivetrain, Vision vision) {
        addCommands(driveTankWithPID(drivetrain), new CenterOnTable(drivetrain, vision));
        this.drivetrain = drivetrain;
    }

    @Override
    public void initialize() {
        drivetrain.resetEncoders();
    }

    private DriveTankWithPID driveTankWithPID(Drivetrain drivetrain) {
        return new DriveTankWithPID(drivetrain, drivetrain.getDrivePIDSettings(), drivetrain.getDrivePIDSettings(),
                leftSetpoint, rightSetpoint, drivetrain::getLeftEncoderPosition, drivetrain::getRightEncoderPosition);
    }
}
