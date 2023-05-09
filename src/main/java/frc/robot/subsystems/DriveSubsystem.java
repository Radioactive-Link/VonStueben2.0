package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;

import frc.robot.Constants;

public class DriveSubsystem extends SubsystemBase {
    private CANSparkMax lMotor;
    private CANSparkMax rMotor;
    private DifferentialDrive drivetrain;
    private Encoder lEncoder = new Encoder(Constants.Encoders.lEncoderA, Constants.Encoders.lEncoderB);
    private Encoder rEncoder = new Encoder(Constants.Encoders.rEncoderA, Constants.Encoders.rEncoderB);

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

        //pi * wheel diam in / counts per revolution
        lEncoder.setDistancePerPulse(Math.PI * 6 / 360);
        rEncoder.setDistancePerPulse(Math.PI * 6 / 360);
    }

    private enum driveMode {
        SLOW,
        NORMAL
    }
    private driveMode currentMode = driveMode.NORMAL; // default mode is normal
    
    public void drive(double f, double r) {
        drivetrain.arcadeDrive(f,r);
    }

    // unscaled drive
    public CommandBase autoDrive(double f, double r) {
        return this.run(() -> drivetrain.arcadeDrive(f, r, false));
    }

    // non-commandbase variant incase above doesn't work.
    // public void autoDrive(double f, double r) {
    //     drivetrain.arcadeDrive(f, r, false);
    // }

    public CommandBase toggleSlowMode() {
        return this.runOnce(() -> 
            currentMode = currentMode == driveMode.SLOW
            ? driveMode.NORMAL : driveMode.SLOW );
    }

    public CommandBase balance() {
        return this.run(() -> drivetrain.arcadeDrive(
            Math.sin(gyro.getRoll() * (Math.PI / 180)), 0.0, false) );
    }

    @Override
    public void periodic() {
        // periodically update the dashboard with subsystem's state
        SmartDashboard.putBoolean("Is Normal Mode", currentMode == driveMode.NORMAL);
        SmartDashboard.putNumber("Gyro Roll", gyro.getRoll());
        SmartDashboard.putNumber("Left Encoder Distance",  lEncoder.getDistance());
        SmartDashboard.putNumber("Right Encoder Distance", rEncoder.getDistance());
    }
}
