package org.firstinspires.ftc.teamcode.robot

import dev.nextftc.core.commands.groups.ParallelGroup
import dev.nextftc.core.subsystems.SubsystemGroup
import org.firstinspires.ftc.teamcode.robot.Intake
import org.firstinspires.ftc.teamcode.robot.Outtake

class Robot {
    object Robot : SubsystemGroup(
        Outtake.Outtake,
        Intake.Intake
    ){
        // So that I don't lose my mind...
        private val intake = Intake.Intake
        private val outtake = Outtake.Outtake

        val allStop = ParallelGroup(intake.zeroServo, outtake.zero).requires(this)
        val intakeStop = intake.zeroServo.requires(intake)
        val outtakeStop = outtake.zero.requires(outtake)
        val intakeRunning = intake.forwardServo.requires(intake)
        val intakeRunningOuttakeReverse = ParallelGroup(intake.forwardServo, outtake.reverse).requires(intake,outtake)
        val outtakeRunningShort = outtake.shortV.requires(outtake)
        val outtakeRunningLong = outtake.longV.requires(outtake)
        val intakeRunningOuttakeRunningShort = ParallelGroup(intake.forwardServo, outtake.shortV).requires(intake,outtake)
        val intakeRunningOuttakeRunningLong = ParallelGroup(intake.forwardServo, outtake.longV).requires(intake,outtake)
        val intakeReverseOuttakeReverse = ParallelGroup(intake.reverseServo, outtake.reverse).requires(intake,outtake)
    }

}