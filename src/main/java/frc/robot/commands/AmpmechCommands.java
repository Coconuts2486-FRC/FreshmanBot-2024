package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ampmech.elevator;
import frc.robot.subsystems.ampmech.roller;
import java.util.function.BooleanSupplier;

public class AmpmechCommands extends Command {

  private BooleanSupplier stop2;
  private BooleanSupplier stop;

  private final elevator m_subsystem;
  private final roller m_subsystem2;
  private int step = 1;
  private static double timer;
  private int part = 0;

  public AmpmechCommands(
      elevator m_subsystem,
      roller m_subsystem2,
      BooleanSupplier stop,
      BooleanSupplier stop2,
      int part) {
    this.stop = stop;
    this.part = part;
    this.m_subsystem = m_subsystem;
    this.m_subsystem2 = m_subsystem2;
    this.stop2 = stop2;
  }

  @Override
  public void initialize() {
    step = 1;
    timer = Timer.getFPGATimestamp();
  }

  @Override
  public void execute() {

    // step one = elevator goes up until it hits top limit switch
    // step two = ampmech rollers spin outwards for a set mout of time to shoot note
    // into amp
    // step three = elevator goes down until it hits bottom limit switch

    // limit switches are reversed so when its not pressed it gives off true not false

    if (part == 1) {
      if (stop.getAsBoolean() == true) {
        elevator.elevatorFunction(-0.50);
      } else {
        elevator.elevatorFunction(0);
        step = 4;
      }
    } else {
      elevator.elevatorFunction(0);
    }

    if (part == 2) {
      if (step == 1) {
        if (Timer.getFPGATimestamp() - timer > 0.75 // number of seconds the rollers spin
        ) {
          roller.rollerFunction(0, 0, 0);
          step = 2;
        } else {
          roller.rollerFunction(-0.75, 0, 0);
        }
      }

      if (step == 2) {
        if (stop2.getAsBoolean() == true) {
          elevator.elevatorFunction(0.33);
        } else {
          elevator.elevatorFunction(0);
        }
      }
    }
  }

  @Override
  public boolean isFinished() {
    if (stop2.getAsBoolean() == false && step == 2 || step == 4) {
      return true;
    } else {
      return false;
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
