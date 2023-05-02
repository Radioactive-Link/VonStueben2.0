package frc.robot.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.subsystems.DriveSubsystem
// import frc.robot.subsystems.ExampleSubsystem

class Autos private constructor() {
    init {
        throw UnsupportedOperationException("This is a utility class!")
    }

    //declaring an object as companion is like "static" in java
    companion object {
        /** Example static factory for an autonomous command.  */
        // fun exampleAuto(subsystem: ExampleSubsystem): CommandBase {
            // return Commands.sequence(subsystem.exampleMethodCommand(), ExampleCommand(subsystem))
        // }

        fun pastLine(drive: DriveSubsystem): CommandBase {
            return drive.autoDrive(0.7,0.0).withTimeout(2.0)
        }

        fun balance(drive: DriveSubsystem): CommandBase {
            return Commands.sequence( drive.autoDrive(0.74,0.0).withTimeout(2.0)
                                    , drive.balance() );
        }

        fun balancePastLine(drive: DriveSubsystem): CommandBase {
            return Commands.sequence( drive.autoDrive(0.8,0.0).withTimeout(3.0)
                                    , drive.autoDrive(0.0,0.0).withTimeout(1.0)
                                    , drive.autoDrive(-0.74,0.0).withTimeout(2.0)
                                    , drive.balance() );
        }
    }
}
