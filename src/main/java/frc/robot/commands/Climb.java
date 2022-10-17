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

    private static final Supplier<Double> OPEN_FRONT_SOLENOID_ULTRASONIC_DISTANCE =
            namespace.addConstantDouble("open front solenoid ultrasonic distance", 50);

    private static final Supplier<Double> OPEN_BACK_SOLENOID_ULTRASONIC_DISTANCE =
            namespace.addConstantDouble("open back solenoid ultrasonic distance", 30);

    private static final Supplier<Double> CLOSE_BACK_SOLENOID_ULTRASONIC_DISTANCE =
            namespace.addConstantDouble("close back solenoid ultrasonic distance", 15);

    private static final Supplier<Double> ULTRASONIC_TOLERANCE =
            namespace.addConstantDouble("ultrasonic tolerance", 3);

    public Climb() {
        Drivetrain drivetrain = Drivetrain.getInstance();
        Climber climber = Climber.getInstance();
        addCommands(
                backAwayFromHab(drivetrain),
                openFrontSolenoid(climber),
                driveForward(drivetrain, OPEN_BACK_SOLENOID_ULTRASONIC_DISTANCE),
                openBackSolenoid(climber),
                closeFrontSolenoid(climber),
                driveForward(drivetrain, CLOSE_BACK_SOLENOID_ULTRASONIC_DISTANCE),
                closeBackSolenoid(climber)
        );
    }

    private ParallelRaceGroup backAwayFromHab(Drivetrain drivetrain) {
        return new DriveArcade(drivetrain, -DRIVE_SPEED, 0).withInterrupt(
                () -> Math.abs(OPEN_FRONT_SOLENOID_ULTRASONIC_DISTANCE.get() - drivetrain.getUltrasonicDistanceInCM())
                        <= ULTRASONIC_TOLERANCE.get()
        );
    }

    private InstantCommand openFrontSolenoid(Climber climber) {
        return climber.frontSolenoidsOn();
    }

    private ParallelRaceGroup driveForward(Drivetrain drivetrain, Supplier<Double> distanceFromHab) {
        return new DriveArcade(drivetrain, DRIVE_SPEED, 0).withInterrupt(
                () -> Math.abs(distanceFromHab.get() - drivetrain.getUltrasonicDistanceInCM()) <=
                        ULTRASONIC_TOLERANCE.get()
        );
    }

    private InstantCommand openBackSolenoid(Climber climber) {
        return climber.backSolenoidOn();
    }

    private InstantCommand closeFrontSolenoid(Climber climber) {
        return climber.frontSolenoidsOff();
    }

    private InstantCommand closeBackSolenoid(Climber climber) {
        return climber.backSolenoidOff();
    }
}
