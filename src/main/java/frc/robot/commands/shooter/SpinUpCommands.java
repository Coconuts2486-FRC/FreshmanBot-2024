package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.shooter;

public class SpinUpCommands extends Command {

  private shooter m_subsystem;

  public SpinUpCommands(shooter shooterSubsystem) {
    this.m_subsystem = shooterSubsystem;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    shooter.shooterFunction(0.75);
  }

  @Override
  public void end(boolean interrupted) {
    shooter.shooterFunction(0);
  }
}
