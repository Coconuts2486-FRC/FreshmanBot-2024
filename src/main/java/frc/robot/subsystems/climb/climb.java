package frc.robot.subsystems.climb;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class climb extends SubsystemBase{

    private final CANSparkMax climbNeo = new CANSparkMax(107,MotorType.kBrushless); // Ask oscar about device ID num
    
    public climb(){
        
    }
    
}
