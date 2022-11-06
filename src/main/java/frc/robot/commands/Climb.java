package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveTank;
import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.control.noise.NoiseReducer;
import com.spikes2212.dashboard.Namespace;
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
            namespace.addConstantDouble("back away from deck distance", -10);

    private static final Supplier<Double> REQUIRED_PITCH_RATE =
            namespace.addConstantDouble("required pitch rate", 4);

    private static final Supplier<Double> GET_WHEELS_ON_DECK_DISTANCE =
            namespace.addConstantDouble("get wheels on deck distance", 70);


    private static final Supplier<Double> OPEN_SOLENOID_WAIT_TIME =
            namespace.addConstantDouble("open solenoid wait time", 0.5);

    private static final Supplier<Double> DISTANCE_TO_GET_ON_DECK =
            namespace.addConstantDouble("distance to get on deck", 50);

    //in cm
    private static final Supplier<Double> DISTANCE_TO_MOVE_ON_DECK =
            namespace.addConstantDouble("distance to move on deck", 100);

    public Climb(Drivetrain drivetrain, Climber climber) {
        addCommands(
                new InstantCommand(() -> namespace.putNumber("step", 1)),
                backAwayFromDeck(drivetrain),
                new InstantCommand(() -> namespace.putNumber("step", 2)),
                openFrontSolenoid(climber),
                new InstantCommand(() -> namespace.putNumber("step", 3)),
                getWheelsAboveDeck(drivetrain),
                new InstantCommand(() -> namespace.putNumber("step", 4)),
                getOnDeck(drivetrain, climber),
                new InstantCommand(() -> namespace.putNumber("step", 5)),
                openBackSolenoid(climber),
                new InstantCommand(() -> namespace.putNumber("step", 6)),
                getFullyOnDeck(drivetrain),
                new InstantCommand(() -> namespace.putNumber("step", 7)),
                closeBackSolenoid(climber),
                new InstantCommand(() -> namespace.putNumber("step", 8))
                );
    }

    private Command getFullyOnDeck(Drivetrain drivetrain) {
        return resetEncoders(drivetrain).andThen(
                new DriveTank(drivetrain, DRIVE_SPEED, DRIVE_SPEED).until(
                        () -> drivetrain.getLeftEncoderPosition() > DISTANCE_TO_MOVE_ON_DECK.get() &&
                                drivetrain.getRightEncoderPosition() > DISTANCE_TO_MOVE_ON_DECK.get()
                )
        );
    }

    private InstantCommand resetEncoders(Drivetrain drivetrain) {
        return new InstantCommand(drivetrain::resetEncoders);
    }

    private Command backAwayFromDeck(Drivetrain drivetrain) {
        return resetEncoders(drivetrain).andThen(
                new DriveTank(drivetrain, -DRIVE_SPEED, -DRIVE_SPEED).until(
                        () -> drivetrain.getLeftEncoderPosition() < BACK_AWAY_FROM_DECK_DISTANCE.get() &&
                                drivetrain.getRightEncoderPosition() < BACK_AWAY_FROM_DECK_DISTANCE.get()
                )
        );
    }

    private Command openFrontSolenoid(Climber climber) {
        return climber.openFrontSolenoid().andThen(new WaitCommand(OPEN_SOLENOID_WAIT_TIME.get()));
    }

    private Command getWheelsAboveDeck(Drivetrain drivetrain) {
        return resetEncoders(drivetrain).andThen(
                new DriveTank(drivetrain, DRIVE_SPEED, DRIVE_SPEED).until(
                        () -> drivetrain.getLeftEncoderPosition() > GET_WHEELS_ON_DECK_DISTANCE.get() &&
                                drivetrain.getRightEncoderPosition() > GET_WHEELS_ON_DECK_DISTANCE.get()
                )
        );
    }

    private Command openBackSolenoid(Climber climber) {
        return climber.openBackSolenoid();
    }

    private InstantCommand closeFrontSolenoid(Climber climber) {
        return climber.closeFrontSolenoid();
    }

    private Command getOnDeck(Drivetrain drivetrain, Climber climber) {
        return resetEncoders(drivetrain).andThen(
                new ParallelCommandGroup(
                        closeFrontSolenoid(climber),
                        new DriveTank(drivetrain, DRIVE_SPEED, DRIVE_SPEED).until(
                                () -> drivetrain.getLeftEncoderPosition() > DISTANCE_TO_GET_ON_DECK.get() &&
                                        drivetrain.getRightEncoderPosition() > DISTANCE_TO_GET_ON_DECK.get()
                        )
                )
        );
    }

    private InstantCommand closeBackSolenoid(Climber climber) {
        return climber.closeBackSolenoid();
    }

    public static void periodic() {
        namespace.update();
    }
}
