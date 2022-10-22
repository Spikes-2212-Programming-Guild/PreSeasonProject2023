package frc.robot;

import com.spikes2212.util.XboxControllerWrapper;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.*;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gripper;
import frc.robot.subsystems.Vision;

public class OI /*GEVALD*/ {

    private static OI instance;

    private final Joystick left = new Joystick(0);
    private final Joystick right = new Joystick(1);

    private final XboxControllerWrapper xbox = new XboxControllerWrapper(2);

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI(Drivetrain.getInstance(), Gripper.getInstance(),
                    Climber.getInstance(), Vision.getInstance());
        }
        return instance;
    }

    private OI(Drivetrain drivetrain, Gripper gripper, Climber climber, Vision vision) {
        xbox.getButtonStart().whenPressed(new DriveToTable(drivetrain, vision));
        xbox.getButtonBack().whenPressed(new Climb(drivetrain, climber));
        xbox.getRBButton().whenPressed(new OpenGripper(gripper));
        xbox.getLBButton().whenPressed(new CloseGripper(gripper));
        xbox.getRightStickButton().whenPressed(new CenterOnCube(drivetrain, vision));
        xbox.getLeftStickButton().whenPressed(new CenterOnTable(drivetrain, vision));
        xbox.getRTButton().whenActive(new PickUpCube(drivetrain, gripper, vision));
    }

    public double getLeftX() {
        return left.getX();
    }

    public double getRightY() {
        return -right.getY();
    }
}
