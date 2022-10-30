package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
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
     * The distance from the deck in which the {@link Climber} back solenoid needs to open.
     */
    private static final Supplier<Double> OPEN_BACK_SOLENOID_DISTANCE =
            namespace.addConstantDouble("open back solenoid ultrasonic in cm", -30);

    private static final Supplier<Double> PITCH_ANGLE =
            namespace.addConstantDouble("pitch angle", 12);

    private static final Supplier<Double> STALL_CURRENT =
            namespace.addConstantDouble("stall current", 19);

    public Climb(Drivetrain drivetrain, Climber climber) {
        addCommands(
                backAwayFromDeck(drivetrain),
                openFrontSolenoid(climber),
                getPartiallyOnDeck(drivetrain),
                openBackSolenoid(climber),
                closeFrontSolenoid(climber),
                getFullyOnDeck(drivetrain),
                closeBackSolenoid(climber)
        );
    }

    private DriveTankWithPID backAwayFromDeck(Drivetrain drivetrain) {
        return new DriveTankWithPID(drivetrain, drivetrain.getShortDrivePIDSettings(),
                drivetrain.getShortDrivePIDSettings(), OPEN_BACK_SOLENOID_DISTANCE,
                OPEN_BACK_SOLENOID_DISTANCE, drivetrain::getLeftEncoderPosition,
                drivetrain::getRightEncoderPosition);
    }

    private InstantCommand openFrontSolenoid(Climber climber) {
        return climber.openFrontSolenoid();
    }

    private ParallelRaceGroup getPartiallyOnDeck(Drivetrain drivetrain) {
        return new DriveArcade(drivetrain, DRIVE_SPEED, 0)
                .withInterrupt(() -> drivetrain.getPitch() >= PITCH_ANGLE.get());
    }

    private ParallelRaceGroup getFullyOnDeck(Drivetrain drivetrain) {
        return new DriveArcade(drivetrain, DRIVE_SPEED, 0)
                .withInterrupt(() -> drivetrain.getCurrent() < STALL_CURRENT.get());
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
