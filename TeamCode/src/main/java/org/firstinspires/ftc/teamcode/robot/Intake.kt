package org.firstinspires.ftc.teamcode.robot

import dev.nextftc.core.commands.groups.ParallelGroup
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.CRServoEx
import dev.nextftc.hardware.powerable.SetPower

class Intake {
    object Intake: Subsystem {
        private val servo1 = CRServoEx("Servo1")
        private val servo2 = CRServoEx("Servo2")
        private val servo3 = CRServoEx("Servo3")

        private val zeroServo1 = SetPower(servo1, 0.0)
        private val zeroServo2 = SetPower(servo2, 0.0)
        private val zeroServo3 = SetPower(servo3, 0.0)
        val zeroServo = ParallelGroup(zeroServo1, zeroServo2, zeroServo3).requires(this)

        private val reverseServo1 = SetPower(servo1, -0.25)
        private val reverseServo2 = SetPower(servo2, -0.25)
        private val reverseServo3 = SetPower(servo3, -0.25)
        val reverseServo = ParallelGroup(reverseServo1, reverseServo2, reverseServo3).requires(this)

        private val forwardServo1 = SetPower(servo1, 1.0)
        private val forwardServo2 = SetPower(servo2, 1.0)
        private val forwardServo3 = SetPower(servo3, 1.0)
        val forwardServo = ParallelGroup(forwardServo1, forwardServo2, forwardServo3).requires(this)
    }
}