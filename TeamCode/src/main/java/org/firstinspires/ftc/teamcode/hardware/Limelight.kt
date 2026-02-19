package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import dev.nextftc.bindings.BindingManager
import dev.nextftc.core.commands.Command
import dev.nextftc.core.commands.conditionals.IfElseCommand
import dev.nextftc.core.components.Component
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.Gamepads
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition

class Limelight(val isTeleop: Boolean) : Component {

    private lateinit var limelight: Limelight3A
    private val limelightLight = ServoEx("Limelightlight")

    private var headingError = 0.0
    private var robotHeading = 0.0

    override fun preStartButtonPressed() {
        limelight = ActiveOpMode.hardwareMap.get(Limelight3A::class.java, "Limelight")
        // Nobody should complain if it begins reading before auto starts
        limelight.start()
        limelight.pipelineSwitch(4)
    }

    override fun preUpdate() {
        // Probably should be var because it is going to keep updating
        var result: LLResult = limelight.latestResult

        if (result.isValid) {
            headingError = result.tx
            robotHeading = result.botpose.orientation.yaw

            if (5 >= headingError && headingError >= -5) {
                SetPosition(limelightLight, 1.0)
            } else {
                SetPosition(limelightLight, 0.0)
            }
        }

        if (isTeleop) {
            Gamepads.gamepad1.x
                .whenBecomesTrue { IfElseCommand(
                    {headingError > 0.1 || headingError < -0.1},
                    { PedroComponent.follower.heading = Math.toRadians(robotHeading)
                        PedroComponent.follower.turnTo(robotHeading - headingError)} as Command
                )}
                .whenBecomesFalse { PedroComponent.follower.startTeleopDrive() }
        }
    }
}