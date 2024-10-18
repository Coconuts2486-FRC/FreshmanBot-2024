// Basic Command Setup
// DO NOT CHANGE

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.apriltagvision.AprilTagVision;
import frc.robot.subsystems.drive.Drive;

public class Target extends Command {
  AprilTagVision aprilTagVision;
  Drive drive;
  private double freeze;
  private double sight;
  private static final PIDController rotatePID = new PIDController(.2, 0, 0.01);

  public Target(Drive drive, AprilTagVision aprilTagVision) {
    this.aprilTagVision = aprilTagVision;
    this.drive = drive;
    freeze = 0;
    sight = 0;
    addRequirements(drive);
  }

  @Override
  public void initialize() {
    rotatePID.enableContinuousInput(0, 360);
    int redOffset = (DriverStation.getAlliance().get() == Alliance.Red) ? -1 : 1;
    freeze = aprilTagVision.getSpeakerYaw().getDegrees() + 175;
    sight = aprilTagVision.getSpeakerYaw().getRotations();
  }

  @Override
  public void execute() {
    // if(Math.abs(sight) == )
    // System.out.println(Math.abs(sight));
    System.out.println(Math.abs(sight));
    if (Math.abs(sight) > 999) {
      boolean isFlipped =
          DriverStation.getAlliance().isPresent()
              && DriverStation.getAlliance().get() == Alliance.Red;

      drive.runVelocity(
          ChassisSpeeds.fromFieldRelativeSpeeds(
              0,
              0,
              0,
              isFlipped ? drive.getRotation().plus(new Rotation2d(Math.PI)) : drive.getRotation()));
    } else {
      boolean isFlipped =
          DriverStation.getAlliance().isPresent()
              && DriverStation.getAlliance().get() == Alliance.Red;

      drive.runVelocity(
          ChassisSpeeds.fromFieldRelativeSpeeds(
              0,
              0,
              rotatePID.calculate(drive.gyroAngles().getDegrees() - freeze),
              isFlipped ? drive.getRotation().plus(new Rotation2d(Math.PI)) : drive.getRotation()));
    }
  }

  @Override
  public void end(boolean interrupted) {}
}
