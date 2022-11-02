package frc.robot;

public class RobotMap {

    public interface CAN {

        int DRIVETRAIN_LEFT_SPARKMAX_1 = 0;
        int DRIVETRAIN_LEFT_SPARKMAX_2 = 1;
        int DRIVETRAIN_RIGHT_SPARKMAX_1 = 2;
        int DRIVETRAIN_RIGHT_SPARKMAX_2 = 3;
        int PIGEON_TALON = 5;

        int ARM_LOWER_SPARKMAX_1 = -1;
        int ARM_LOWER_SPARKMAX_2 = -1;
        int ARM_UPPER_SPARKMAX = -1;
    }

    public interface PCM {

        int CLIMBER_FRONT_SOLENOID_FORWARD = 0;
        int CLIMBER_FRONT_SOLENOID_REVERSE = 1;
        int CLIMBER_BACK_SOLENOID_FORWARD = 2;
        int CLIMBER_BACK_SOLENOID_REVERSE = 3;

        int GRIPPER_SOLENOID_FORWARD = -1;
        int GRIPPER_SOLENOID_REVERSE = -1;
    }

    public interface DIO {

        int GRIPPER_LIMIT = -1;
    }

    public interface PWM {

    }

    public interface AIN {

        int ULTRASONIC_CHANNEL_1 = -1;
        int ULTRASONIC_CHANNEL_2 = -1;
    }
}
