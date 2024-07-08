package frc.robot.subsystems.climb;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Climb Class

public class Climb extends SubsystemBase {

  private final CANSparkMax climbNeo = new CANSparkMax(107, MotorType.kBrushless); 
  // Device ID is needs a change
  public Climb() {
    climbNeo.setIdleMode(IdleMode.kBrake);
  }

  public void climbSet(double speed) {
    climbNeo.set(speed);
  }
}
