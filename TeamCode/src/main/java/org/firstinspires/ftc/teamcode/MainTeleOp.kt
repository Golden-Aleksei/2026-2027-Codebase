package org.firstinspires.ftc.teamcode

import com.bylazar.telemetry.PanelsTelemetry
import com.bylazar.telemetry.TelemetryManager
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.bindings.BindingManager
import dev.nextftc.core.commands.Command
import dev.nextftc.core.commands.conditionals.IfElseCommand
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroDriverControlled
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.robot.Drivetrain
import org.firstinspires.ftc.teamcode.robot.Limelight
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp(name = "Main TeleOp")
class MainTeleOp: NextFTCOpMode() {
    lateinit var telemetryM: TelemetryManager



    override fun onInit() {
        telemetryM = PanelsTelemetry.telemetry
        addComponents(
            BulkReadComponent,
            BindingsComponent,
            PedroComponent(Constants::createFollower),
            SubsystemComponent(Robot.Robot),
            Limelight(),
            Drivetrain(false)
        )
    }


    override fun onUpdate() {
        telemetryM.update()
        telemetry.update()
        PedroComponent.follower.update()
        BindingManager.update()
        val robot = Robot.Robot // Stop me from going crazy

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
                IfElseCommand({ Limelight().headingError > 0.1 || Limelight().headingError < -0.1 },
                    {PedroComponent.follower.heading = Math.toRadians(Limelight().robotHeading)
                        PedroComponent.follower.turnTo(Limelight().robotHeading - Limelight().headingError)} as Command))
            .whenBecomesFalse({PedroComponent.follower.startTeleOpDrive()})
    }

    override fun onStop() {
        BindingManager.reset()
    }

}