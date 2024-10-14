package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.shooter;

public class SpinUpCommands extends Command {

  private shooter shooterSubsystem;

  public SpinUpCommands(shooter shooterSubsystem) {
    this.shooterSubsystem = shooterSubsystem;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    shooterSubsystem.shooterFunction(0.72);
  }

  @Override
  public void end(boolean interrupted) {
    shooterSubsystem.shooterFunction(0);
  }
}
