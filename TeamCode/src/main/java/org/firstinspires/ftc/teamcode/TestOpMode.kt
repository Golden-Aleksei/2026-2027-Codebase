package org.firstinspires.ftc.teamcode

import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.RobotLog
import dev.nextftc.bindings.BindingManager
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

@TeleOp(name = "test")
class TestOpMode : NextFTCOpMode( ) {
    init {
        addComponents(
            SubsystemComponent(Intake, Shooter, Limelight),
            BulkReadComponent,
            PedroComponent(Constants::createFollower),
            BindingsComponent
        )
        telemetry = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)
    }

    override fun onStartButtonPressed() {
        Gamepads.gamepad2.rightBumper .toggleOnBecomesTrue()
            .whenBecomesTrue {
                Shooter.start
            }
            .whenBecomesFalse {
                Shooter.stop
            }
    }

    override fun onUpdate() {
        ActiveOpMode.telemetry.addData("Right Bumper ",Gamepads.gamepad2.rightBumper.get().toString())
        ActiveOpMode.telemetry.update()
        BindingManager.update()
    }

    override fun onStop() {
        BindingManager.reset()
    }
}