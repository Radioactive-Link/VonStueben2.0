// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.Constants.Controllers;
import frc.robot.commands.Autos;
import frc.robot.subsystems.DriveSubsystem;

import java.util.Objects;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem drive = new DriveSubsystem();

  // define the controller
  private final CommandXboxController driverController =
      new CommandXboxController(Controllers.DRIVER_XBOX_CONTROLLER);

  // declaring a trigger to make code in configureBindings shorter
  private final Trigger lStick = driverController.leftStick();
  private final Trigger startBtn = driverController.start();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // set default commands for subsystems
    // default command for drivesubsystem is to drive using controller axes
    drive.setDefaultCommand(Commands.run(() -> drive.drive( driverController.getLeftY()
                                                          , driverController.getRightX() )));

    setupDashboard();

    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    lStick.onTrue(drive.toggleSlowMode());
    startBtn.whileTrue(drive.balance());
  }

  // stores names of autonomous commands. Displayed to dashboard in setUpDashboard
  // and used for comparison in getAutonomousCommand
  private final String[] autoCommandNames = {"PastLine", "Balance", "BalancePastLine"};
  // stores corresponding autoCommands. Must line up with above array to work as expected.
  private final CommandBase[] autoCommands = { Autos.pastLine(drive)
                                             , Autos.balance(drive)
                                             , Autos.balancePastLine(drive)};
  private final SendableChooser<String> chooser = new SendableChooser<>();

  public void setupDashboard() {
    //set default option
    chooser.setDefaultOption(autoCommandNames[0], autoCommandNames[0]);

    //add other commands to dropdown of autocommands
    for (var e : autoCommandNames) chooser.addOption(e, e);

    //put to dashboard
    SmartDashboard.putData("Auto Commands", chooser);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    var autoSelected = chooser.getSelected();

    // match up selected with names, if a match is found,
    // return the corresponding function
    for (int i = 0; i < autoCommandNames.length; ++i) 
      if (Objects.equals(autoSelected, autoCommandNames[i]))
        return autoCommands[i];

    // default: "PastLine"
    return Autos.pastLine(drive);
  }
}
