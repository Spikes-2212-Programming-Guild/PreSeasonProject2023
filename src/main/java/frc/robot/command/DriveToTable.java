package frc.robot.command;

import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.RootNamespace;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

public class DriveToTable extends DriveTankWithPID {

    private static RootNamespace namespace = new RootNamespace("drive to table");
    private static final Supplier<Double> namespaceLeftSetPoint
            = namespace.addConstantDouble("left setpoint", 0);
    private static final Supplier<Double> namespaceLeftSource = namespace.addConstantDouble("left source", 0);
    private static final Supplier<Double> namespaceRightSource =
            namespace.addConstantDouble("right source", 0);
    private static final Supplier<Double> namespaceRightSetPoint
            = namespace.addConstantDouble("right setpoint", 0);

    public DriveToTable(Drivetrain drivetrain) {
        super(drivetrain, drivetrain.getPidSettingsDrive(), drivetrain.getPidSettingsDrive(), namespaceLeftSetPoint,
                namespaceRightSetPoint, namespaceLeftSource, namespaceRightSource);
    }
}
