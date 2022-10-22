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

    public interface PCM {

        int CLIMBER_FRONT_SOLENOID_1_FORWARD = -1;
        int CLIMBER_FRONT_SOLENOID_1_REVERSE = -1;
        int CLIMBER_FRONT_SOLENOID_2_FORWARD = -1;
        int CLIMBER_FRONT_SOLENOID_2_REVERSE = -1;
        int CLIMBER_BACK_SOLENOID_FORWARD = -1;
        int CLIMBER_BACK_SOLENOID_REVERSE = -1;

        int GRIPPER_SOLENOID_FORWARD = -1;
        int GRIPPER_SOLENOID_REVERSE = -1;
    }

    public interface DIO {

    }

    public interface PWM {

    }

    public interface AIN {

    }
}
