package frc.robot;

public class RobotMap {

    public interface CAN {

        int DRIVETRAIN_LEFT_SPARKMAX_1 = -1;
        int DRIVETRAIN_LEFT_SPARKMAX_2 = -1;
        int DRIVETRAIN_RIGHT_SPARKMAX_1 = -1;
        int DRIVETRAIN_RIGHT_SPARKMAX_2 = -1;

        int PIGEON_TALON = -1;
        
        int ULTRASONIC_CHANNEL_1 = -1;
        int ULTRASONIC_CHANNEL_2 = -1;
    }

    public interface DIO {

    }

    public interface PWM {

    }

    public interface AIN {

    }

    public interface PCM {

        int CLAW_FORWARD_SOLENOID = -1;
        int CLAW_REVERSE_SOLENOID = -1;
    }
}
