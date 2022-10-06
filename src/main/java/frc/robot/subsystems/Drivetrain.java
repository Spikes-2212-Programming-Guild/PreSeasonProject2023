package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.util.PigeonWrapper;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

public class Drivetrain extends TankDrivetrain {

    public static final double MILLIMETER_TO_CENTIMETER = 10.0;
    private static Drivetrain instance;

    private final CANSparkMax left1;
    private final CANSparkMax left2;
    private final CANSparkMax right1;
    private final CANSparkMax right2;

    private final Ultrasonic ultrasonic;
    private final PigeonWrapper pigeon;

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain("drivetrain",
                    new CANSparkMax(RobotMap.CAN.DRIVETRAIN_LEFT_SPARKMAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.DRIVETRAIN_LEFT_SPARKMAX_2, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.DRIVETRAIN_RIGHT_SPARKMAX_1, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new CANSparkMax(RobotMap.CAN.DRIVETRAIN_RIGHT_SPARKMAX_2, CANSparkMaxLowLevel.MotorType.kBrushless),
                    new PigeonWrapper(RobotMap.CAN.PIGEON_TALON),
                    new Ultrasonic(RobotMap.CAN.ULTRASONIC_CHANNEL_1, RobotMap.CAN.ULTRASONIC_CHANNEL_2)
            );
        }
        return instance;
    }

    private Drivetrain(String namespaceName, CANSparkMax left1, CANSparkMax left2,
                       CANSparkMax right1, CANSparkMax right2, PigeonWrapper pigeon, Ultrasonic ultrasonic) {
        super(namespaceName, new MotorControllerGroup(left1, left2), new MotorControllerGroup(right1, right2));
        this.left1 = left1;
        this.left2 = left2;
        this.right1 = right1;
        this.right2 = right2;
        this.pigeon = pigeon;
        this.ultrasonic = ultrasonic;
        Ultrasonic.setAutomaticMode(true);
        configureDashboard();
    }

    public double getUltrasonicInCM(){
        return ultrasonic.getRangeMM()/MILLIMETER_TO_CENTIMETER;
    }

    public double getYaw() {
        double yaw = pigeon.getYaw() % 360;
        if (yaw > 180) yaw -= 360;
        if (yaw < -180) yaw += 360;
        return yaw;
    }

    public void resetPigeon() {
        pigeon.reset();
    }

    @Override
    public void configureDashboard() {
        namespace.putData("reset pigeon", new InstantCommand(this::resetPigeon) {
            @Override
            public boolean runsWhenDisabled() {
                return true;
            }
        });
        namespace.putNumber("pigeon yaw", this::getYaw);
        namespace.putNumber("left neo 1 encoder value", left1.getEncoder()::getPosition);
        namespace.putNumber("left neo 2 encoder value", left2.getEncoder()::getPosition);
        namespace.putNumber("right neo 1 encoder value", right1.getEncoder()::getPosition);
        namespace.putNumber("right neo 2 encoder value", right2.getEncoder()::getPosition);
        namespace.putNumber("ultrasonic distance value",this::getUltrasonicInCM);
    }
}
