package frc.robot

// import edu.wpi.first.wpilibj2.command.button.POVButton; // dpad
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.Constants.Controllers
import frc.robot.commands.Autos
import frc.robot.subsystems.DriveSubsystem

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the [Robot]
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
class RobotContainer {
    // The robot's subsystems and commands are defined here...
    private val driveSubsystem = DriveSubsystem()

    private val driverController =
            CommandXboxController(Constants.Controllers.kDriverControllerPort)
    private val lStickBtn = driverController.leftStick()
    private val startBtn = driverController.start()

    private final val autoCommandNames = listOf("PastLine", "Balance", "BalancePastLine")
    private final val autoCommands =
            listOf(
                    Autos.pastLine(driveSubsystem),
                    Autos.balance(driveSubsystem),
                    Autos.balancePastLine(driveSubsystem)
            )
    private val chooser = SendableChooser<String>()

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    init {
        // Configure the trigger bindings
        configureBindings()
        // setup the dashboard with autonomous options
        setupDashboard()
    }

    /**
     * Use this method to define your trigger->command mappings. Triggers can be created via the
     * [Trigger#Trigger(java.util.function.BooleanSupplier)] constructor with an arbitrary
     * predicate, or via the named factories in
     * [edu.wpi.first.wpilibj2.command.button.CommandGenericHID]'s subclasses for
     * [CommandXboxController]/[edu.wpi.first.wpilibj2.command.button.CommandPS4Controller]
     * controllers or [edu.wpi.first.wpilibj2.command.button.CommandJoystick].
     */
    private fun configureBindings() {
        // driveSubsystem.setDefaultCommand(
        //         driveSubsystem.drive(driverController.getLeftY(), driverController.getRightX())
        // )
        driveSubsystem.setDefaultCommand(Commands.run({ driveSubsystem.drive(driverController.getLeftY(), driverController.getRightX()) }, driveSubsystem))

        // Schedule ExampleCommand when exampleCondition changes to true
        // Trigger { exampleSubsystem.exampleCondition() }.onTrue(ExampleCommand(exampleSubsystem))

        // Schedule exampleMethodCommand when the Xbox controller's B button is pressed,
        // cancelling on release.
        // driverController.b().whileTrue(exampleSubsystem.exampleMethodCommand())
        lStickBtn.onTrue(driveSubsystem.toggleSlowMode())
        startBtn.whileTrue(driveSubsystem.balance())
    }

    private fun setupDashboard() {
        chooser.setDefaultOption(autoCommandNames.first(), autoCommandNames.first())
        autoCommandNames.forEach { chooser.addOption(it, it) }
        SmartDashboard.putData("Auto Commands", chooser)
    }

    /**
     * Use this to pass the autonomous command to the main [Robot] class.
     *
     * @return the command to run in autonomous
     */
    val autonomousCommand: Command
        get() {
            val selected = chooser.getSelected()
            autoCommandNames.forEachIndexed { i, e -> if (e == selected) return autoCommands[i] }
            // default: PastLine
            return autoCommands.first()
        }
}
