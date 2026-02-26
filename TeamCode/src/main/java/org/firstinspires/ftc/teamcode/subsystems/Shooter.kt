package org.firstinspires.ftc.teamcode.subsystems

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.hardware.DcMotor
import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.control.feedforward.BasicFeedforwardParameters
import dev.nextftc.core.commands.delays.WaitUntil
import dev.nextftc.core.commands.groups.ParallelGroup
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.controllable.RunToVelocity
import dev.nextftc.hardware.impl.MotorEx

@Configurable
object Shooter : Subsystem {
    const val TICKS_PER_REV = 112 / 4

    var controlled = true

    @JvmField
    var targetVelocity = 400.0

    val lMotor = MotorEx("LOuttake")
    val rMotor = MotorEx("ROuttake")

    @JvmField
    var LFFCoefficients = BasicFeedforwardParameters(0.0,0.0,0.0)
    var RFFCoefficeints = BasicFeedforwardParameters(0.0, 0.0, 0.0)

    @JvmField
    var LVelPIDCoefficeients = PIDCoefficients(0.0,0.0,0.0)
    var RVelPIDCoefficients= PIDCoefficients(125.0,0.0,0.0)

    override fun initialize() {
        lMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rMotor.reversed()
        lController.goal = KineticState()
        rController.goal = KineticState()
    }

    @JvmField
    val lController = controlSystem {
        velPid(LVelPIDCoefficeients)
        basicFF(LFFCoefficients)
    }

    @JvmField
    val rController = controlSystem {
        velPid(RVelPIDCoefficients)
        basicFF(RFFCoefficeints)
    }

    var loopCount = 0
    const val LOOP_THRESHOLD = 40
    val checkWithinToleranceForCorrectNumberOfLoops = WaitUntil {
        if (loopCount >= LOOP_THRESHOLD) {
            true
        } else {
            if (rMotor.velocity > (rController.goal.velocity - 10) && rMotor.velocity < (rController.goal.velocity + 60)) {
                loopCount++
            } else {
                loopCount = 0
            }

            false
        }
    }

    @JvmField
    val start = ParallelGroup(
        RunToVelocity(
            lController,
            (2400 / 60.0) * TICKS_PER_REV,
            KineticState(Double.POSITIVE_INFINITY, targetVelocity, Double.POSITIVE_INFINITY)
        ),
        RunToVelocity(
            rController,
            (2400 / 60.0) * TICKS_PER_REV,
            KineticState(Double.POSITIVE_INFINITY, targetVelocity, Double.POSITIVE_INFINITY)
        ),
        checkWithinToleranceForCorrectNumberOfLoops
    ).requires(this)

    val stop = ParallelGroup(
        RunToVelocity(
            lController, 0.0, KineticState(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
        ), RunToVelocity(
            rController, 0.0, KineticState(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
        )
    ).requires(this)

    val reverseIntake = ParallelGroup(
        RunToVelocity(lController, -((20 / 60.0) * TICKS_PER_REV)),
        RunToVelocity(rController, -((20 / 60.0) * TICKS_PER_REV))
    ).requires(this)

    override fun periodic() {
        if (controlled) {
            lMotor.power = lController.calculate(lMotor.state)
            rMotor.power = rController.calculate(rMotor.state)
        }
    }
}