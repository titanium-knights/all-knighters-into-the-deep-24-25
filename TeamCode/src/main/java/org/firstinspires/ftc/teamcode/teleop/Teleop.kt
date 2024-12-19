package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive

class Teleop : OpMode() {
    private lateinit var drive : SimpleMecanumDrive;
    override fun init() {
        drive = SimpleMecanumDrive(hardwareMap);
    }

    override fun loop() {
        drive.move(gamepad1.left_stick_x.toDouble(), -gamepad1.left_stick_y.toDouble(), gamepad1.right_stick_x.toDouble());
    }
}