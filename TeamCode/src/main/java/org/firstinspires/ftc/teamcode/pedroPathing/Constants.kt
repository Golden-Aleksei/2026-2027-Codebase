package org.firstinspires.ftc.teamcode.pedroPathing

import com.pedropathing.control.FilteredPIDFCoefficients
import com.pedropathing.follower.Follower
import com.pedropathing.follower.FollowerConstants
import com.pedropathing.ftc.FollowerBuilder
import com.pedropathing.ftc.drivetrains.MecanumConstants
import com.pedropathing.ftc.localization.constants.PinpointConstants
import com.pedropathing.paths.PathConstraints
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

class Constants {
    companion object {
        var followerConstants: FollowerConstants = FollowerConstants()
            .forwardZeroPowerAcceleration(-28.495093589825387)
            .lateralZeroPowerAcceleration(-31.885836425241894)
            .translationalPIDFCoefficients(com.pedropathing.control.PIDFCoefficients(
                0.1,
                0.0,
                0.01,
                0.021))
            .headingPIDFCoefficients(com.pedropathing.control.PIDFCoefficients(
                1.0,
                0.0,
                0.03,
                0.03))
            .drivePIDFCoefficients(FilteredPIDFCoefficients(
                0.1,
                0.0,
                0.01,
                0.6,
                0.0))
            .centripetalScaling(0.0005)
            .mass(11.0);

        var pathConstants: PathConstraints = PathConstraints(0.99,
            100.0,
            1.0,
            1.0)

        @JvmStatic
        fun createFollower(hardwareMap: HardwareMap): Follower {
            return FollowerBuilder(followerConstants, hardwareMap)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstants)
                .mecanumDrivetrain(driveConstants)
                .build()
        }

        var driveConstants: MecanumConstants = MecanumConstants()
            .xVelocity(60.71848182978592)
            .yVelocity(53.26731956662155)
            .maxPower(1.0)
            .rightFrontMotorName("rf")
            .rightRearMotorName("rr")
            .leftRearMotorName("lr")
            .leftFrontMotorName("lf")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)

        var localizerConstants: PinpointConstants = PinpointConstants()
            .forwardPodY(1.5)
            .strafePodX(1.0)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
    }
}