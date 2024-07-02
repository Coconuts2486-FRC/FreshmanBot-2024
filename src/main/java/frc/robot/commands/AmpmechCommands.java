package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ampmech.elevator;
import frc.robot.subsystems.ampmech.roller;
import java.util.function.BooleanSupplier;

public class AmpmechCommands extends Command {

  private BooleanSupplier stop2;
  private BooleanSupplier stop;
  private BooleanSupplier noteIn;
  private final elevator m_subsystem;
  private final roller m_subsystem2;
  public int step = 0;
  private int outtakeStop = 0;
  private static double timer;

  public AmpmechCommands(
      elevator m_subsystem,
      roller m_subsystem2,
      BooleanSupplier stop,
      BooleanSupplier stop2,
      int outtakeStop) {
    this.stop = stop;
    this.m_subsystem = m_subsystem;
    this.m_subsystem2 = m_subsystem2;
    this.stop2 = stop2;
    this.noteIn = noteIn;
    this.outtakeStop = outtakeStop;
  }

  @Override
  public void initialize() {
    step = 0;
    // outtakeStop = 0;

  }

  @Override
  public void execute() {

    if (step == 0) {
      if (stop.getAsBoolean() == true) {
        elevator.elevatorFunction(0.5);
      } else {
        elevator.elevatorFunction(0);

        step = 1;
        timer = Timer.getFPGATimestamp();
      }
    }
    if (step == 1) {
      if (Timer.getFPGATimestamp() - timer > 2) {
        roller.rollerFunction(0, 0, 0);
        step = 2;
      } else {
        roller.rollerFunction(-0.5, 0, 0);
      }
      System.out.println(timer);
    }
    System.out.println(timer);
    System.out.println(Timer.getFPGATimestamp() - timer);

    if (step == 2) {
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
