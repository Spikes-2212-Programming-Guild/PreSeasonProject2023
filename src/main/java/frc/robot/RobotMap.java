package frc.robot;

public class RobotMap {

    public interface CAN {

        int DRIVETRAIN_LEFT_SPARKMAX_1 = 2;
        int DRIVETRAIN_LEFT_SPARKMAX_2 = 11;
        int DRIVETRAIN_RIGHT_SPARKMAX_1 = 4;
        int DRIVETRAIN_RIGHT_SPARKMAX_2 = 3;
        int PIGEON_TALON = 5;

        int ARM_LOWER_SPARKMAX_1 = -1;
        int ARM_LOWER_SPARKMAX_2 = -1;
        int ARM_UPPER_SPARKMAX = -1;
    }

    public interface PCM {

        int CLIMBER_FRONT_SOLENOID_FORWARD = 3;
        int CLIMBER_FRONT_SOLENOID_REVERSE = 2;
        int CLIMBER_BACK_SOLENOID_FORWARD = 1;
        int CLIMBER_BACK_SOLENOID_REVERSE = 0;

        int GRIPPER_SOLENOID_FORWARD = -1;
        int GRIPPER_SOLENOID_REVERSE = -1;
    }

    public interface DIO {

        int GRIPPER_LIMIT = -1;
    }
}
