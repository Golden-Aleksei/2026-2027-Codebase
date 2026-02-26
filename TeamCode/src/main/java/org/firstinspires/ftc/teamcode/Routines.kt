package org.firstinspires.ftc.teamcode

import dev.nextftc.core.commands.Command
import dev.nextftc.core.commands.delays.WaitUntil
import dev.nextftc.core.commands.groups.ParallelGroup
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.commands.utility.InstantCommand
import org.firstinspires.ftc.teamcode.subsystems.Intake
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import kotlin.time.Duration.Companion.milliseconds

object Routines {
    val shoot
        get() = SequentialGroup(
            Shooter.start.withDeadline(WaitUntil { Shooter.rMotor.velocity > Shooter.controller.goal.velocity }),
            Intake.forward.endAfter(200.milliseconds),
            Intake.stop,
            Shooter.stop
        )

    val stopShoot = InstantCommand(
        SequentialGroup(
            Shooter.stop,
            Intake.stop
        )
    )

    val intake: Command
        get() = ParallelGroup(Intake.forward, Shooter.reverseIntake).endAfter(200.milliseconds)

    val haltIntake = Intake.stop

    val reverseIntake = ParallelGroup(Intake.reverse, Shooter.reverseIntake)
}
