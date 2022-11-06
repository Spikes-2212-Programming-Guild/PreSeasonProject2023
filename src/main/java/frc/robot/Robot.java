// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.Climb;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gripper;

public class Robot extends TimedRobot {

    private final RootNamespace namespace = new RootNamespace("robot");
    private Drivetrain drivetrain;
    private Climber climber;
    private Gripper gripper;
    private Arm lower;
    private Arm upper;
    private OI oi;

    @Override
    public void robotInit() {
        drivetrain = Drivetrain.getInstance();
        climber = Climber.getInstance();
        gripper = Gripper.getInstance();
//        lower = Arm.getLowerInstance();
//        upper = Arm.getUpperInstance();
        oi = OI.getInstance();
        Compressor compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
        namespace.putData("compressor on", new InstantCommand(compressor::enableDigital));
        namespace.putData("compressor off", new InstantCommand(compressor::disable));
    }

    @Override
    public void robotPeriodic() {
        Climb.periodic();
        drivetrain.periodic();
        climber.periodic();
        gripper.periodic();
//        lower.periodic();
//        upper.periodic();
        namespace.update();
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        DriveArcade driveArcade = new DriveArcade(drivetrain, oi::getRightY, oi::getLeftX);
        drivetrain.setDefaultCommand(driveArcade);
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void simulationInit() {
    }

    @Override
    public void simulationPeriodic() {
    }
}
