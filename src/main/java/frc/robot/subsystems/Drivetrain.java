package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.RobotMap;

public class Drivetrain extends TankDrivetrain {

    private ADXRS450_Gyro gyro;

    public Drivetrain() {
        super(new MotorControllerGroup(new CANSparkMax(RobotMap.CAN.PORT_LEFT1, CANSparkMaxLowLevel.MotorType.kBrushless),
                        new CANSparkMax(RobotMap.CAN.PORT_LEFT2, CANSparkMaxLowLevel.MotorType.kBrushless)),
                new MotorControllerGroup(new CANSparkMax(RobotMap.CAN.PORT_LEFT1, CANSparkMaxLowLevel.MotorType.kBrushless),
                        new CANSparkMax(RobotMap.CAN.PORT_LEFT2, CANSparkMaxLowLevel.MotorType.kBrushless)));
        gyro = new ADXRS450_Gyro();
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putNumber("TankDrive", gyro::getAngle);
    }
}
