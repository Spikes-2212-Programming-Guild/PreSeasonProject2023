package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.InstantCommand;
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
    private static final Supplier<Double> OPEN_BACK_SOLENOID_ULTRASONIC_IN_CM =
            namespace.addConstantDouble("open back solenoid ultrasonic in cm", 30);

    /**
     * The distance from the deck in which the {@link Climber} back solenoid needs to close.
     */
    private static final Supplier<Double> CLOSE_BACK_SOLENOID_ULTRASONIC_IN_CM =
            namespace.addConstantDouble("close back solenoid ultrasonic in cm", 15);

    /**
     * The maximum amount of centimeters that the robot is allowed to miss the target by.
     */
    private static final Supplier<Double> ULTRASONIC_TOLERANCE =
            namespace.addConstantDouble("ultrasonic tolerance", 3);

    public Climb(Drivetrain drivetrain, Climber climber) {
        addCommands(
                backAwayFromDeck(drivetrain),
                openFrontSolenoid(climber),
                driveForward(drivetrain, OPEN_BACK_SOLENOID_ULTRASONIC_IN_CM),
                openBackSolenoid(climber),
                closeFrontSolenoid(climber),
                driveForward(drivetrain, CLOSE_BACK_SOLENOID_ULTRASONIC_IN_CM),
                closeBackSolenoid(climber)
        );
    }

    private ParallelRaceGroup backAwayFromDeck(Drivetrain drivetrain) {
        return new DriveArcade(drivetrain, -DRIVE_SPEED, 0).withInterrupt(
                () -> Math.abs(OPEN_FRONT_SOLENOIDS_ULTRASONIC_IN_CM.get() - drivetrain.getUltrasonicDistanceInCM())
                        <= ULTRASONIC_TOLERANCE.get()
        );
    }

    private InstantCommand openFrontSolenoid(Climber climber) {
        return climber.openFrontSolenoid();
    }

    private ParallelRaceGroup driveForward(Drivetrain drivetrain, Supplier<Double> distanceFromDeck) {
        return new DriveArcade(drivetrain, DRIVE_SPEED, 0).withInterrupt(
                () -> Math.abs(distanceFromDeck.get() - drivetrain.getUltrasonicDistanceInCM()) <=
                        ULTRASONIC_TOLERANCE.get()
        );
    }

    private InstantCommand openBackSolenoid(Climber climber) {
        return climber.openBackSolenoid();
    }

    private InstantCommand closeFrontSolenoid(Climber climber) {
        return climber.closeFrontSolenoid();
    }

    private InstantCommand closeBackSolenoid(Climber climber) {
        return climber.closeBackSolenoid();
    }
}
