package frc.robot.commands;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
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

    public final double DRIVE_SPEED = 0.4;

    private static final RootNamespace namespace = new RootNamespace("climb command");

    /**
     * The distance from the deck in which the {@link Climber} back solenoid needs to open in cm.
     */
    private static final Supplier<Double> BACK_AWAY_FROM_DECK_DISTANCE =
            namespace.addConstantDouble("back away from deck distance", -10);

    private static final Supplier<Double> OPEN_SOLENOID_WAIT_TIME =
            namespace.addConstantDouble("open solenoid wait time", 0.5);

    //in cm
    private static final Supplier<Double> DISTANCE_TO_MOVE_ON_DECK =
            namespace.addConstantDouble("distance to move on deck", 100);

    public Climb(Drivetrain drivetrain, Climber climber) {
        addCommands(
                getWheelsAboveDeck(drivetrain, climber),
                getOnDeck(drivetrain, climber),
                openBackSolenoid(climber),
                getFullyOnDeck(drivetrain),
                closeBackSolenoid(climber),
                new DriveArcade(drivetrain, DRIVE_SPEED, 0.0).withTimeout(0.05)
        );
    }

    private Command getFullyOnDeck(Drivetrain drivetrain) {
        return resetEncoders(drivetrain).andThen(
                new DriveTank(drivetrain, DRIVE_SPEED, DRIVE_SPEED).until(
                        () -> drivetrain.getLeftEncoderPosition() > DISTANCE_TO_MOVE_ON_DECK.get() &&
                                drivetrain.getRightEncoderPosition() > DISTANCE_TO_MOVE_ON_DECK.get()
                ).withTimeout(6)
        );
    }

    private InstantCommand resetEncoders(Drivetrain drivetrain) {
        return new InstantCommand(drivetrain::resetEncoders);
    }

    private Command openFrontSolenoid(Climber climber) {
        return climber.openFrontSolenoid().andThen(new WaitCommand(OPEN_SOLENOID_WAIT_TIME.get()));
    }

    private Command getWheelsAboveDeck(Drivetrain drivetrain, Climber climber) {
        return new SequentialCommandGroup(
                new DriveTank(drivetrain, DRIVE_SPEED, DRIVE_SPEED) {
                    @Override
                    public void end(boolean i) {
                    }
                }.withTimeout(0.3),
                openFrontSolenoid(climber),
                new DriveTank(drivetrain, DRIVE_SPEED, DRIVE_SPEED).withTimeout(1)
        );
    }

    private Command openBackSolenoid(Climber climber) {
        return climber.openBackSolenoid().andThen(new WaitCommand(0.5));
    }

    private InstantCommand closeFrontSolenoid(Climber climber) {
        return climber.closeFrontSolenoid();
    }

    private Command getOnDeck(Drivetrain drivetrain, Climber climber) {
        return resetEncoders(drivetrain).andThen(
                new ParallelCommandGroup(
                        closeFrontSolenoid(climber),
                        new DriveTank(drivetrain, DRIVE_SPEED, DRIVE_SPEED).withTimeout(1)));
    }

    private InstantCommand closeBackSolenoid(Climber climber) {
        return climber.closeBackSolenoid();
    }

    public static void periodic() {
        namespace.update();
    }
}
