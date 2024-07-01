package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ampmech.elevator;
import java.util.function.BooleanSupplier;

public class AmpmechCommands2 extends Command {

  private BooleanSupplier stop2;
  private BooleanSupplier stop;
  private final elevator m_subsystem;
  private double speed;

  public AmpmechCommands2(
      elevator m_subsystem, BooleanSupplier stop, BooleanSupplier stop2, double speed) {
    this.stop = stop;
    this.m_subsystem = m_subsystem;
    this.speed = speed;
    this.stop2 = stop2;
  }

  @Override
  public void execute() {

    if (RobotContainer.ampmechStep == 1) {
      if (stop2.getAsBoolean() == false) {
        elevator.elevatorFunction(-0.5);
      } else {
        elevator.elevatorFunction(0);
        RobotContainer.ampmechStep = 0;
      }
    }
  }
}
