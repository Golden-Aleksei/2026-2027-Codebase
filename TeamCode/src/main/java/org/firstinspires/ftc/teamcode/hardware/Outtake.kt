package org.firstinspires.ftc.teamcode.hardware

import dev.nextftc.control.builder.controlSystem
import dev.nextftc.core.commands.groups.ParallelGroup
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.controllable.RunToVelocity
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition

/**
 * Subsystem that controls the outtake
 *
 * @author D4LM (Julian) - #18592 Golden Aleksei (2026-2027)
 */
object Outtake : Subsystem {
    @JvmField
    var longVelocity = 800.0
    var shortVelocity = 680.0

    private var lightFlashPower = 1.0

    private val lMotor = MotorEx("LOuttake")
    private val rMotor = MotorEx("ROuttake").reversed()
    private val light = ServoEx("Light")

    private val lController = controlSystem {
        velPid(125.0,0.0,0.006)
        basicFF(14.2)
    }

    private val rController = controlSystem {
        velPid(125.0, 0.0, 0.006)
        basicFF(13.4)
    }

    override fun periodic() {
        lMotor.power = lController.calculate(lMotor.state)
        rMotor.power = rController.calculate(rMotor.state)

        lightFlashPower = if (lightFlashPower == 1.0) { 0.0 } else { 1.0 }

        // Don't need to check both, if there is a difference between both, then that is another problem related to PIDF tuning
        if (longVelocity + 20 >= rMotor.velocity && rMotor.velocity >= longVelocity - 20) {
            lightOn
        } else if (shortVelocity + 20 >= rMotor.velocity && rMotor.velocity >= shortVelocity - 20) {
            lightFlash
        } else {
            lightOff
        }
    }

    private val lightOff = SetPosition(light, 0.0).requires(light)
    private val lightOn = SetPosition(light, 1.0).requires(light)
    private val lightFlash = SetPosition(light, lightFlashPower).requires(light)

    private val lZero = RunToVelocity(lController, 0.0)
    private val rZero = RunToVelocity(rController, 0.0)
    val zero = ParallelGroup(lZero, rZero).requires(Outtake)

    private val lReverse = RunToVelocity(lController, -60.0)
    private val rReverse = RunToVelocity(rController, -60.0)
    val reverse = ParallelGroup(lReverse, rReverse).requires(Outtake)

    private val lLongV = RunToVelocity(lController, longVelocity)
    private val rLongV = RunToVelocity(rController, longVelocity)
    val longVelocityScore = ParallelGroup(lLongV, rLongV).requires(Outtake)

    private val lShortV = RunToVelocity(lController, shortVelocity)
    private val rShortV = RunToVelocity(rController, shortVelocity)
    val shortVelocityScore = ParallelGroup(lShortV, rShortV).requires(Outtake)
}