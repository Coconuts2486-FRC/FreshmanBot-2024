package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ampmech.roller;
import frc.robot.subsystems.shooter.shooter;

public class QuickShoot extends Command {

  private roller m_subsystem2;
  private static double timer;
  private shooter shooterSubsystem;

  public QuickShoot(roller ampSubsystem, shooter shooterSubsystem) {
    this.m_subsystem2 = ampSubsystem;
    this.shooterSubsystem = shooterSubsystem;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {

    if (shooterSubsystem.getSpeed() < 21000) {
      shooterSubsystem.shooterFunction(0.72);
      timer = Timer.getFPGATimestamp();
    } else if (Timer.getFPGATimestamp() - timer > 1.5) {
      roller.rollerFunction(0, 0, 0);
    } else {
      roller.rollerFunction(0.75, 0, 0);
    }
  }

  @Override
  public void end(boolean interrupted) {
    roller.rollerFunction(0, 0, 0);
    shooterSubsystem.shooterFunction(0);
  }
}
