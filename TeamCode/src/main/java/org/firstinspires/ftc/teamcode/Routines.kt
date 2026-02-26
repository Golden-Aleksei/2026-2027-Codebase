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
        get() = Shooter.start

    val stopShoot = InstantCommand(
        SequentialGroup(
            Shooter.stop,
            Intake.stop
        )
    )

    val intake: Command
        get() = ParallelGroup(Intake.forward, Shooter.reverseIntake)

    val haltIntake = Intake.stop

    val reverseIntake = ParallelGroup(Intake.reverse, Shooter.reverseIntake)
}
