package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.Shooter;

public class SpinUpCommands extends Command {

  private Shooter m_subsystem;

  public SpinUpCommands(Shooter shooterSubsystem) {
    this.m_subsystem = shooterSubsystem;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    // The 775 motors on the shooter have a max speed of 6000 RPM at 12V
    this.m_subsystem.runVelocity(3000);
  }

  @Override
  public void end(boolean interrupted) {
    this.m_subsystem.runVolts(0);
  }
}
