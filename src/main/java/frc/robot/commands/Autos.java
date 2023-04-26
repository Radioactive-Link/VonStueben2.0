// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;

import frc.robot.subsystems.DriveSubsystem;

public final class Autos {

  public static CommandBase pastLine(DriveSubsystem drive) {
    return drive.autoDrive(0.7,0.0).withTimeout(2);
    // return Commands.run(() -> drive.autoDrive(0.7,0.0)).withTimeout(2);
  }

  public static CommandBase balance(DriveSubsystem drive) {
    return Commands.sequence(
      drive.autoDrive(0.74,0.0).withTimeout(2)
      , drive.balance() );
    // return Commands.sequence(
    // Commands.run(() -> drive.autoDrive(0.74,0.0)).withTimeout(2)
    // , Commands.run(() -> drive.balance()) );
  }

  public static CommandBase balancePastLine(DriveSubsystem drive) {
    return Commands.sequence(
      drive.autoDrive(0.8,0.0).withTimeout(3)
      , drive.autoDrive(0.0,0.0).withTimeout(1)
      , drive.autoDrive(-0.74,0.0).withTimeout(2)
      , drive.balance() );
    // return Commands.sequence(
    //   Commands.run(() -> drive.autoDrive(0.8,0.0)).withTimeout(3)
    //   , Commands.run(() -> drive.autoDrive(0.0,0.0)).withTimeout(1)
    //   , Commands.run(() -> drive.autoDrive(-0.74,0.0)).withTimeout(2)
    //   , Commands.run(() -> drive.balance()) );
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
