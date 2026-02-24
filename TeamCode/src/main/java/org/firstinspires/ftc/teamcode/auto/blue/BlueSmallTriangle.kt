package org.firstinspires.ftc.teamcode.auto.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import dev.nextftc.extensions.pedro.FollowPath
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.hardware.Limelight
import org.firstinspires.ftc.teamcode.hardware.robotModes
import kotlin.time.Duration.Companion.minutes

@Autonomous(name = "Blue Small Triangle", group = "Blue")
class BlueSmallTriangle : NextFTCOpMode() {
    init {
        addComponents(
            BulkReadComponent,
            Limelight(false)
        )
    }


    override fun onStartButtonPressed() {
        FollowPath(BluePaths.preloadScoreLargeTriangle).then(
        robotModes(5).endAfter(1.minutes),
            robotModes(0)
        )
    }
}