package org.firstinspires.ftc.teamcode.pedroPathing

import dev.nextftc.bindings.BindingManager
import dev.nextftc.core.components.Component
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroDriverControlled
import dev.nextftc.ftc.Gamepads

/**
 * Component that sets up bindings for teleop driving.
 *
 * @param isOneGamepad Parameter to check if you have one or two gamepads installed.
 * This cannot be changed when the program is running.
 *
 * @author D4LM (Julian) - #18592 Golden Aleksei (2026-2027)
 */
class TeleOpDrivetrain(val isOneGamepad: Boolean): Component {

    override fun preStartButtonPressed() {
        BindingManager.update()
        val slowMultiplier = 0.5
        var turnMultiplier = 1.0

        var driveControlled = PedroDriverControlled(
            Gamepads.gamepad1.leftStickY,
            Gamepads.gamepad1.leftStickX,
            { Gamepads.gamepad1.rightStickX.get() * turnMultiplier }
        )
        driveControlled()

        var slowDriveControlled = PedroDriverControlled(
            { Gamepads.gamepad1.leftStickY.get() * slowMultiplier },
            { Gamepads.gamepad1.leftStickX.get() * slowMultiplier },
            { Gamepads.gamepad1.rightStickX.get() * slowMultiplier * turnMultiplier }
        )

        Gamepads.gamepad1.b
            .toggleOnBecomesTrue()
            .whenBecomesTrue { slowDriveControlled() }
            .whenBecomesFalse { driveControlled() }

        if (!isOneGamepad) {
            Gamepads.gamepad1.rightBumper
                .whenBecomesTrue { turnMultiplier += 0.1 }
            Gamepads.gamepad1.leftBumper
                .whenBecomesTrue { turnMultiplier -= 0.1 }
        }

        PedroComponent.follower.startTeleopDrive()
    }
}