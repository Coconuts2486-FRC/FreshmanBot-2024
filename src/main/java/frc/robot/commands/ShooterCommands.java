package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ampmech.roller;
import frc.robot.subsystems.shooter.shooter;

public class ShooterCommands extends Command {

  private shooter m_subsystem;
  private roller m_subsystem2;
  private static double timer;
  private static int step;
 

  public ShooterCommands(shooter shooterSubsystem, roller ampSubsystem, int step) {
    this.m_subsystem = shooterSubsystem;
    this.m_subsystem2 = ampSubsystem;
    this.step = step;
  }

  @Override
  public void initialize() {
    timer = Timer.getFPGATimestamp();
    step = 0;
 
  }

  @Override
  public void execute() {
    if(step == 1){
      shooter.shooterFunction(0.5);
    } else if(step == 0){
      if(Timer.getFPGATimestamp() - timer > 2){
        roller.rollerFunction(0.5, 0.0, 0.0);
      } else{
        roller.rollerFunction(0.5, 0.0, 0.0);
      }
    }
    }
    
  

  @Override
  public void end(boolean interrupted) {}
}
