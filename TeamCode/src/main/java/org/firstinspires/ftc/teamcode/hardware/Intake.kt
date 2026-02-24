package org.firstinspires.ftc.teamcode.hardware

import dev.nextftc.core.commands.groups.ParallelGroup
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.CRServoEx
import dev.nextftc.hardware.powerable.SetPower

/**
 * Subsystem that controls the intake
 *
 * @author D4LM (Julian) - #18592 Golden Aleksei (2026-2027)
 */

object Intake: Subsystem {
    private val servo1 = CRServoEx("Servo1")
    private val servo2 = CRServoEx("Servo2")
    private val servo3 = CRServoEx("Servo3")

    private val zeroServo1 = SetPower(servo1, 0.0)
    private val zeroServo2 = SetPower(servo2, 0.0)
    private val zeroServo3 = SetPower(servo3, 0.0)
    val zero = ParallelGroup(zeroServo1, zeroServo2, zeroServo3).requires(Intake)

    private val reverseServo1 = SetPower(servo1, -0.25)
    private val reverseServo2 = SetPower(servo2, -0.25)
    private val reverseServo3 = SetPower(servo3, -0.25)
    val reverse = ParallelGroup(reverseServo1, reverseServo2, reverseServo3).requires(Intake)

    private val forwardServo1 = SetPower(servo1, 1.0)
    private val forwardServo2 = SetPower(servo2, 1.0)
    private val forwardServo3 = SetPower(servo3, 1.0)
    val forward = ParallelGroup(forwardServo1, forwardServo2, forwardServo3).requires(Intake)
}
