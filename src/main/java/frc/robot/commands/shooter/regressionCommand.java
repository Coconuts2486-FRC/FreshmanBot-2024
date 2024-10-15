// Basic Command Setup
// DO NOT CHANGE

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.apriltagvision.AprilTagVision;
import frc.robot.subsystems.shooter.pivot;

public class regressionCommand extends Command {
  pivot pivot;
  AprilTagVision aprilTagVision;
  private double freezeRegress;

  public regressionCommand(pivot pivot, AprilTagVision aprilTagVision) {
    this.pivot = pivot;
    this.aprilTagVision = aprilTagVision;
  }

  @Override
  public void initialize() {
    freezeRegress = aprilTagVision.getSpeakerDistance();
  }

  @Override
  public void execute() {
    double a = 2.9629E-8;
    double b = -0.0000154932;
    double c = 0.00288892;
    double yIntercept = 0.784031;
    double angle;
    if (freezeRegress < -100) {
      angle = 0.88;
    } else {
      angle =
          (a * Math.pow(freezeRegress, 3)
              + b * Math.pow(freezeRegress, 2)
              + c * freezeRegress
              + yIntercept);
    }

    pivot.setPosisition(angle);
  }

  @Override
  public void end(boolean interrupted) {
    pivot.stop();
  }
}
