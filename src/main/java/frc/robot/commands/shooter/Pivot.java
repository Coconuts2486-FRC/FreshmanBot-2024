package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.pivot;

public class Pivot extends Command {

  private final pivot test;
  public static double besty;
  public static final DutyCycleEncoder rev = new DutyCycleEncoder(3);

  public Pivot(pivot test) {
    this.test = test;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    test.pivotFunction(besty);
    besty = rev.getAbsolutePosition() - 0.05;
  }

  @Override
  public void end(boolean interrupted) {}
}
