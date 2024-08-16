package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class shooter extends SubsystemBase {

  private static TalonSRX shooterMotor1;
  private static TalonSRX shooterMotor2;

  public shooter() {

    shooterMotor1 = new TalonSRX(25);
    shooterMotor2 = new TalonSRX(26);

    shooterMotor1.configOpenloopRamp(.1);
    shooterMotor2.configOpenloopRamp(.1);
  }

  public static void shooterFunction(double speed) {
    shooterMotor1.set(ControlMode.PercentOutput, speed);
    shooterMotor2.set(ControlMode.PercentOutput, speed);
  }
}
