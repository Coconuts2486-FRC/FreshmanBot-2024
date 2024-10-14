package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.pivot;

public class setpoint extends Command {
  pivot pivot;
  double angle;

  public setpoint(pivot pivot, double angle) {
    this.pivot = pivot;
    this.angle = angle;
    addRequirements(pivot);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    // pivot.periodic();
    pivot.setPosisition(angle);
  }

  @Override
  public void end(boolean interrupted) {
    pivot.stop();
  }
}
