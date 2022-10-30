package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

public class Climb extends SequentialCommandGroup {

    public static final double DRIVE_SPEED = 0.3;

    private static final RootNamespace namespace = new RootNamespace("climb command");

    /**
     * The distance from the deck in which the {@link Climber} front solenoids need to open.
     */
    private static final Supplier<Double> OPEN_FRONT_SOLENOIDS_ULTRASONIC_IN_CM =
            namespace.addConstantDouble("open front solenoids ultrasonic in cm", 50);

    /**
     * The distance from the deck in which the {@link Climber} back solenoid needs to open.
     */
    private static final Supplier<Double> OPEN_BACK_SOLENOID_DISTANCE_IN_CM =
            namespace.addConstantDouble("open back solenoid distance in cm", 30);

    /**
     * The distance from the deck in which the {@link Climber} back solenoid needs to close.
     */
    private static final Supplier<Double> CLOSE_BACK_SOLENOID_DISTANCE_IN_CM =
            namespace.addConstantDouble("close back solenoid distance in cm", 15);

    private static final Supplier<Double> HIT_WALL_ACCELERATION =
            namespace.addConstantDouble("hit wall acceleration", 0);

    /**
     * The maximum amount of centimeters that the robot is allowed to miss the target by.
     */
    private static final Supplier<Double> ULTRASONIC_TOLERANCE =
            namespace.addConstantDouble("ultrasonic tolerance", 3);

    private final Drivetrain drivetrain;
    private final Climber climber;

    public Climb(Drivetrain drivetrain, Climber climber) {
        this.drivetrain = drivetrain;
        this.climber = climber;
        addRequirements(drivetrain, climber);
        addCommands(
                backAwayFromDeck(drivetrain),
                climber.openFrontSolenoid(),
                driveForward(drivetrain, OPEN_BACK_SOLENOID_ULTRASONIC_IN_CM),
                climber.openBackSolenoid(),
                climber.closeFrontSolenoid(),
                driveForward(drivetrain, CLOSE_BACK_SOLENOID_ULTRASONIC_IN_CM),
                climber.closeBackSolenoid()
        );
    }

    private ParallelRaceGroup backAwayFromDeck(Drivetrain drivetrain) {
        return new DriveTankWithPID(drivetrain, drivetrain.getZoomToTablePIDSettings(), drivetrain.getZoomToTablePIDSettings(),
                );
    }

    private ParallelRaceGroup driveToDeck() {
        return new DriveArcade(drivetrain, DRIVE_SPEED, 0).withInterrupt(this::hitDeck);
    }

    private

    private boolean hitDeck() {
        return drivetrain.getAccelerometerValue() < HIT_WALL_ACCELERATION.get();
    }
}
