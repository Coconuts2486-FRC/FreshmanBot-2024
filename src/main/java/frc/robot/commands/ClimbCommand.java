package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.climb.Climb;
import java.util.function.DoubleSupplier;

public class ClimbCommand extends Command {

  private final Climb climbRun;
  private final DoubleSupplier rightStick;

  public ClimbCommand(Climb climbRunB, DoubleSupplier rightStick) {
    this.climbRun = climbRunB;
    this.rightStick = rightStick;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    climbRun.climbSet(rightStick.getAsDouble());
  }

  @Override
  public void end(boolean interrupted) {}
}
