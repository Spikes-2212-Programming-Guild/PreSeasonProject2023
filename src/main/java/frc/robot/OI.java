package frc.robot;

import com.spikes2212.command.genericsubsystem.commands.smartmotorcontrollersubsystem.MoveSmartMotorControllerSubsystem;
import com.spikes2212.util.PlaystationControllerWrapper;
import com.spikes2212.util.UnifiedControlMode;
import com.spikes2212.util.XboxControllerWrapper;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

import static com.spikes2212.control.FeedForwardSettings.EMPTY_FFSETTINGS;
import static com.spikes2212.control.PIDSettings.EMPTY_PID_SETTINGS;

public class OI /*GEVALD*/ {

    private static OI instance;

    private final PlaystationControllerWrapper ps = new PlaystationControllerWrapper(0);
    private final XboxControllerWrapper xbox = new XboxControllerWrapper(1);

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI(Drivetrain.getInstance(), Gripper.getInstance(),
                    Climber.getInstance(), Vision.getInstance(), Arm.getLowerInstance(), Arm.getUpperInstance());
        }
        return instance;
    }

    private OI(Drivetrain drivetrain, Gripper gripper, Climber climber, Vision vision, Arm lowerShaft, Arm upperShaft) {
        xbox.getButtonStart().whenPressed(new DriveToTable(drivetrain, vision));
        xbox.getButtonBack().whenPressed(new Climb(drivetrain, climber));

        xbox.getRightStickButton().whenPressed(new CenterOnCube(drivetrain, vision));

        xbox.getLBButton().whenPressed(gripper.openSolenoid());
        xbox.getRBButton().whenPressed(gripper.closeSolenoid());

        xbox.getLTButton().whenActive(new InstantCommand(() -> CommandScheduler.getInstance().cancelAll()));

        xbox.getLeftButton().whenPressed(climber.openFrontSolenoid());
        xbox.getRightButton().whenPressed(climber.closeBackSolenoid());
        xbox.getDownButton().whenPressed(climber.openBackSolenoid());
        xbox.getUpButton().whenPressed(climber.closeFrontSolenoid());

        xbox.getRedButton().whileHeld(
                new MoveSmartMotorControllerSubsystem(lowerShaft, EMPTY_PID_SETTINGS, EMPTY_FFSETTINGS,
                        UnifiedControlMode.PERCENT_OUTPUT, Arm.LOWER_SHAFT_MOVE_FORWARD_SPEED) {
                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                }
        );

        xbox.getGreenButton().whileHeld(
                new MoveSmartMotorControllerSubsystem(upperShaft, EMPTY_PID_SETTINGS, EMPTY_FFSETTINGS,
                        UnifiedControlMode.PERCENT_OUTPUT, Arm.UPPER_SHAFT_MOVE_DOWN_SPEED) {
                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                }
        );

        xbox.getYellowButton().whileHeld(
                new MoveSmartMotorControllerSubsystem(upperShaft, EMPTY_PID_SETTINGS, EMPTY_FFSETTINGS,
                        UnifiedControlMode.PERCENT_OUTPUT, Arm.UPPER_SHAFT_MOVE_UP_SPEED) {
                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                }
        );

        xbox.getBlueButton().whileHeld(
                new MoveSmartMotorControllerSubsystem(lowerShaft, EMPTY_PID_SETTINGS, EMPTY_FFSETTINGS,
                        UnifiedControlMode.PERCENT_OUTPUT, Arm.LOWER_SHAFT_MOVE_BACKWARD_SPEED) {
                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                }
        );

    }

    public double getLeftX() {
        double val = ps.getRightX();
        return val * val * Math.signum(val);
    }

    public double getRightY() {
        double val = -ps.getLeftY();
        return val * val * Math.signum(val);
    }
}