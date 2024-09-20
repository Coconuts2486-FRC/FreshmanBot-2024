package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.pivot;
import java.util.function.BooleanSupplier;

public class Pivot extends Command {

  private BooleanSupplier bottom;
  private BooleanSupplier top;
  private double offset = 0.38;
  private int auto;
  private static final DutyCycleEncoder encoderActual = new DutyCycleEncoder(3);
  private static double encoder;
  private final pivot pivot;

  public Pivot(pivot pivot, BooleanSupplier bottom, BooleanSupplier top, int auto) {
    this.pivot = pivot;
    this.bottom = bottom;
    this.top = top;
    this.auto = auto;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    encoder = encoderActual.getAbsolutePosition() - offset;
    SmartDashboard.putNumber("Pivot", encoder);
    // 0.5 = subwoofer shot
    // 0.55 = podium shot

    if (bottom.getAsBoolean() == true && top.getAsBoolean() == true) {
      pivot.pivotFunction(0.5, encoder, auto);
    } else if (bottom.getAsBoolean() == false && top.getAsBoolean() == false) {
      pivot.pivotFunction(encoder, encoder, auto);
    }

    // SmartDashboard.putBoolean("limit switch test", bottom.getAsBoolean());
  }

  @Override
  public void end(boolean interrupted) {}
}
