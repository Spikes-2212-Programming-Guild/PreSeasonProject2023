package frc.robot;

import com.spikes2212.util.XboxControllerWrapper;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.subsystems.Drivetrain;

public class OI /*GEVALD*/ {
    private final Joystick right = new Joystick(1);
    private final Joystick left = new Joystick(0);

    public double getRightY() {
        return -right.getY();
    }

    public double getLeftX() {
        return left.getX();
    }
}

