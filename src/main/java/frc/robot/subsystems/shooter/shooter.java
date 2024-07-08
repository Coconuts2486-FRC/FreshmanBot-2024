package frc.robot.subsystems.shooter;

import javax.swing.text.Position;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class shooter extends SubsystemBase{

private static CANSparkMax shooterMotor1;
private static CANSparkMax shooterMotor2;

public shooter(){

shooterMotor1 = new CANSparkMax(67, MotorType.kBrushless);
shooterMotor2 = new CANSparkMax(66, MotorType.kBrushless);

}

public static void shooterFunction(double speed1, double speed2){
    shooterMotor1.set(speed1);
    shooterMotor2.set(speed2);
}
}