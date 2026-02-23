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
import org.firstinspires.ftc.teamcode.nextFTC.DecoupledMotorEx

@Configurable
object Shooter : Subsystem {
    const val TICKS_PER_REV = 112 / 4

    var controlled = true

    val lMotor = DecoupledMotorEx("lOuttake", "lf").reversed()
    val rMotor = DecoupledMotorEx("rOuttake", "rf").reversed()

    @JvmField
    var basicFFCoefficients =
        BasicFeedforwardParameters(0.001, 0.0, 0.0) // Maybe modify these FF values?

    @JvmField
    var velPIDCoefficients = PIDCoefficients(0.0, 0.0, 0.0) // TODO: Modify these PID values

    override fun initialize() {
        lMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        controller.goal = KineticState()
    }

    @JvmField
    val controller = controlSystem {
        velPid(velPIDCoefficients)
        basicFF(basicFFCoefficients)
    }

    var loopCount = 0
    const val LOOP_THRESHOLD = 40
    val checkWithinToleranceForCorrectNumberOfLoops = WaitUntil {
        if (loopCount >= LOOP_THRESHOLD) {
            true
        } else {
            if (rMotor.velocity > (controller.goal.velocity - 10) && rMotor.velocity < (controller.goal.velocity + 60)) {
                loopCount++
            } else {
                loopCount = 0
            }

            false
        }
    }

    val start = ParallelGroup(
        RunToVelocity(
            controller,
            (2400 / 60.0) * TICKS_PER_REV,
            KineticState(Double.POSITIVE_INFINITY, 500.0, Double.POSITIVE_INFINITY)
        ).requires(this),
        checkWithinToleranceForCorrectNumberOfLoops
    )

    val stop = RunToVelocity(
        controller, 0.0, KineticState(
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY
        )
    ).requires(this)

    val reverseIntake = RunToVelocity(controller, -((1000 / 60.0) * TICKS_PER_REV)).requires(this)

    override fun periodic() {
        if (controlled) {
            lMotor.power = controller.calculate(lMotor.state)
            rMotor.power = controller.calculate(rMotor.state)
        }
    }
}