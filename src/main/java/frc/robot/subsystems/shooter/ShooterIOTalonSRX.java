// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.util.Units;

public class ShooterIOTalonSRX implements ShooterIO {

  // The shooter 775 motors each have a 3:1 gearbox on the output shaft
  private static final double GEAR_RATIO = 3.0;

  private final TalonSRX lower = new TalonSRX(25);
  private final TalonSRX upper = new TalonSRX(26);

  private final double lowerPosition = lower.getSelectedSensorPosition();
  private final StatusSignal<Double> lowerVelocity = lower.getVelocity();
  private final StatusSignal<Double> lowerAppliedVolts = lower.getMotorVoltage();
  private final StatusSignal<Double> lowerCurrent = lower.getSupplyCurrent();
  private final StatusSignal<Double> upperCurrent = upper.getSupplyCurrent();

  public ShooterIOTalonSRX() {
    var config = new TalonSRXConfiguration();
    config.CurrentLimits.SupplyCurrentLimit = 30.0;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    lower.getConfigurator().apply(config);
    upper.getConfigurator().apply(config);
    upper.setControl(new Follower(lower.getDeviceID(), false));

    BaseStatusSignal.setUpdateFrequencyForAll(
        50.0, lowerPosition, lowerVelocity, lowerAppliedVolts, lowerCurrent, upperCurrent);
    lower.optimizeBusUtilization();
    upper.optimizeBusUtilization();
  }

  @Override
  public void updateInputs(ShooterIOInputs inputs) {
    BaseStatusSignal.refreshAll(
        lowerPosition, lowerVelocity, lowerAppliedVolts, lowerCurrent, upperCurrent);
    inputs.positionRad = Units.rotationsToRadians(lowerPosition.getValueAsDouble()) / GEAR_RATIO;
    inputs.velocityRadPerSec =
        Units.rotationsToRadians(lowerVelocity.getValueAsDouble()) / GEAR_RATIO;
    inputs.appliedVolts = lowerAppliedVolts.getValueAsDouble();
    inputs.currentAmps =
        new double[] {lowerCurrent.getValueAsDouble(), upperCurrent.getValueAsDouble()};
  }

  @Override
  public void setVoltage(double volts) {
    lower.setControl(new VoltageOut(volts));
  }

  @Override
  public void setVelocity(double velocityRadPerSec, double ffVolts) {
    lower.setControl(
        new VelocityVoltage(
            Units.radiansToRotations(velocityRadPerSec),
            0.0,
            true,
            ffVolts,
            0,
            false,
            false,
            false));
  }

  @Override
  public void stop() {
    lower.stopMotor();
  }

  @Override
  public void configurePID(double kP, double kI, double kD) {
    var config = new Slot0Configs();
    config.kP = kP;
    config.kI = kI;
    config.kD = kD;
    lower.getConfigurator().apply(config);
  }
}

// package frc.robot.subsystems.shooter;

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;

// public class shooter extends SubsystemBase {

//   private static TalonSRX shooterMotor1;
//   private static TalonSRX shooterMotor2;

//   public shooter() {

//     shooterMotor1 = new TalonSRX(25);
//     shooterMotor2 = new TalonSRX(26);
//   }

//   public static void shooterFunction(double speed) {
//     shooterMotor1.set(ControlMode.PercentOutput, speed);
//     shooterMotor2.set(ControlMode.PercentOutput, speed);
//   }
// }
