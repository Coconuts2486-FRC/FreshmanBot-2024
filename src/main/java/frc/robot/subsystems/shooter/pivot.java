package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class pivot extends SubsystemBase {

  private static TalonSRX pivotMotor;
  PIDController hi = new PIDController(1, 0, 0);

  public pivot() {

    // pivotMotor = new TalonSRX(26);

  }

  public void pivotFunction(double test, double posisitionWanted, double posisitionCurrent) {

    System.out.println(test);

    // pivotMotor.set(ControlMode.PercentOutput, hi.calculate(posisitionCurrent, posisitionWanted
    // ));
    // pivotMotor.set() how to do the pivot
  }
}
