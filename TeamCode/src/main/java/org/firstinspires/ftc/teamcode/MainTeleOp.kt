package org.firstinspires.ftc.teamcode

import com.bylazar.telemetry.PanelsTelemetry
import com.bylazar.telemetry.TelemetryManager
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.bindings.BindingManager
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.hardware.Intake
import org.firstinspires.ftc.teamcode.hardware.Limelight
import org.firstinspires.ftc.teamcode.hardware.Outtake
import org.firstinspires.ftc.teamcode.hardware.robotModes
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.pedroPathing.TeleOpDrivetrain

@TeleOp(name = "Main TeleOp")
class MainTeleOp : NextFTCOpMode(){
    init {
        addComponents(
            BulkReadComponent,
            SubsystemComponent(Intake, Outtake),
            BindingsComponent,
            PedroComponent(Constants::createFollower),
            TeleOpDrivetrain(false),
            Limelight(true)
        )
    }

    lateinit var telemetryM: TelemetryManager

    override fun onInit() {
        telemetryM = PanelsTelemetry.telemetry

        Gamepads.gamepad2.rightBumper whenTrue robotModes(4) whenBecomesFalse robotModes(1)

        Gamepads.gamepad2.leftBumper whenTrue robotModes(9) whenBecomesFalse robotModes(0)

        Gamepads.gamepad2.rightTrigger.atLeast(0.75) whenBecomesTrue robotModes(2)

        Gamepads.gamepad2.y .toggleOnBecomesTrue() whenTrue robotModes(6) whenBecomesFalse robotModes(2)

        Gamepads.gamepad2.a .toggleOnBecomesTrue() whenTrue robotModes(5) whenBecomesFalse robotModes(2)
    }

    override fun onUpdate() {
        BindingManager.update()
        telemetryM.update()

    }

    override fun onStop() {
        BindingManager.reset()
    }
}