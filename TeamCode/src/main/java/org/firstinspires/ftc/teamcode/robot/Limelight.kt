package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import dev.nextftc.core.components.Component
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.Gamepads
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition

class Limelight : Component {
    private lateinit var limelight: Limelight3A
    private val limelightLight = ServoEx("Limelightlight")
    var headingError = 0.0
    var robotHeading = 0.0



    override fun preStartButtonPressed() {
        limelight = ActiveOpMode.hardwareMap.get(Limelight3A::class.java, "Limelight")
        limelight.start()
        limelight.pipelineSwitch(4)
    }

    override fun preUpdate() {
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
    }



}