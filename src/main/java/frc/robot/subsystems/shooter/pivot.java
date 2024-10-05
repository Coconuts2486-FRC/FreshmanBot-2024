package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import frc.robot.Constants.PivotConstants;
import frc.robot.subsystems.limitswitch.LimitSwitch;
import frc.robot.subsystems.limitswitch.LimitSwitchIONC;
import frc.robot.subsystems.limitswitch.LimitSwitchIONO;

public class pivot extends PIDSubsystem {

  // Define the hardware for this subsystem
  private final TalonSRX m_pivotMotor = new TalonSRX(27);
  private final DutyCycleEncoder m_pivotEncoder = new DutyCycleEncoder(3);
  private final LimitSwitch pivotTop = new LimitSwitch(new LimitSwitchIONC(5));
  private final LimitSwitch pivotBottom = new LimitSwitch(new LimitSwitchIONO(4));

  // Internal housekeeping
  private double setPoint = PivotConstants.kPivotSubwoofer;

  /** The pivot subsystem for the robot */
  public pivot() {
    super(new PIDController(PivotConstants.kP, PivotConstants.kI, PivotConstants.kD));
    getController().setTolerance(PivotConstants.kPivotTolerace);
    setSetpoint(setPoint);
  }

  /** Define the setpoint in the instance WITH ERROR CHECKING */
  public Command setpointCommand(double setPoint) {
    if (setPoint > PivotConstants.kPivotMax) {
      setPoint = PivotConstants.kPivotMax;
    } else if (setPoint < PivotConstants.kPivotMin) {
      setPoint = PivotConstants.kPivotMin;
    }
    this.setPoint = setPoint;
    // implicitly require `this`
    return this.runOnce(() -> setSetpoint(this.setPoint));
  }

  /** Get the current setpoint for the subsystem */
  public double getSetpoint() {
    return this.setPoint;
  }

  /** Get the measurement of the subsystem's position, modulo the encoder offset */
  @Override
  public double getMeasurement() {
    return m_pivotEncoder.getAbsolutePosition() - PivotConstants.kPivotOffset;
  }

  /** Run the pivot using PID IFF limit switches are not pressed; otherwise move off */
  @Override
  public void useOutput(double output, double setpoint) {

    // If either limit switch pressed, move away
    if (pivotTop.is_pressed().getAsBoolean()) {
      m_pivotMotor.set(ControlMode.PercentOutput, -0.1);
    } else if (pivotBottom.is_pressed().getAsBoolean()) {
      m_pivotMotor.set(ControlMode.PercentOutput, 0.1);
    } else {
      // Otherwise, use the PID
      m_pivotMotor.set(ControlMode.PercentOutput, output);
    }
  }

  /** Is the subsystem at the setpoint? */
  public boolean atSetpoint() {
    return m_controller.atSetpoint();
  }

  /** Periodically print the pivot encoder position & limit switchs on the SmartDashboard */
  @Override
  public void periodic() {
    super.periodic();
    SmartDashboard.putNumber("Pivot", getMeasurement());
    SmartDashboard.putBoolean("TopPivotLimit", pivotTop.is_pressed().getAsBoolean());
    SmartDashboard.putBoolean("BottomPivotLimit", pivotBottom.is_pressed().getAsBoolean());
  }
}
