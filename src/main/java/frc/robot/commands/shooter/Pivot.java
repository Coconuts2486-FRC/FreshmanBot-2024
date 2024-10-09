package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.pivot;
import java.util.function.BooleanSupplier;

public class Pivot extends Command {

  private double speed;
  private BooleanSupplier bottom;
  private BooleanSupplier top;
  private double posisition;
  private double offset = 0; // 0.38;
  private int manuel;

  private static double encoder;
  private final pivot pivot;

  public Pivot(
      double posisition,
      pivot pivot,
      BooleanSupplier bottom,
      BooleanSupplier top,
      int manuel,
      double speed) {
    this.pivot = pivot;
    this.bottom = bottom;
    this.top = top;
    this.manuel = manuel;
    this.posisition = posisition;
    this.speed = speed;
  }

  @Override
  public void initialize() {}

  // test
  // test2
  @Override
  public void execute() {

    pivot.periodic();
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
    // if (manuel == 1 && top.getAsBoolean() == true && bottom.getAsBoolean() == false) {
    //   pivot.pivotFunction(posisition, encoder, 1, 0, bottom.getAsBoolean(), top.getAsBoolean());
    // } else

    pivot.pivotFunction(0, manuel, speed, bottom.getAsBoolean(), top.getAsBoolean());
  }

  // SmartDashboard.putBoolean("limit switch test", bottom.getAsBoolean());

  @Override
  public void end(boolean interrupted) {}

  // @Override
  // public boolean isFinished() {
  //   if (manuel == 2 && bottom.getAsBoolean() || top.getAsBoolean()) {
  //     return true;
  //   } else {
  //     return false;
  //   }
  // }
}
