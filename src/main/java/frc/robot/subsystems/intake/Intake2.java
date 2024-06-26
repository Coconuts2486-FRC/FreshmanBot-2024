package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake2 extends SubsystemBase {

  private static CANSparkMax intakeMotor;

  public Intake2() {

    intakeMotor = new CANSparkMax(13, MotorType.kBrushless);
  }

  public static void intakeFunction(double speed, double speed2) {
    intakeMotor.set(speed + speed2);
  }
}
