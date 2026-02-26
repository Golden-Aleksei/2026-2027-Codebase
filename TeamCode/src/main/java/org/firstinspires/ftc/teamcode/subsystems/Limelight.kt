@file:Suppress("UnusedEquals")

package org.firstinspires.ftc.teamcode.subsystems

import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.geometry.Pose
import com.qualcomm.hardware.limelightvision.Limelight3A
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.ftc.ActiveOpMode
import org.firstinspires.ftc.teamcode.Routines

@Configurable
object Limelight : Subsystem {

    // Motifs
    enum class Motif {
        GPP,
        PGP,
        PPG,
        UNKNOWN
    }

    @JvmField
    var matchMotif = Motif.UNKNOWN

    var lastLength = 0
    var axial = 0.0
    var lateral = 0.0
    var yaw = 0.0

    var yawPIDFCoefficients = PIDCoefficients(0.0, 0.0, 0.0)
    var axialPIDCoefficients = PIDCoefficients(0.0, 0.0, 0.0)
    var lateralPIDCoefficients = PIDCoefficients(0.0, 0.0, 0.0)

    var axialOffset = 0.0
    var lateralOffset = 0.0
    var yawOffset = 0.0

    var yawPID = controlSystem {
        posPid(yawPIDFCoefficients)
    }
    var axialPID = controlSystem {
        posPid(axialPIDCoefficients)
    }
    var lateralPID = controlSystem {
        posPid(lateralPIDCoefficients)
    }

    lateinit var limeLight: Limelight3A

    override fun initialize() {
        limeLight = ActiveOpMode.hardwareMap.get(Limelight3A::class.java, "limelight")

        limeLight.pipelineSwitch(4)
        limeLight.start()
    }

    fun stop() {
        limeLight.stop()
    }

    override fun periodic() {
        val fiducialResults = limeLight.getLatestResult().fiducialResults
        if (!fiducialResults.isEmpty()) {
            val snapshot = fiducialResults[0]
            if (snapshot.fiducialId == 20 || snapshot.fiducialId == 24) {
                PedroComponent.follower.pose == PedroComponent.follower.pose.coordinateSystem.convertToPedro(
                    Pose(
                        snapshot.robotPoseFieldSpace.position.x,
                        snapshot.robotPoseFieldSpace.position.y,
                        snapshot.robotPoseFieldSpace.orientation.yaw
                    )
                )
            }
        }
    }

    var detectMotif = InstantCommand {
        val fiducialResult = limeLight.getLatestResult().fiducialResults
        if (!fiducialResult.isEmpty()) {
            val snapshot = fiducialResult[0]
            matchMotif = when (snapshot.fiducialId) {
                21 -> Motif.GPP
                22 -> Motif.PGP
                else -> Motif.PPG
            }
        }
    }
}