package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;

import frc.robot.Constants;

public class DriveSubsystem extends SubsystemBase {
    //TODO: Figure out what kind of encoders are on the motors
    private CANSparkMax lMotor;
    private CANSparkMax rMotor;
    private DifferentialDrive drivetrain;

    private AHRS gyro;

    //constructor
    public DriveSubsystem() {
        // initialize motors and drivetrain
        lMotor = new CANSparkMax(Constants.MotorControllers.LEFT,  MotorType.kBrushed);
        rMotor = new CANSparkMax(Constants.MotorControllers.RIGHT, MotorType.kBrushed);

        /**
         * The RestoreFactoryDefaults method can be used to reset the configuration parameters
         * in the SPARK MAX to their factory default state. If no argument is passed, these
         * parameters will not persist between power cycles
         */
        lMotor.restoreFactoryDefaults();
        rMotor.restoreFactoryDefaults();

        lMotor.setInverted(true);

        drivetrain = new DifferentialDrive(lMotor, rMotor);

        // initialize the gyro on the MXP port
        try {
            gyro = new AHRS(SPI.Port.kMXP);
        } catch (RuntimeException err) {
            DriverStation.reportError("Error instantiating navX: " + err.getMessage(), true);
        }
    }

    private enum driveMode {
        SLOW,
        NORMAL
    }
    private driveMode currentMode = driveMode.NORMAL; // default mode is normal

    public CommandBase drive(double f, double r) {
        return this.run(() -> {
            if (currentMode == driveMode.NORMAL) 
                drivetrain.arcadeDrive(f/1.2, r/1.4);
            else
                drivetrain.arcadeDrive(f/1.3, r/1.6); });
    }

    // unscaled drive
    public CommandBase autoDrive(double f, double r) {
        return this.run(() -> drivetrain.arcadeDrive(f, r, false));
    }

    public CommandBase toggleSlowMode() {
        return this.runOnce(() -> 
            currentMode = currentMode == driveMode.SLOW
            ? driveMode.NORMAL : driveMode.SLOW );
    }

    public CommandBase balance() {
        return this.run(() -> drivetrain.arcadeDrive(
            Math.sin(gyro.getRoll()) * (Math.PI / 180), 0.0, false) );
    }

    @Override
    public void periodic() {
        // periodically update the dashboard with subsystem's state
        SmartDashboard.putBoolean("Is Normal Mode", currentMode == driveMode.NORMAL);
        SmartDashboard.putNumber("Gyro Roll", gyro.getRoll());
    }
}
