package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import frc.robot.RobotMap;

import java.util.List;

/**
 * Controls the double-jointed arm that is responsible for placing the cubes.
 */
public class Arm extends MotoredGenericSubsystem {

    enum State {
        RESTING, PICKING, PLACING_ZERO, PLACING_ONE, PLACING_TWO;
    }

    private static Arm lowerInstance;
    private static Arm upperInstance;

    private final RelativeEncoder encoder;

    public static Arm getLowerInstance() {
        if (lowerInstance == null) {
            lowerInstance = new Arm("lower shaft",
                    new CANSparkMax(RobotMap.CAN.ARM_LOWER_SPARKMAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.ARM_LOWER_SPARKMAX_2, CANSparkMaxLowLevel.MotorType.kBrushless)
            );
        }
        return lowerInstance;
    }

    public static Arm getUpperInstance() {
        if (upperInstance == null) {
            upperInstance = new Arm("upper shaft", new CANSparkMax(RobotMap.CAN.ARM_UPPER_SPARKMAX,
                    CANSparkMaxLowLevel.MotorType.kBrushless));
        }
        return upperInstance;
    }

    private Arm(String namespaceName, CANSparkMax... motors) {
        super(namespaceName, motors);
        this.encoder = List.of(motors).get(0).getEncoder();
        configureDashboard();
    }

    public double getEncoderPosition() {
        return encoder.getPosition();
    }

    @Override
    public void configureDashboard() {
        namespace.putNumber("encoder position", this::getEncoderPosition);
    }
}
