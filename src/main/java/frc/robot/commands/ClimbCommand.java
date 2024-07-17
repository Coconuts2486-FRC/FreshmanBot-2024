// package frc.robot.commands;

// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.subsystems.climb.climb;
// import java.util.function.DoubleSupplier;

// public class ClimbCommand extends Command {

//   private final climb climbRun;
//   private final DoubleSupplier rightStick;

//   public ClimbCommand(climb climbRunB, DoubleSupplier rightStick) {
//     this.climbRun = climbRunB;
//     this.rightStick = rightStick;
//   }

//   @Override
//   public void initialize() {}

//   @Override
//   public void execute() {
//     climbRun.climbSet(rightStick.getAsDouble());
//   }

//   @Override
//   public void end(boolean interrupted) {}
// }
