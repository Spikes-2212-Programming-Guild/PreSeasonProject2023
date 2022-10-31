package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;

import java.util.function.Supplier;

public class Climb extends SequentialCommandGroup {

    public static final double DRIVE_SPEED = 0.3;

    private static final RootNamespace namespace = new RootNamespace("climb command");

    /**
     * The distance from the deck in which the {@link Climber} back solenoid needs to open in cm.
     */
    private static final Supplier<Double> BACK_AWAY_FROM_DECK_DISTANCE =
            namespace.addConstantDouble("back away from deck distance", -30);

    private static final Supplier<Double> REQUIRED_PITCH_RATE =
            namespace.addConstantDouble("required pitch rate", 4);

    //in cm
    private static final Supplier<Double> DISTANCE_TO_MOVE_ON_DECK =
            namespace.addConstantDouble("distance to move on deck", 70);

    private static final Supplier<Double> GET_PARTIALLY_ON_DECK_TIMEOUT =
            namespace.addConstantDouble("get partially on deck timeout", 0.5);

    private static final Supplier<Double> OPEN_SOLENOID_WAIT_TIME =
            namespace.addConstantDouble("open solenoid wait time", 0.5);

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

    private Command backAwayFromDeck(Drivetrain drivetrain) {
        return resetEncoders(drivetrain).andThen(new DriveTankWithPID(drivetrain, drivetrain.getShortDrivePIDSettings(),
                drivetrain.getShortDrivePIDSettings(), BACK_AWAY_FROM_DECK_DISTANCE,
                BACK_AWAY_FROM_DECK_DISTANCE, drivetrain::getLeftEncoderPosition,
                drivetrain::getRightEncoderPosition));
    }

    private Command openFrontSolenoid(Climber climber) {
        return climber.openFrontSolenoid().andThen(new WaitCommand(OPEN_SOLENOID_WAIT_TIME.get()));
    }

    private Command getPartiallyOnDeck(Drivetrain drivetrain) {
        return new DriveArcade(drivetrain, DRIVE_SPEED, 0)
                .until(() -> drivetrain.getPitchRate() > REQUIRED_PITCH_RATE.get())
                .andThen(
                        new DriveArcade(drivetrain, DRIVE_SPEED, 0)
                        .withTimeout(GET_PARTIALLY_ON_DECK_TIMEOUT.get())
                );
    }

    private Command getFullyOnDeck(Drivetrain drivetrain) {
        return resetEncoders(drivetrain).andThen(new DriveTankWithPID(drivetrain, drivetrain.getShortDrivePIDSettings(),
                drivetrain.getShortDrivePIDSettings(), DISTANCE_TO_MOVE_ON_DECK, DISTANCE_TO_MOVE_ON_DECK,
                drivetrain::getLeftEncoderPosition, drivetrain::getRightEncoderPosition));
    }

    private Command openBackSolenoid(Climber climber) {
        return climber.openBackSolenoid();
    }

    private InstantCommand resetEncoders(Drivetrain drivetrain) {
        return new InstantCommand(drivetrain::resetEncoders);
    }

    private InstantCommand closeFrontSolenoid(Climber climber) {
        return climber.closeFrontSolenoid();
    }

    private InstantCommand closeBackSolenoid(Climber climber) {
        return climber.closeBackSolenoid();
    }
}
