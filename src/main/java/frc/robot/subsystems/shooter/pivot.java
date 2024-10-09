package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class pivot extends SubsystemBase {
  private double offset = 0.024;
  private static TalonSRX pivotMotor;
  PIDController pivotPID = new PIDController(-0.5, 0, 0);
  private static final DutyCycleEncoder encoderActual = new DutyCycleEncoder(3);
  double encoder;
  // private doublesuppier encoder = encoderActual;

  public pivot() {

    pivotMotor = new TalonSRX(27);
  }

  // double encoder = encoderActual.getAbsolutePosition();

  public void pivotFunction(
      double posisitionWanted, int manuel, double speed, boolean move, boolean move2) {

    if (move == false && speed < 0) {

      pivotMotor.set(ControlMode.PercentOutput, speed);

    } else if (move2 == true && speed > 0) {

      pivotMotor.set(ControlMode.PercentOutput, speed);

    } else if (manuel == 2 && move == false && move2 == true) {
      pivotMotor.set(ControlMode.PercentOutput, pivotPID.calculate(encoder, posisitionWanted));
    } else {
      pivotMotor.set(ControlMode.PercentOutput, 0);
    }
  }

  public void periodic() {
    encoder = encoderActual.getAbsolutePosition();
    SmartDashboard.putNumber("Pivot", encoder);
  }
}
