package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.pivot;
import java.util.function.BooleanSupplier;

public class Pivot extends Command {

  private BooleanSupplier bottom;
  private BooleanSupplier top;
  private double posisition;
  private double offset = 0; // 0.38;
  private int manuel;
  private static final DutyCycleEncoder encoderActual = new DutyCycleEncoder(3);
  private static double encoder;
  private final pivot pivot;

  public Pivot(double posisition, pivot pivot, BooleanSupplier bottom, BooleanSupplier top, int manuel) {
    this.pivot = pivot;
    this.bottom = bottom;
    this.top = top;
    this.manuel = manuel;
    this.posisition = posisition;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    encoder = encoderActual.getAbsolutePosition();
    SmartDashboard.putNumber("Pivot", encoder);
    SmartDashboard.putBoolean("TopPivot", top.getAsBoolean());
    SmartDashboard.putBoolean("BottomPivot", bottom.getAsBoolean());
    // top pivot value is opossite
    // 0.88 = subwoofer shot
    // 0.93 = podium shot

    // if (bottom.getAsBoolean() == false && top.getAsBoolean() == false) {
    //   pivot.pivotFunction(0.5, encoder, auto);
    // } else if (bottom.getAsBoolean() == true && top.getAsBoolean() == true) {
    //   pivot.pivotFunction(encoder, encoder, auto);
    // }
    if (manuel == 1 && top.getAsBoolean() == true && bottom.getAsBoolean() == false && encoder > 0.87 && encoder < 0.99)  {
      pivot.pivotFunction(posisition, encoder, 1, 0);
    } else if (manuel == 2 && top.getAsBoolean() == true && encoder > 0.87) {
      pivot.pivotFunction(0, 0, 2, 0.25);
    } else if (manuel == 3 && bottom.getAsBoolean() == false && encoder < 0.99) {
      pivot.pivotFunction(0, 0, 2, -0.25); 
    } else if (manuel == 0) {
      pivot.pivotFunction(0, 0, 2, 0.0);
    } else{
      pivot.pivotFunction(0, 0, 2, 0.0);
      manuel = 0;
    }
  }

  // SmartDashboard.putBoolean("limit switch test", bottom.getAsBoolean());

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    if (top.getAsBoolean() == false && manuel == 1) {
      return true;
    } else if (bottom.getAsBoolean() == true && manuel == 1) {
      return true;
    } else if (manuel == 0) {
      return true;
    }else {
      return false;
    }
  }
}
