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
import org.firstinspires.ftc.teamcode.hardware.RobotModes
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.pedroPathing.TeleOpDrivetrain

@TeleOp(name = "Programmer TeleOp")
class ProgrammerTeleOp : NextFTCOpMode(){
    init {
        addComponents(
            BulkReadComponent,
            SubsystemComponent(Intake, Outtake),
            BindingsComponent,
            PedroComponent(Constants::createFollower),
            TeleOpDrivetrain(true),
            Limelight(true)
        )
    }

    lateinit var telemetryM: TelemetryManager

    override fun onInit() {
        telemetryM = PanelsTelemetry.telemetry
    }

    override fun onUpdate() {
        BindingManager.update()
        telemetryM.update()

        Gamepads.gamepad1.rightBumper whenTrue RobotModes(4) whenBecomesFalse RobotModes(1)

        Gamepads.gamepad1.leftBumper whenTrue RobotModes(9) whenBecomesFalse RobotModes(0)

        Gamepads.gamepad1.rightTrigger.atLeast(0.75) whenBecomesTrue RobotModes(2)

        Gamepads.gamepad1.y .toggleOnBecomesTrue() whenTrue RobotModes(6) whenBecomesFalse RobotModes(2)

        Gamepads.gamepad1.a .toggleOnBecomesTrue() whenTrue RobotModes(5) whenBecomesFalse RobotModes(2)
    }

    override fun onStop() {
        BindingManager.reset()
    }
}