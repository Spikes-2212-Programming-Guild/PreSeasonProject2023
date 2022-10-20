package frc.robot.command;

import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.dashboard.RootNamespace;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

public class DriveToTable extends DriveTankWithPID {

    private static RootNamespace namespace = new RootNamespace("drive to table");
    private static final Supplier<Double> LeftSetpoint
            = namespace.addConstantDouble("left setpoint", 0);
    private static final Supplier<Double> LeftSource = namespace.addConstantDouble("left source", 0);
    private static final Supplier<Double> RightSource =
            namespace.addConstantDouble("right source", 0);
    private static final Supplier<Double> RightSetpoint
            = namespace.addConstantDouble("right setpoint", 0);

    public DriveToTable(Drivetrain drivetrain) {
        super(drivetrain, drivetrain.getPIDSettingsDrive(), drivetrain.getPIDSettingsDrive(), LeftSetpoint,
                RightSetpoint, LeftSource, RightSource);
    }
}
