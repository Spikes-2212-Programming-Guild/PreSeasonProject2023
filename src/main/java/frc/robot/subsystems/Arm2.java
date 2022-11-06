package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.RobotMap;

public class Arm2 extends MotoredGenericSubsystem {

    private static Arm2 lowerInstance, upperInstance;

    public static Arm2 getLowerInstance() {
        if (lowerInstance == null) {
            lowerInstance = new Arm2("arm2 lower",
                    new CANSparkMax(RobotMap.CAN.ARM_LOWER_SPARKMAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.ARM_LOWER_SPARKMAX_2, CANSparkMaxLowLevel.MotorType.kBrushless));
        }
        return lowerInstance;
    }

    public static Arm2 getUpperInstance() {
        if (upperInstance == null) {
            upperInstance = new Arm2("arm2 upper",
                    new CANSparkMax(RobotMap.CAN.ARM_UPPER_SPARKMAX, CANSparkMaxLowLevel.MotorType.kBrushless));
        }
        return upperInstance;
    }

    public Arm2(String namespaceName, MotorController... motorControllers) {
        super(namespaceName, motorControllers);
        for (MotorController motorController : motorControllers) {
            ((CANSparkMax) motorController).setIdleMode(CANSparkMax.IdleMode.kBrake);
        }
    }
}
