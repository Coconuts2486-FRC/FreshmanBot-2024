// package frc.robot.commands;

// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.subsystems.ampmech.roller;
// import frc.robot.subsystems.shooter.shooter;

// public class ShooterCommands extends Command {

//   private shooter m_subsystem;
//   private roller m_subsystem2;
//   private static double timer;

//   public ShooterCommands(shooter shooterSubsystem, roller ampSubsystem) {
//     m_subsystem = shooterSubsystem;
//     m_subsystem2 = ampSubsystem;
//   }

//   @Override
//   public void initialize() {
//     timer = Timer.getFPGATimestamp();
//   }

//   @Override
//   public void execute() {

//     if (Timer.getFPGATimestamp() - timer > 2) {
//       shooter.shooterFunction(0.5);
//       roller.rollerFunction(0.5, 0, 0);
//     } else {
//       shooter.shooterFunction(0.5);
//     }
//   }

//   @Override
//   public void end(boolean interrupted) {}
// }
