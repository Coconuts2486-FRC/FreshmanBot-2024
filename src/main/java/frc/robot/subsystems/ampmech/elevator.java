package frc.robot.subsystems.ampmech;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
// Already set up

public class elevator {

  private static CANSparkMax elevatorMotor;

  public elevator() {

    elevatorMotor = new CANSparkMax(20, MotorType.kBrushless); // 5:1 Motor
  }

  public static void elevatorFunction(double speed) {
    elevatorMotor.set(speed);
  }
}
