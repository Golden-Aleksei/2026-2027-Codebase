package org.firstinspires.ftc.teamcode.auto.blue

import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.Path

object BluePaths{
    init {
        BluePoses
    }
    val preloadScoreLargeTriangle = Path(BezierLine(BluePoses.launchPointSmallTriangle, BluePoses.scorePointLargeTriangle))
}

object BluePoses{
    val launchPointSmallTriangle = Pose(56.0,8.0, Math.toRadians(90.0))
    val scorePointLargeTriangle = Pose(56.0,87.0,Math.toRadians(135.0))
}