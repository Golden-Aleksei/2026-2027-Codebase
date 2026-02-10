package org.firstinspires.ftc.teamcode

import com.bylazar.telemetry.PanelsTelemetry
import com.bylazar.telemetry.TelemetryManager
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.bindings.BindingManager
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.core.units.rad
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroDriverControlled
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.pedroPathing.Constants

@TeleOp(name = "Main TeleOp")
class MainTeleOp: NextFTCOpMode() {
    lateinit var telemetryM: TelemetryManager
    var slowMode = false

    override fun onInit() {
        telemetryM = PanelsTelemetry.telemetry
        addComponents(
            BulkReadComponent,
            BindingsComponent,
            PedroComponent(Constants::createFollower),
            SubsystemComponent(Robot.Robot),
            Limelight()
        )
        PedroComponent.follower.update()
    }

    override fun onStartButtonPressed() {
        PedroComponent.follower.startTeleOpDrive();
    }

    override fun onUpdate() {
        PedroComponent.follower.update()
        telemetryM.update()
        telemetry.update()
        BindingManager.update()
        val robot = Robot.Robot // Stop me from going crazy

        var turnMultiplier = 1.0
        val slowMultiplier = 0.5
        val driverControlled = PedroDriverControlled(
            Gamepads.gamepad1.leftStickY,
            Gamepads.gamepad1.leftStickX,
            { Gamepads.gamepad1.rightStickX.get() * turnMultiplier}
        )

        val slowDriverControlled = PedroDriverControlled(
            {Gamepads.gamepad1.leftStickY.get() * slowMultiplier},
            {Gamepads.gamepad1.leftStickX.get() * slowMultiplier},
            {Gamepads.gamepad1.rightStickX.get() * slowMultiplier * turnMultiplier}
        )

        if (!slowMode) {
            driverControlled()
        } else {
            slowDriverControlled()
        }

        Gamepads.gamepad1.b
            .toggleOnBecomesTrue()
            .whenTrue{ slowMode }
            .whenFalse { !slowMode }

        Gamepads.gamepad1.rightBumper whenTrue { turnMultiplier + 0.1 }

        Gamepads.gamepad1.leftBumper whenTrue { turnMultiplier - 0.1 }

        // Gamepad 2
        Gamepads.gamepad2.rightBumper whenTrue robot.intakeRunning whenBecomesFalse robot.intakeStop

        Gamepads.gamepad2.leftBumper whenTrue robot.intakeReverseOuttakeReverse whenBecomesFalse robot.allStop

        Gamepads.gamepad2.rightTrigger.atLeast(0.75) whenBecomesTrue robot.outtakeStop

        Gamepads.gamepad2.y
            .toggleOnBecomesTrue()
            .whenBecomesFalse { robot.outtakeStop }
            .whenTrue { robot.outtakeRunningLong }

        Gamepads.gamepad2.a
            .toggleOnBecomesTrue()
            .whenTrue( robot.outtakeRunningShort)
            .whenBecomesFalse { robot.outtakeStop }

        Gamepads.gamepad1.x
            .whenBecomesTrue(
                (if (Limelight().headingError > 0.1 || Limelight().headingError < -0.1) {
                    PedroComponent.follower.heading = Math.toRadians(Limelight().robotHeading)
                    PedroComponent.follower.turnTo(Limelight().robotHeading- Limelight().headingError)
                } else {

                }) as Runnable)
            .whenBecomesFalse({PedroComponent.follower.startTeleOpDrive()})
    }

    override fun onStop() {
        BindingManager.reset()
    }

}