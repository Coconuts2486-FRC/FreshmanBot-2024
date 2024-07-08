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
  public int step = 1;
  private static double timer;

  public AmpmechCommands(
      elevator m_subsystem,
      roller m_subsystem2,
      BooleanSupplier stop,
      BooleanSupplier stop2) {
    this.stop = stop;
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

    // step one = elevator goes up until it hits top limit switch
    // step two = ampmech rollers spin outwards for a set mout of time to shoot note
    // into amp
    // step three = elevator goes down until it hits bottom limit switch

    if (step == 1) {
      if (stop.getAsBoolean() == true) {
        elevator.elevatorFunction(0.5);
      } else {
        elevator.elevatorFunction(0);

        step = 2;
        timer = Timer.getFPGATimestamp();
      }
    }
    if (step == 2) {
      if (Timer.getFPGATimestamp() - timer > 2 // number of seconds the rollers spin
      ) {
        roller.rollerFunction(0, 0, 0);
        step = 3;
      } else {
        roller.rollerFunction(-0.5, 0, 0);
      }
      
    }


    if (step == 3) {
      if (stop2.getAsBoolean() == true) {
        elevator.elevatorFunction(-0.5);
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
