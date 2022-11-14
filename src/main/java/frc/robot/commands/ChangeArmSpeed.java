package frc.robot.commands;

import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.function.Supplier;

public class ChangeArmSpeed extends InstantCommand {

    private static final RootNamespace armSpeeds = new RootNamespace("arm speeds");

    public static final Supplier<Double> LOWER_SHAFT_MOVE_FORWARD_SPEED =
            armSpeeds.addConstantDouble("lower shaft move forward speed", 0.1);
    public static final Supplier<Double> LOWER_SHAFT_MOVE_BACKWARD_SPEED =
            armSpeeds.addConstantDouble("lower shaft move backward speed", -0.2);

    public static boolean toTriple = true;

    public ChangeArmSpeed() {
    }

    @Override
    public void initialize() {
        if (toTriple) {
            Supplier<Double> lowerShaftBackward = armSpeeds.addConstantDouble("lower shaft move backward speed",
                    LOWER_SHAFT_MOVE_BACKWARD_SPEED.get() * 2);
            Supplier<Double> lowerShaftForward = armSpeeds.addConstantDouble("lower shaft move forward speed",
                    LOWER_SHAFT_MOVE_FORWARD_SPEED.get() * 2);

        } else {
            Supplier<Double> lowerShaftBackward = armSpeeds.addConstantDouble("lower shaft move backward speed",
                    LOWER_SHAFT_MOVE_BACKWARD_SPEED.get() / 2);
            Supplier<Double> lowerShaftForward = armSpeeds.addConstantDouble("lower shaft move forward speed",
                    LOWER_SHAFT_MOVE_FORWARD_SPEED.get() / 2);
        }

        toTriple = !toTriple;
        super.initialize();
    }
}
