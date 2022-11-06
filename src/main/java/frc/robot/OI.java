package frc.robot;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.smartmotorcontrollersubsystem.MoveSmartMotorControllerSubsystem;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.util.UnifiedControlMode;
import com.spikes2212.util.XboxControllerWrapper;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.Arm.State;

import static com.spikes2212.control.FeedForwardSettings.EMPTY_FFSETTINGS;
import static com.spikes2212.control.PIDSettings.EMPTY_PID_SETTINGS;

public class OI /*GEVALD*/ {

    private static OI instance;

    private final Joystick left = new Joystick(0);
    private final Joystick right = new Joystick(1);

    private final XboxControllerWrapper xbox = new XboxControllerWrapper(2);

    public static OI getInstance() {
        if (instance == null) {
//            instance = new OI(Drivetrain.getInstance(), Gripper.getInstance(),
//                    Climber.getInstance(), Vision.getInstance(), Arm.getLowerInstance(), Arm.getUpperInstance());
//            instance = new OI(Climber.getInstance());
            instance = new OI(Gripper.getInstance(), Arm.getLowerInstance(), Arm.getUpperInstance(), Climber.getInstance());
        }
        return instance;
    }

    private OI(Drivetrain drivetrain, Gripper gripper, Climber climber, Vision vision, Arm lowerShaft, Arm upperShaft) {
        xbox.getButtonStart().whenPressed(new DriveToTable(drivetrain, vision));
        xbox.getButtonBack().whenPressed(new Climb(drivetrain, climber));

        xbox.getLBButton().whenPressed(new CloseGripper(gripper));
        xbox.getRBButton().whenPressed(new OpenGripper(gripper));

        xbox.getRightStickButton().whenPressed(new CenterOnCube(drivetrain, vision));
        xbox.getLeftStickButton().whenPressed(new CenterOnTable(drivetrain, vision));

        xbox.getLTButton().whenActive(new MoveArm(lowerShaft, upperShaft, State.RESTING));
        xbox.getRTButton().whenActive(new PickUpCube(drivetrain, gripper, vision));

        xbox.getGreenButton().whenPressed(new MoveArm(lowerShaft, upperShaft, State.PICKING));
        xbox.getBlueButton().whenPressed(new MoveArm(lowerShaft, upperShaft, State.PLACING_ZERO));
        xbox.getYellowButton().whenPressed(new MoveArm(lowerShaft, upperShaft, State.PLACING_ONE));
        xbox.getRedButton().whenPressed(new MoveArm(lowerShaft, upperShaft, State.PLACING_TWO));

        xbox.getLeftButton().whileHeld(new MoveSmartMotorControllerSubsystem(lowerShaft, EMPTY_PID_SETTINGS,
                EMPTY_FFSETTINGS, UnifiedControlMode.PERCENT_OUTPUT, () -> -Arm.LOWER_SHAFT_MOVE_SPEED));
        xbox.getRightButton().whileHeld(new MoveSmartMotorControllerSubsystem(lowerShaft, EMPTY_PID_SETTINGS,
                EMPTY_FFSETTINGS, UnifiedControlMode.PERCENT_OUTPUT, () -> Arm.LOWER_SHAFT_MOVE_SPEED));

        xbox.getDownButton().whileHeld(new MoveSmartMotorControllerSubsystem(upperShaft, EMPTY_PID_SETTINGS,
                EMPTY_FFSETTINGS, UnifiedControlMode.PERCENT_OUTPUT, () -> -Arm.UPPER_SHAFT_MOVE_SPEED));
        xbox.getUpButton().whileHeld(new MoveSmartMotorControllerSubsystem(upperShaft, EMPTY_PID_SETTINGS,
                EMPTY_FFSETTINGS, UnifiedControlMode.PERCENT_OUTPUT, () -> Arm.UPPER_SHAFT_MOVE_SPEED));
    }

    private OI(Gripper gripper, Arm lowerShaft, Arm upperShaft, Climber climber) {
        xbox.getLBButton().whenPressed(gripper.openSolenoid());
        xbox.getRBButton().whenPressed(gripper.closeSolenoid());

//        xbox.getLeftButton().whileHeld(new MoveGenericSubsystem(lowerShaft, -Arm.LOWER_SHAFT_MOVE_SPEED));
//        xbox.getRightButton().whileHeld(new MoveGenericSubsystem(lowerShaft, Arm.LOWER_SHAFT_MOVE_SPEED));
//        xbox.getDownButton().whileHeld(new MoveGenericSubsystem(upperShaft, Arm.UPPER_SHAFT_MOVE_SPEED));
//        xbox.getUpButton().whileHeld(new MoveGenericSubsystem(upperShaft, -Arm.UPPER_SHAFT_MOVE_SPEED));

        xbox.getLeftButton().whileHeld(
                new MoveSmartMotorControllerSubsystem(lowerShaft, EMPTY_PID_SETTINGS, EMPTY_FFSETTINGS,
                        UnifiedControlMode.PERCENT_OUTPUT, () -> -Arm.LOWER_SHAFT_MOVE_SPEED) {
                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                }
        );

        xbox.getRightButton().whileHeld(
                new MoveSmartMotorControllerSubsystem(lowerShaft, EMPTY_PID_SETTINGS, EMPTY_FFSETTINGS,
                        UnifiedControlMode.PERCENT_OUTPUT, () -> Arm.LOWER_SHAFT_MOVE_SPEED) {
                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                }
        );

        xbox.getDownButton().whileHeld(
                new MoveSmartMotorControllerSubsystem(upperShaft, EMPTY_PID_SETTINGS, EMPTY_FFSETTINGS,
                        UnifiedControlMode.PERCENT_OUTPUT, () -> -Arm.UPPER_SHAFT_MOVE_SPEED) {
                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                }
        );

        xbox.getUpButton().whileHeld(
                new MoveSmartMotorControllerSubsystem(upperShaft, EMPTY_PID_SETTINGS, EMPTY_FFSETTINGS,
                        UnifiedControlMode.PERCENT_OUTPUT, () -> Arm.UPPER_SHAFT_MOVE_SPEED) {
                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                }
        );

        xbox.getGreenButton().whenPressed(climber.openBackSolenoid());
        xbox.getRedButton().whenPressed(climber.closeBackSolenoid());
        xbox.getBlueButton().whenPressed(climber.openFrontSolenoid());
        xbox.getYellowButton().whenPressed(climber.closeFrontSolenoid());
    }

    public double getLeftX() {
        return left.getX();
    }

    public double getRightY() {
        return -right.getY();
    }
}