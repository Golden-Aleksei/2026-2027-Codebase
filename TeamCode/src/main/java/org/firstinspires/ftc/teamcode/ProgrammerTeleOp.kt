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

    override fun onStartButtonPressed() {
        BindingManager.update()
        telemetryM = PanelsTelemetry.telemetry
        Gamepads.gamepad1.rightBumper whenTrue RobotModes(4) whenBecomesFalse RobotModes(1)

    }

    override fun onUpdate() {
        BindingManager.update()
        telemetryM.update()


    }

    override fun onStop() {
        BindingManager.reset()
    }
}