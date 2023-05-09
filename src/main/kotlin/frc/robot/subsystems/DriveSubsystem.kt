package frc.robot.subsystems

import com.kauailabs.navx.frc.AHRS
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants

class DriveSubsystem : SubsystemBase() {
    private val lMotor = CANSparkMax(Constants.MotorControllers.kLeft, MotorType.kBrushed)
    private val rMotor = CANSparkMax(Constants.MotorControllers.kRight, MotorType.kBrushed)
    private val drivetrain = DifferentialDrive(lMotor, rMotor)

    private val lEncoder = Encoder(Constants.Encoders.kLEncoderA, Constants.Encoders.kLEncoderB)
    private val rEncoder = Encoder(Constants.Encoders.kREncoderA, Constants.Encoders.kREncoderB)

    private lateinit var gyro: AHRS

    init {
        /**
         * The RestoreFactoryDefaults method can be used to reset the configuration parameters in
         * the SPARK MAX to their factory default state. If no argument is passed, these parameters
         * will not persist between power cycles
         */
        lMotor.restoreFactoryDefaults()
        rMotor.restoreFactoryDefaults()

        lMotor.setInverted(true)

        // pi * wheel diam in / counts per revolution
        lEncoder.setDistancePerPulse(Math.PI * 6 / 360)
        rEncoder.setDistancePerPulse(Math.PI * 6 / 360)

        // initialize the gyro on the MXP port
        try {
            gyro = AHRS(SPI.Port.kMXP)
        } catch (err: RuntimeException) {
            DriverStation.reportError("Error instantiating navX: " + err.message, true)
        }
    }

    private enum class DriveMode {
        kSlow,
        kNormal
    }
    private var currentMode = DriveMode.kNormal // default mode is normal

    fun drive(f: Double, r: Double) {
        if (currentMode == DriveMode.kNormal)
            drivetrain.arcadeDrive(f/1.2, r/1.4)
        else
            drivetrain.arcadeDrive(f/1.3, r/1.6)
    }

    // unscaled drive
    fun autoDrive(f: Double, r: Double): CommandBase {
        return this.run { drivetrain.arcadeDrive(f, r, false) }
    }

    fun toggleSlowMode(): CommandBase {
        return this.runOnce {
            currentMode = if (currentMode == DriveMode.kSlow) DriveMode.kNormal else DriveMode.kSlow
        }
    }

    fun balance(): CommandBase {
        return this.run {
            drivetrain.arcadeDrive(Math.sin(gyro.getRoll() * (Math.PI / 180)), 0.0, false)
        }
    }

    override fun periodic() {
        // periodically update the dashboard with subsystem's state
        SmartDashboard.putBoolean("Is Normal Mode", currentMode == DriveMode.kNormal)
        SmartDashboard.putNumber("Gyro Roll", gyro.getRoll().toDouble())
        SmartDashboard.putNumber("Left Encoder Distance", lEncoder.getDistance())
        SmartDashboard.putNumber("Right Encoder Distance", rEncoder.getDistance())
    }
}
