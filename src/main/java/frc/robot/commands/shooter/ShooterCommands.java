package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ampmech.roller;

public class ShooterCommands extends Command {

  private roller m_subsystem2;
  private static double timer;

  public ShooterCommands(roller ampSubsystem) {
    this.m_subsystem2 = ampSubsystem;
  }

  @Override
  public void initialize() {
    timer = Timer.getFPGATimestamp();
  }

  @Override
  public void execute() {
    if (Timer.getFPGATimestamp() - timer > 1) {
      roller.rollerFunction(0, 0, 0);
    } else {
      roller.rollerFunction(.5, 0, 0);
    }
  }

  @Override
  public void end(boolean interrupted) {
    roller.rollerFunction(0, 0, 0);
  }
}
