package frc.robot;

public class RobotMap {

    public interface CAN {

        int DRIVETRAIN_LEFT_SPARKMAX_1 = 8;
        int DRIVETRAIN_LEFT_SPARKMAX_2 = 4;
        int DRIVETRAIN_RIGHT_SPARKMAX_1 = 1;
        int DRIVETRAIN_RIGHT_SPARKMAX_2 = 2;
        int PIGEON_TALON = 5;

        int ARM_LOWER_SPARKMAX_1 = 6;
        int ARM_LOWER_SPARKMAX_2 = 7;
        int ARM_UPPER_SPARKMAX = 3;
    }

    public interface PCM {

        int CLIMBER_FRONT_SOLENOID_FORWARD = 2;
        int CLIMBER_FRONT_SOLENOID_REVERSE = 3;
        int CLIMBER_BACK_SOLENOID_FORWARD = 1;
        int CLIMBER_BACK_SOLENOID_REVERSE = 0;

        int GRIPPER_SOLENOID_FORWARD = 4;
        int GRIPPER_SOLENOID_REVERSE = 5;
    }

    public interface DIO {

        int GRIPPER_LIMIT = -1;
    }
}
