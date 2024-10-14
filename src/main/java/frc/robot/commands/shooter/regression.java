
// Basic Command Setup
// DO NOT CHANGE

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.pivot;


public class regression extends Command {
    pivot pivot;
    private double freezeRegress;

  public regression(pivot pivot) {
    this.pivot = pivot;
  }

  @Override
  public void initialize() {
    freezeRegress = pivot.getSpeakerDistance();
  }

  @Override
  public void execute() {
    double a = 2.9629E-8;
    double b = -0.0000154932;
    double c = 0.00288892;
    double yIntercept = 0.784031;
    double angle;
    if (freezeRegress < -100){
        angle = 0.88;
    } else {
       angle = (a * Math.pow(freezeRegress, 3) + b * Math.pow(freezeRegress, 2) + c * freezeRegress + yIntercept);
    }

    pivot.setPosisition(angle);
  }

  @Override
  public void end(boolean interrupted) {
    pivot.stop();
  }
}