package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ampmech.roller;
import frc.robot.subsystems.shooter.shooter;


public class ShooterCommands extends Command {

private shooter m_subsystem;
private roller m_subsystem2; 

public void shooterCommand(shooter shooterSubsystem, roller rollerSubsystem){
    m_subsystem = shooterSubsystem;
    m_subsystem2 =rollerSubsystem;
}

    @Override
  public void initialize() {}

  @Override
  public void execute() {}
  
  @Override
  public void end(boolean interrupted) {}

}
