package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ampmech.roller;
import frc.robot.subsystems.intake.Intake2;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

// Suppliers
public class IntakeCommandauto extends Command {
  private final Intake2 m_subsystem;
  private final DoubleSupplier rightTrigger;
  private final DoubleSupplier leftTrigger;
  private final double bumper;

  private final BooleanSupplier limit;

  public IntakeCommandauto(
      roller subsystem2,
      Intake2 subsystem,
      double bumper,
      DoubleSupplier rightTrigger,
      DoubleSupplier leftTrigger,
      BooleanSupplier limit) {
    m_subsystem = subsystem;
    this.bumper = bumper;
    this.rightTrigger = rightTrigger;
    this.leftTrigger = leftTrigger;
    this.limit = limit;
  }

  @Override
  public void initialize() {}

  /*
   * This is checking for when the triggers are pressed
   * And how far they are pressed down equals the speed
   */
  @Override
  public void execute() {
    if (limit.getAsBoolean() == true) {
      Intake2.intakeFunction(0, rightTrigger.getAsDouble(), -leftTrigger.getAsDouble());
      roller.rollerFunction(0, rightTrigger.getAsDouble(), -leftTrigger.getAsDouble());
    } else {
      Intake2.intakeFunction(bumper, rightTrigger.getAsDouble(), -leftTrigger.getAsDouble());
      roller.rollerFunction(bumper, rightTrigger.getAsDouble(), -leftTrigger.getAsDouble());
    }
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    if (limit.getAsBoolean() == true) {
      return true;
    } else {
      return false;
    }
  }
}
