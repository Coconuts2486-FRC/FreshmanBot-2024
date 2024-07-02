package frc.robot.commands;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Command;

public class ClimbCommand extends Command{

    private final CANSparkMax neo = new CANSparkMax(22,MotorType.kBrushless); // Ask oscar about device ID num
    
    
}
