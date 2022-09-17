package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI /*GEVALD*/ {

    Joystick left = new Joystick(0);
    Joystick right = new Joystick(1);

    public OI() {
    }

    public double getLeftX() {
        return left.getX();
    }

    public double getRightY() {
        return -right.getY();
    }
}
