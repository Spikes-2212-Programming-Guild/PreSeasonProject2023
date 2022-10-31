package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.command.drivetrains.commands.DriveArcadeWithPID;
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
    private static final Supplier<Double> OPEN_BACK_SOLENOID_DISTANCE =
            namespace.addConstantDouble("open back solenoid distance", -30);

    private static final Supplier<Double> PITCH_ANGLE =
            namespace.addConstantDouble("pitch angle", 12);

    //in cm
    private static final Supplier<Double> DISTANCE_TO_MOVE_ON_DECK =
            namespace.addConstantDouble("distance to move on deck", 70);

    public Climb(Drivetrain drivetrain, Climber climber) {
        addCommands(
                backAwayFromDeck(drivetrain),
                openFrontSolenoid(drivetrain, climber),
                getPartiallyOnDeck(drivetrain),
                openBackSolenoid(drivetrain, climber),
                closeFrontSolenoid(climber),
                getFullyOnDeck(drivetrain),
                closeBackSolenoid(climber)
        );
    }

    private Command backAwayFromDeck(Drivetrain drivetrain) {
        return resetEncoders(drivetrain).andThen(new DriveTankWithPID(drivetrain, drivetrain.getShortDrivePIDSettings(),
                drivetrain.getShortDrivePIDSettings(), OPEN_BACK_SOLENOID_DISTANCE,
                OPEN_BACK_SOLENOID_DISTANCE, drivetrain::getLeftEncoderPosition,
                drivetrain::getRightEncoderPosition));
    }

    private Command openFrontSolenoid(Drivetrain drivetrain, Climber climber) {
        return climber.openFrontSolenoid()
                .andThen(new WaitUntilCommand(() -> drivetrain.getPitch() < PITCH_ANGLE.get()));
    }

    private Command getPartiallyOnDeck(Drivetrain drivetrain) {
        return new DriveArcade(drivetrain, DRIVE_SPEED, 0).until(() -> drivetrain.getPitchAceleration() > 0);
    }

    private Command getFullyOnDeck(Drivetrain drivetrain) {
        return resetEncoders(drivetrain).andThen(new DriveTankWithPID(drivetrain, drivetrain.getShortDrivePIDSettings(),
                drivetrain.getShortDrivePIDSettings(), DISTANCE_TO_MOVE_ON_DECK, DISTANCE_TO_MOVE_ON_DECK,
                drivetrain::getLeftEncoderPosition, drivetrain::getRightEncoderPosition));
    }

    private Command openBackSolenoid(Drivetrain drivetrain, Climber climber) {
        return climber.openBackSolenoid().andThen(new WaitUntilCommand(() -> drivetrain.getPitch() == 0));
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
