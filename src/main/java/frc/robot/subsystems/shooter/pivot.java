package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class pivot extends SubsystemBase {
  private double offset = 0.024;
  private static TalonSRX pivotMotor;
  PIDController pivot = new PIDController(0.5, 0, 0);

  public pivot() {

    pivotMotor = new TalonSRX(27);
  }

  public void pivotFunction(
      double posisitionWanted, double posisitionCurrent, int manuel, double speed) {

    if (manuel == 1) {
      pivotMotor.set(ControlMode.PercentOutput, pivot.calculate(posisitionCurrent, posisitionWanted));
    } else if (manuel == 2) {
      pivotMotor.set(ControlMode.PercentOutput, speed);
    }
    // pivotMotor.set() how to do the pivot
  }
}
