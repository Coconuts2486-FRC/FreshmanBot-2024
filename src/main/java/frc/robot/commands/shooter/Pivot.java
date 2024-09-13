package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.pivot;

public class Pivot extends Command {

  private static final DutyCycleEncoder encoderActual = new DutyCycleEncoder(3);
  double encoder = encoderActual.getAbsolutePosition();
  private final pivot pivot;

  public Pivot(pivot pivot) {
    this.pivot = pivot;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    pivot.pivotFunction(encoder, 0, 0);
    SmartDashboard.putNumber("Pivot", encoder);
  }

  @Override
  public void end(
    boolean interrupted) {}
}
