package org.firstinspires.ftc.teamcode

import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.RobotLog
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import dev.nextftc.hardware.driving.FieldCentric
import dev.nextftc.hardware.driving.MecanumDriverControlled
import dev.nextftc.hardware.impl.Direction
import dev.nextftc.hardware.impl.IMUEx
import dev.nextftc.hardware.impl.MotorEx
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.panels.Drawing
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.subsystems.Intake
import org.firstinspires.ftc.teamcode.subsystems.Limelight
import org.firstinspires.ftc.teamcode.subsystems.Limelight.matchMotif
import org.firstinspires.ftc.teamcode.subsystems.Shooter

@TeleOp(name = "Competition TeleOp")
class CompetitionTeleOp : NextFTCOpMode() {
    init {
        addComponents(
            SubsystemComponent(Intake, Shooter, Limelight),
            BulkReadComponent,
            PedroComponent(Constants::createFollower),
            BindingsComponent
        )
        telemetry = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)
    }

    private val leftFrontMotor = MotorEx("lf").brakeMode()
    private val rightFrontMotor = MotorEx("rf").brakeMode()
    private val leftRearMotor = MotorEx("lr").brakeMode()
    private val rightRearMotor = MotorEx("rr").brakeMode()
    private val imu = IMUEx("imu", Direction.BACKWARD, Direction.FORWARD).zeroed()

    override fun onStartButtonPressed() {
        // You can put commands here

        val driveControlled = MecanumDriverControlled(
            leftFrontMotor,
            rightFrontMotor,
            leftRearMotor,
            rightRearMotor,
            -Gamepads.gamepad1.leftStickY,
            Gamepads.gamepad1.leftStickX,
            Gamepads.gamepad1.rightStickX,
            FieldCentric(imu)
        )
        driveControlled()

        Gamepads.gamepad1.rightStickButton whenBecomesTrue { imu.zero() }

        // Slow mode
        Gamepads.gamepad1.b whenBecomesTrue {
            driveControlled.scalar = 0.5
        } whenBecomesFalse {
            driveControlled.scalar = 1.0
        }

        Gamepads.gamepad2.rightBumper whenBecomesTrue Routines.intake whenBecomesFalse Routines.haltIntake
        Gamepads.gamepad2.leftBumper whenBecomesTrue Routines.reverseIntake whenBecomesFalse Routines.haltIntake

        Gamepads.gamepad2.rightTrigger.atLeast(0.75) whenBecomesTrue Routines.haltIntake

        Gamepads.gamepad2.y.toggleOnBecomesTrue() whenBecomesTrue Routines.shoot
    }

    override fun onUpdate() {
        Drawing.drawDebug(PedroComponent.follower)

        RobotLog.d(
            "Motor Amp: Left Front Motor: " + leftFrontMotor.motor.getCurrent(CurrentUnit.AMPS)
                .toString()
        )
        RobotLog.d(
            "Motor Amp: Left Rear Motor: " + leftRearMotor.motor.getCurrent(CurrentUnit.AMPS)
                .toString()
        )
        RobotLog.d(
            "Motor Amp: Right Front Motor: " + rightFrontMotor.motor.getCurrent(CurrentUnit.AMPS)
                .toString()
        )
        RobotLog.d(
            "Motor Amp: Right Rear Motor: " + rightRearMotor.motor.getCurrent(CurrentUnit.AMPS)
        )

        RobotLog.d(
            "Motor Amp: Left Shooter Motor: " + Shooter.lMotor.motor.getCurrent(CurrentUnit.AMPS)
                .toString()
        )
        RobotLog.d(
            "Motor Amp: Right Shooter Motor: " + Shooter.rMotor.motor.getCurrent(CurrentUnit.AMPS)
                .toString()
        )

        ActiveOpMode.telemetry.addData("Limelight on", Limelight.limeLight.isRunning)

        ActiveOpMode.telemetry.addData(
            "Current left velocity",
            Shooter.lMotor.state.velocity / Shooter.TICKS_PER_REV * 60
        )
        ActiveOpMode.telemetry.addData(
            "Current right velocity",
            Shooter.rMotor.state.velocity / Shooter.TICKS_PER_REV * 60
        )
        ActiveOpMode.telemetry.addData(
            "Target velocity",
            Shooter.controller.goal.velocity / Shooter.TICKS_PER_REV * 60
        )
        ActiveOpMode.telemetry.addData("Current left power: ", Shooter.lMotor.power)
        ActiveOpMode.telemetry.addData("Current right power: ", Shooter.rMotor.power)

        ActiveOpMode.telemetry.addData("Motif: ", matchMotif.toString())

        ActiveOpMode.telemetry.update()
    }
}