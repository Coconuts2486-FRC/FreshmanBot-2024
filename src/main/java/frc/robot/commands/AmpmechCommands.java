package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ampmech.elevator;
import java.util.function.BooleanSupplier;

public class AmpmechCommands extends Command {

  private BooleanSupplier stop;
  private final elevator m_subsystem;
  private double speed;

  public AmpmechCommands(elevator m_subsystem, BooleanSupplier stop, double speed) {
    this.stop = stop;
    this.m_subsystem = m_subsystem;
    this.speed = speed;
  }

  @Override
  public void execute() {
    if (stop.getAsBoolean() == false) {
      elevator.elevatorFunction(speed);
    } else {
      elevator.elevatorFunction(0);
    }
  }

  @Override
  public String toString() {
    return "AmpmechCommands []";
  }

  @Override
  public void end(boolean interrupted) {
    elevator.elevatorFunction(0);
  }
}
