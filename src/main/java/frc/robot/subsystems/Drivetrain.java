package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.RobotMap;

public class Drivetrain extends TankDrivetrain {

    private static Drivetrain instance;

    private final CANSparkMax left1;
    private final CANSparkMax left2;
    private final CANSparkMax right1;
    private final CANSparkMax right2;

    private final ADXRS450_Gyro gyro;

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain("drivetrain",
                    new CANSparkMax(RobotMap.CAN.DRIVETRAIN_LEFT_SPARKMAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.DRIVETRAIN_LEFT_SPARKMAX_2, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.DRIVETRAIN_RIGHT_SPARKMAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.DRIVETRAIN_RIGHT_SPARKMAX_2, CANSparkMaxLowLevel.MotorType.kBrushless)
            );

        }
        return instance;
    }

    private Drivetrain(String namespaceName, CANSparkMax left1, CANSparkMax left2,
                       CANSparkMax right1, CANSparkMax right2) {
        super(namespaceName, new MotorControllerGroup(left1, left2), new MotorControllerGroup(right1, right2));
        this.left1 = left1;
        this.left2 = left2;
        this.right1 = right1;
        this.right2 = right2;
        this.gyro = new ADXRS450_Gyro();
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putNumber("gyro value", gyro::getAngle);
        rootNamespace.putNumber("left neo 1 encoder value", left1.getEncoder()::getPosition);
        rootNamespace.putNumber("left neo 2 encoder value", left2.getEncoder()::getPosition);
        rootNamespace.putNumber("right neo 1 encoder value", right1.getEncoder()::getPosition);
        rootNamespace.putNumber("right neo 2 encoder value", right2.getEncoder()::getPosition);
    }
}
