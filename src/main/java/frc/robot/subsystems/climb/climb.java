package frc.robot.subsystems.climb;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class climb extends SubsystemBase {

  private final CANSparkMax climbNeo = new CANSparkMax(23, MotorType.kBrushless);

  public climb() {
    // climbNeo = new CANSparkMax(35, MotorType.kBrushless);
    // climbNeo.setIdleMode(IdleMode.kBrake);
  }

  public void climbSet(double speed) {
    climbNeo.set(speed);
  }
}
