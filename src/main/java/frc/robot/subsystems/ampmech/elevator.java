package frc.robot.subsystems.ampmech;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

public class elevator {

  private static CANSparkMax elevatorMotor;

  public elevator() {

    elevatorMotor = new CANSparkMax(20, MotorType.kBrushless); // 5:1
  }
}
