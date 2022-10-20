package frc.robot;

import com.spikes2212.util.XboxControllerWrapper;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.command.DriveToTable;
import frc.robot.subsystems.Drivetrain;

public class OI /*GEVALD*/ {

    private final Joystick left = new Joystick(0);
    private final Joystick right = new Joystick(1);
    
    private final XboxControllerWrapper xbox = new XboxControllerWrapper(2);

    public OI() {
        Drivetrain drivetrain = Drivetrain.getInstance();

        xbox.getButtonStart().whenPressed(new DriveToTable(drivetrain));

    }

    public double getLeftX() {
        return left.getX();
    }

    public double getRightY() {
        return -right.getY();
    }
}
