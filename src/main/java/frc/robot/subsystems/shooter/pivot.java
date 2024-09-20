package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class pivot extends SubsystemBase {
  private double offset = 0.024;
  private static TalonSRX pivotMotor;
  PIDController hi = new PIDController(1, 0, 0.001);

  public pivot() {

    pivotMotor = new TalonSRX(27);
  }

  public void pivotFunction(double posisitionWanted, double posisitionCurrent, int auto) {

    if (auto == 1) {
      pivotMotor.set(ControlMode.PercentOutput, hi.calculate(posisitionCurrent, posisitionWanted));
    } else if (auto == 2) {
      pivotMotor.set(ControlMode.PercentOutput, 0.1);
    } else if (auto == 3) {
      pivotMotor.set(ControlMode.PercentOutput, -0.1);
    } else if (auto == 0) {
      pivotMotor.set(ControlMode.PercentOutput, 0);
    }
    // pivotMotor.set() how to do the pivot
  }
}
