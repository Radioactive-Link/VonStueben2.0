package frc.robot

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. inside the companion object). Do not put anything functional in this class.
 *
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
class Constants {
    object Controllers {
        const val kDriverControllerPort = 0
    }
    object MotorControllers {
        const val kLeft = 1
        const val kRight = 2
    }
    object Encoders {
        const val kLEncoderA = 0
        const val kLEncoderB = 1
        const val kREncoderA = 2
        const val kREncoderB = 3
    }
}