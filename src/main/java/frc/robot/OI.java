package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public class OI /*GEVALD*/ {

    private Joystick left = new Joystick(0);
    private Joystick right = new Joystick(1);
    public OI() {

    }

    public double getX() {
        return left.getX();
    }

    public double getY() {
        return -right.getY();
    }
}
