package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class pivot extends SubsystemBase {

  private static TalonSRX pivotMotor;
   //PIDController hi = new PIDController(1, 0, 0);

  public pivot() {

    // public static rev = new DutyCycleEncoder(3);

    // pivotMotor = new TalonSRX(26);

  }

  public void pivotFunction(double test) {

    System.out.println(test);

    //hi.calcaulate(4, 0);
    // pivotMotor.set() how to do the pivot
  }
}
