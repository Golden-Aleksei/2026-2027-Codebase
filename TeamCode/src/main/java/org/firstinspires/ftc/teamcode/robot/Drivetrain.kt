package org.firstinspires.ftc.teamcode.robot

import dev.nextftc.bindings.BindingManager
import dev.nextftc.core.components.Component
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroDriverControlled
import dev.nextftc.ftc.Gamepads

class Drivetrain(var isProgrammer: Boolean): Component {
    override fun postInit() {
        PedroComponent.follower.update()
    }

    override fun postStartButtonPressed() {
        PedroComponent.follower.startTeleOpDrive()
    }
    override fun preUpdate() {
        PedroComponent.follower.update()
        BindingManager.update()

        val slowMultiplier = 0.5
        var turnMultiplier = 1.0

        var driverControlled = PedroDriverControlled(
            Gamepads.gamepad1.leftStickY,
            Gamepads.gamepad1.leftStickX,
            { Gamepads.gamepad1.rightStickX.get() * turnMultiplier}
        )

        var slowDriveControlled = PedroDriverControlled(
            { Gamepads.gamepad1.leftStickY.get() * slowMultiplier},
            { Gamepads.gamepad1.leftStickX.get() * slowMultiplier},
            { Gamepads.gamepad1.rightStickX.get() * slowMultiplier * turnMultiplier}
        )

        Gamepads.gamepad1.b
            .toggleOnBecomesTrue()
            .whenBecomesTrue { slowDriveControlled() }
            .whenBecomesFalse { driverControlled() }

        if (!isProgrammer) {
            Gamepads.gamepad1.rightBumper .whenBecomesTrue {turnMultiplier += 0.1}
        }
    }
}