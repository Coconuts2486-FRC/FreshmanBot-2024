package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ampmech.roller;

public class ShooterCommands extends Command {

  private roller m_subsystem2;
  private static double timer;
  private static double speed;

  public ShooterCommands(roller ampSubsystem, double speed) {
    this.m_subsystem2 = ampSubsystem;

    this.speed = speed;
  }

  @Override
  public void initialize() {
    timer = Timer.getFPGATimestamp();
  }

  @Override
  public void execute() {

    if (Timer.getFPGATimestamp() - timer > 1.5) {
      roller.rollerFunction(0, 0, 0);
    } else {
      roller.rollerFunction(speed, 0, 0);
    }
  }

  @Override
  public void end(boolean interrupted) {
    roller.rollerFunction(0, 0, 0);
  }
}
