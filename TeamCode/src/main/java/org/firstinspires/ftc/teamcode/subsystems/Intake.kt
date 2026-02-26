package org.firstinspires.ftc.teamcode.subsystems

import dev.nextftc.core.commands.groups.ParallelGroup
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.CRServoEx
import dev.nextftc.hardware.powerable.SetPower

object Intake : Subsystem {
    val servo1 = CRServoEx("servo1")
    val servo2 = CRServoEx("servo2")
    val servo3 = CRServoEx("servo3")

    val stop = ParallelGroup(
        SetPower(servo1, 0.0),
        SetPower(servo2, 0.0),
        SetPower(servo3, 0.0)
    ).requires(this)

    val forward = ParallelGroup(
        SetPower(servo1, 1.0),
        SetPower(servo2, 1.0),
        SetPower(servo3, 1.0)
    ).requires(this)

    val reverse = ParallelGroup(
        SetPower(servo1, -0.25),
        SetPower(servo2, -0.25),
        SetPower(servo3, -0.25)
    ).requires(this)


}