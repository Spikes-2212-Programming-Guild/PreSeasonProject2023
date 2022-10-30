package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.dashboard.RootNamespace;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

public class DriveToTable extends DriveTankWithPID {

    private static final RootNamespace namespace = new RootNamespace("drive to table");

    private static final Supplier<Double> leftSetpoint = namespace.addConstantDouble("left setpoint", 0);
    private static final Supplier<Double> rightSetpoint = namespace.addConstantDouble("right setpoint", 0);

    public DriveToTable(Drivetrain drivetrain) {
        super(drivetrain, drivetrain.getZoomToTablePIDSettings(), drivetrain.getZoomToTablePIDSettings(), leftSetpoint,
                rightSetpoint, drivetrain::getLeftEncoderPosition, drivetrain::getRightEncoderPosition);
    }

    @Override
    public void initialize() {
        ((Drivetrain) drivetrain).resetEncoders();
    }
}
