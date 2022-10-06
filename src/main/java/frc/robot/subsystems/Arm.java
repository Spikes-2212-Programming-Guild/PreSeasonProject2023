package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.RobotMap;

import java.util.List;

public class Arm extends MotoredGenericSubsystem {

    private static Arm upperInstance;
    private static Arm lowerInstance;
    private RelativeEncoder encoder;

    private Arm(String namespaceName, CANSparkMax... motorControllers) {
        super(namespaceName, motorControllers);
        this.encoder = List.of(motorControllers).get(0).getEncoder();
    }

    public static Arm getUpperInstance() {
        if (upperInstance == null) {
            upperInstance = new Arm("upper arm", new CANSparkMax(RobotMap.CAN.ARM_UPPER_SPARKMAX, CANSparkMaxLowLevel.MotorType.kBrushless));
        }
        return upperInstance;
    }

    public static Arm getLowerInstance() {
        if (lowerInstance == null) {
            lowerInstance = new Arm("lower arm", new CANSparkMax(RobotMap.CAN.ARM_LOWER_SPARKMAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.ARM_LOWER_SPARKMAX_2, CANSparkMaxLowLevel.MotorType.kBrushless));
        }
        return lowerInstance;
    }
}
