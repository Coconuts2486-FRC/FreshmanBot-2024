package frc.robot.subsystems.indexer;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
// Already set up

public class indexer {

  private static CANSparkMax rollerMotor;

  public indexer() {

    rollerMotor = new CANSparkMax(21, MotorType.kBrushless); // 3:1 Motor
  }

  public static void rollerFunction(double speed, double speed2, double speed3) {
    rollerMotor.set(speed + speed2 + speed3);
  }
}
