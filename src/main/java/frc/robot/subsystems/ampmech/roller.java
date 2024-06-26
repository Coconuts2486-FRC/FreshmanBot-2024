package frc.robot.subsystems.ampmech;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

public class roller {

  private static CANSparkMax rollerMotor;

  public roller() {

    rollerMotor = new CANSparkMax(21, MotorType.kBrushless); // 3:1
  }

  public static void rollerFunction(double speed, double speed2, double speed3) {
    rollerMotor.set(speed + speed2 + speed3);
  }
}
