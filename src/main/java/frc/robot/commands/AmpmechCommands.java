package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.elevator;
import frc.robot.subsystems.indexer.indexer;
import java.util.function.BooleanSupplier;

public class AmpmechCommands extends Command {

  private BooleanSupplier stop2;
  private BooleanSupplier stop;

  private final elevator m_subsystem;
  private final indexer m_subsystem2;
  private int step = 1;
  private static double timer;
  private double test = 0;

  public AmpmechCommands(
      elevator m_subsystem,
      indexer m_subsystem2,
      BooleanSupplier stop,
      BooleanSupplier stop2,
      double test) {
    this.stop = stop;
    this.test = test;
    this.m_subsystem = m_subsystem;
    this.m_subsystem2 = m_subsystem2;
    this.stop2 = stop2;
  }

  @Override
  public void initialize() {
    step = 1;
  }

  @Override
  public void execute() {

    elevator.elevatorFunction(test);

    // step one = elevator goes up until it hits top limit switch
    // step two = ampmech rollers spin outwards for a set mout of time to shoot note
    // into amp
    // step three = elevator goes down until it hits bottom limit switch

    // limit switches are reversed so when its not pressed it gives off true not false

    if (step == 1) {
      if (stop.getAsBoolean() == true) {
        elevator.elevatorFunction(-0.50);
      } else {
        elevator.elevatorFunction(0);

        step = 2;
        timer = Timer.getFPGATimestamp();
      }
    }
    if (step == 2) {
      if (Timer.getFPGATimestamp() - timer > 0.5 // number of seconds the rollers spin
      ) {
        indexer.rollerFunction(0, 0, 0);
        step = 3;
      } else {
        indexer.rollerFunction(-0.75, 0, 0);
      }
    }

    if (step == 3) {
      if (stop2.getAsBoolean() == true) {
        elevator.elevatorFunction(0.33);
      } else {
        elevator.elevatorFunction(0);
      }
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
