package org.firstinspires.ftc.teamcode.hardware

import dev.nextftc.core.commands.Command
import dev.nextftc.core.commands.conditionals.switchCommand
import dev.nextftc.core.commands.groups.ParallelGroup

/**
 * Command that switches how the intake and outtake operates depending on mode selection
 *
 * @param [modes] An int, see below for the different modes:
 *
 * 0: Intake and outtake stops
 *
 * 1: Intake stops
 *
 * 2: Outtake stops
 *
 * 3: Intake collects artifacts
 *
 * 4: Intake collects artifacts and outtake stops artifacts from exiting
 *
 * 5: Outtake scores artifacts from large launch zone
 *
 * 6: Outtake scores artifacts from small launch zone
 *
 * 7: Intake moves artifacts for outtake to score artifacts into large launch zone
 *
 * 8: Intake moves artifacts for outtake to score artifacts into small launch zone
 *
 * 9: Intake and outtake reverses to kick artifacts out of robot without attempt to score
 * */


fun robotModes(mode: Int): Command {
    return when(mode) {
        0 -> ParallelGroup(Intake.zero, Outtake.zero).requires(Intake, Outtake)
        1 -> Intake.zero.requires(Intake)
        2 -> Outtake.zero.requires(Outtake)
        3 -> Intake.forward.requires(Intake)
        4 -> ParallelGroup(Intake.forward, Outtake.reverse).requires(Intake, Outtake)
        5 -> Outtake.shortVelocityScore.requires(Outtake)
        6 -> Outtake.longVelocityScore.requires(Outtake)
        7 -> ParallelGroup(Intake.forward, Outtake.shortVelocityScore).requires(Intake, Outtake)
        8 -> ParallelGroup(Intake.forward, Outtake.longVelocityScore).requires(Intake, Outtake)
        9 -> ParallelGroup(Intake.reverse, Outtake.reverse).requires(Intake, Outtake)
        else -> ParallelGroup(Intake.zero, Outtake.zero).requires(Intake, Outtake) // Default safe case
    }
}