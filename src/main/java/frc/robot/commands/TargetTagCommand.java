// Copyright (c) 2024 FRC 2486
// http://github.com/Coconuts2486-FRC
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.drive.Drive;
import java.util.function.DoubleSupplier;

/** A command that will turn the robot to the specified angle. */
public class TargetTagCommand extends PIDCommand {
  /**
   * Turns to robot to the specified angle.
   *
   * @param targetAngleDegrees The angle to turn to
   * @param drive The drive subsystem to use
   */
  public TargetTagCommand(
      Drive drive, DoubleSupplier xSupplier, DoubleSupplier ySupplier, double targetAngleDegrees) {
    super(
        new PIDController(DriveConstants.kTurnP, DriveConstants.kTurnI, DriveConstants.kTurnD),
        // Close loop on heading
        drive::getHeading,
        // Set reference to target
        targetAngleDegrees,
        // Pipe output to turn robot
        output ->
            drive.runVelocity(
                ChassisSpeeds.fromFieldRelativeSpeeds(
                    // vxMetersPerSecond
                    new Pose2d(
                                new Translation2d(),
                                new Rotation2d(xSupplier.getAsDouble(), ySupplier.getAsDouble()))
                            .transformBy(
                                new Transform2d(
                                    MathUtil.applyDeadband(
                                        Math.hypot(
                                            xSupplier.getAsDouble(), ySupplier.getAsDouble()),
                                        DriveConstants.DEADBAND),
                                    0.0,
                                    new Rotation2d()))
                            .getTranslation()
                            .getX()
                        * drive.getMaxLinearSpeedMetersPerSec(),
                    // vyMetersPerSecond
                    new Pose2d(
                                new Translation2d(),
                                new Rotation2d(xSupplier.getAsDouble(), ySupplier.getAsDouble()))
                            .transformBy(
                                new Transform2d(
                                    MathUtil.applyDeadband(
                                        Math.hypot(
                                            xSupplier.getAsDouble(), ySupplier.getAsDouble()),
                                        DriveConstants.DEADBAND),
                                    0.0,
                                    new Rotation2d()))
                            .getTranslation()
                            .getY()
                        * drive.getMaxLinearSpeedMetersPerSec(),
                    // omegaRadiansPerSecond
                    output,
                    // robotAngle
                    DriverStation.getAlliance().isPresent()
                            && DriverStation.getAlliance().get() == Alliance.Red
                        ? drive.getRotation().plus(new Rotation2d(Math.PI))
                        : drive.getRotation())),
        // Require the drive
        drive);

    // Set the controller to be continuous (because it is an angle controller)
    getController().enableContinuousInput(-180, 180);
    // Set the controller tolerance - the delta tolerance ensures the robot is stationary at the
    // setpoint before it is considered as having reached the reference
    getController()
        .setTolerance(DriveConstants.kTurnToleranceDeg, DriveConstants.kTurnRateToleranceDegPerS);
  }

  @Override
  public boolean isFinished() {
    // End when the controller is at the reference.
    return getController().atSetpoint();
  }
}
