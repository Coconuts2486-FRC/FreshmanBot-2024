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

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import edu.wpi.first.math.util.Units;

public class ShooterIOTalonSRX implements ShooterIO {

  // The shooter 775 motors each have a 3:1 gearbox on the output shaft
  private static final double GEAR_RATIO = 3.0;
  private static final double COMP_VOLTAGE = 11.0;

  private final TalonSRX lowerShot = new TalonSRX(25);
  private final TalonSRX upperShot = new TalonSRX(26);

  public ShooterIOTalonSRX() {

    // Set up the configurations for the upper & lower shooter motors
    var config = new TalonSRXConfiguration();
    config.continuousCurrentLimit = 30; // Current limit!
    lowerShot.configAllSettings(config);
    upperShot.configAllSettings(config);
    lowerShot.setNeutralMode(NeutralMode.Coast);
    upperShot.setNeutralMode(NeutralMode.Coast);

    // More motor configurations -- voltage ramps, compensation
    lowerShot.configOpenloopRamp(
        0.5); // 0.5 seconds from neutral to full output (during open-loop control)
    lowerShot.configClosedloopRamp(0); // 0 disables ramping (during closed-loop control)
    lowerShot.configVoltageCompSaturation(
        COMP_VOLTAGE); // "full output" scales to 11 Volts for all control modes
    lowerShot.enableVoltageCompensation(true); // turn on/off feature
    upperShot.follow(lowerShot); // Lower to simply follow upper

    // BaseStatusSignal.setUpdateFrequencyForAll(
    // 50.0, lowerPosition, lowerVelocity, lowerAppliedVolts, lowerCurrent,
    // upperCurrent);
    // lower.optimizeBusUtilization();
    // upper.optimizeBusUtilization();
  }

  @Override
  public void updateInputs(ShooterIOInputs inputs) {
    // BaseStatusSignal.refreshAll(
    // lowerPosition, lowerVelocity, lowerAppliedVolts, lowerCurrent, upperCurrent);
    inputs.positionRad =
        Units.rotationsToRadians(lowerShot.getSelectedSensorPosition()) / GEAR_RATIO;
    inputs.velocityRadPerSec =
        Units.rotationsToRadians(lowerShot.getSelectedSensorVelocity()) / GEAR_RATIO;
    inputs.appliedVolts = lowerShot.getMotorOutputVoltage();
    inputs.currentAmps = new double[] {lowerShot.getSupplyCurrent(), upperShot.getSupplyCurrent()};
  }

  /**
   * Run the shooter in open-loop voltage mode
   *
   * @param volts The open-loop voltage at which to run the motor
   */
  @Override
  public void setVoltage(double volts) {
    lowerShot.set(ControlMode.PercentOutput, Math.max(volts / COMP_VOLTAGE, 1.0));
  }

  /**
   * Run the shooter in closed-loop velocity mode
   *
   * @param velocityRadPerSec The requested closed-loop velocity of the shooter motor in radians/sec
   * @param ffVolts The modeled feed-forward voltage predicted to achieve this velocity
   */
  @Override
  public void setVelocity(double velocityRadPerSec, double ffVolts) {

    // Convert the input into something Phoenix5 understands
    double vRotPerSec = Units.radiansToRotations(velocityRadPerSec);
    // 2048 ticks per rotation, 10 "time units" (100 ms) per second
    double vPhx5 = vRotPerSec * 2048. / 10.;
    // Set the velocity
    // lowerShot.config_kF(0, 0.05, 50);
    lowerShot.set(ControlMode.Velocity, vPhx5);
  }

  /** Stop the motor */
  @Override
  public void stop() {
    lowerShot.set(ControlMode.PercentOutput, 0);
  }

  /**
   * Configure the shooter's PID
   *
   * @param kP Proportional gain
   * @param kI Integral gain
   * @param kD Derivative gain
   */
  @Override
  public void configurePID(double kP, double kI, double kD) {
    lowerShot.config_kP(0, kP, 50);
    lowerShot.config_kI(0, kI, 50);
    lowerShot.config_kD(0, kD, 50);
  }
}
