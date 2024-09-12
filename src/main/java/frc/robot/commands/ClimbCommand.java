package frc.robot.commands;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.climb.climb;
import frc.robot.subsystems.shooter.pivot;
import java.util.function.DoubleSupplier;

public class ClimbCommand extends Command {

  private final climb climbRun;
  private final pivot test;
  private final DoubleSupplier rightStick;
  public static double besty;
  public static final DutyCycleEncoder rev = new DutyCycleEncoder(3);

  public ClimbCommand(climb climbRunB, DoubleSupplier rightStick, pivot test) {
    this.climbRun = climbRunB;
    this.test = test;
    this.rightStick = rightStick;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {

    climbRun.climbSet(rightStick.getAsDouble());
    test.pivotFunction(besty);
    besty = rev.get();
  }

  @Override
  public void end(boolean interrupted) {
    climbRun.climbSet(0);
  }
}
