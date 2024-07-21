package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.indexer.indexer;

public class ShooterCommands extends Command {

  private indexer m_subsystem2;
  private static double timer;
  private static double speed;

  public ShooterCommands(indexer ampSubsystem) {
    this.m_subsystem2 = ampSubsystem;
    this.speed = speed;
  }

  @Override
  public void initialize() {
    timer = Timer.getFPGATimestamp();
  }

  @Override
  public void execute() {
    if (Timer.getFPGATimestamp() - timer > 1) {
      indexer.rollerFunction(0, 0, 0);
    } else {
      indexer.rollerFunction(.5, 0, 0);
    }
  }

  @Override
  public void end(boolean interrupted) {
    indexer.rollerFunction(0, 0, 0);
  }
}
