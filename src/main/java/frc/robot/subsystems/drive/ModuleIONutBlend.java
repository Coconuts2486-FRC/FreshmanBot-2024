// Copyright 2024 FRC 2486
// https://github.com/Coconuts2486-FRC
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

package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkLowLevel.PeriodicFrame;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;
import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import java.util.OptionalDouble;
import java.util.Queue;

/**
 * Module IO implementation for blended TalonFX drive motor controller,
 * SparkMax turn motor controller (NEO or NEO 550), and CANcoder.
 *
 * <p>
 * To calibrate the absolute encoder offsets, point the modules straight
 * (such that forward motion on the drive motor will propel the robot
 * forward) and copy the reported values from the absolute encoders using
 * AdvantageScope. These values are logged under
 * "/Drive/ModuleX/TurnAbsolutePositionRad"
 */

public class ModuleIONutBlend implements ModuleIO {
    // CAN Devices
    private final TalonFX driveTalon;
    private final CANSparkMax turnSparkMax;
    private final CANcoder cancoder;

    // Timestamp Queues
    private final Queue<Double> PhxTimestampQueue;
    private final Queue<Double> SpkTimestampQueue;

    // Drive telemetry information
    private final StatusSignal<Double> drivePosition;
    private final Queue<Double> drivePositionQueue;
    private final StatusSignal<Double> driveVelocity;
    private final StatusSignal<Double> driveAppliedVolts;
    private final StatusSignal<Double> driveCurrent;

    // Steer telemetry information
    private final StatusSignal<Double> turnAbsolutePosition;
    private final RelativeEncoder turnRelativeEncoder;
    private final Queue<Double> turnPositionQueue;

    // Gear ratios for SDS MK4i L2, adjust as necessary
    private final double DRIVE_GEAR_RATIO = (50.0 / 14.0) * (17.0 / 27.0) * (45.0 / 15.0);
    private final double TURN_GEAR_RATIO = 150.0 / 7.0;

    private final boolean isTurnMotorInverted = true;
    private final Rotation2d absoluteEncoderOffset;

    /*
     * NutBlend Module I/O, using Falcon drive and NEO steer motors
     * Based on the ModuleIOTalonFX module, with the SparkMax components
     * added in appropriately.
     */
    public ModuleIONutBlend(int index) {
        switch (index) {
            case 0:
                driveTalon = new TalonFX(0);
                turnSparkMax = new CANSparkMax(2, MotorType.kBrushless);
                cancoder = new CANcoder(2);
                absoluteEncoderOffset = new Rotation2d(0.0); // MUST BE CALIBRATED
                break;
            case 1:
                driveTalon = new TalonFX(3);
                turnSparkMax = new CANSparkMax(4, MotorType.kBrushless);
                cancoder = new CANcoder(5);
                absoluteEncoderOffset = new Rotation2d(0.0); // MUST BE CALIBRATED
                break;
            case 2:
                driveTalon = new TalonFX(6);
                turnSparkMax = new CANSparkMax(6, MotorType.kBrushless);
                cancoder = new CANcoder(8);
                absoluteEncoderOffset = new Rotation2d(0.0); // MUST BE CALIBRATED
                break;
            case 3:
                driveTalon = new TalonFX(9);
                turnSparkMax = new CANSparkMax(8, MotorType.kBrushless);
                cancoder = new CANcoder(11);
                absoluteEncoderOffset = new Rotation2d(0.0); // MUST BE CALIBRATED
                break;
            default:
                throw new RuntimeException("Invalid module index");
        }

        // Drive Configuration
        var driveConfig = new TalonFXConfiguration();
        driveConfig.CurrentLimits.SupplyCurrentLimit = 40.0;
        driveConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        driveTalon.getConfigurator().apply(driveConfig);
        setDriveBrakeMode(true);

        // Steer Configuration
        turnSparkMax.restoreFactoryDefaults();
        turnSparkMax.setCANTimeout(250);
        turnRelativeEncoder = turnSparkMax.getEncoder();
        turnSparkMax.setInverted(isTurnMotorInverted);
        turnSparkMax.setSmartCurrentLimit(30);
        turnSparkMax.enableVoltageCompensation(12.0);
        turnRelativeEncoder.setPosition(0.0);
        turnRelativeEncoder.setMeasurementPeriod(10);
        turnRelativeEncoder.setAverageDepth(2);
        turnSparkMax.setCANTimeout(0);

        // CANCoder Configuration
        cancoder.getConfigurator().apply(new CANcoderConfiguration());

        // Odometry
        PhxTimestampQueue = PhoenixOdometryThread.getInstance().makeTimestampQueue();
        SpkTimestampQueue = SparkMaxOdometryThread.getInstance().makeTimestampQueue();

        drivePosition = driveTalon.getPosition();
        drivePositionQueue = PhoenixOdometryThread.getInstance().registerSignal(driveTalon, driveTalon.getPosition());
        driveVelocity = driveTalon.getVelocity();
        driveAppliedVolts = driveTalon.getMotorVoltage();
        driveCurrent = driveTalon.getSupplyCurrent();

        turnAbsolutePosition = cancoder.getAbsolutePosition();
        turnPositionQueue = SparkMaxOdometryThread.getInstance()
                .registerSignal(
                        () -> {
                            double value = turnRelativeEncoder.getPosition();
                            if (turnSparkMax.getLastError() == REVLibError.kOk) {
                                return OptionalDouble.of(value);
                            } else {
                                return OptionalDouble.empty();
                            }
                        });

        BaseStatusSignal.setUpdateFrequencyForAll(
                Module.ODOMETRY_FREQUENCY, drivePosition);
        BaseStatusSignal.setUpdateFrequencyForAll(
                50.0,
                driveVelocity,
                driveAppliedVolts,
                driveCurrent,
                turnAbsolutePosition);
        driveTalon.optimizeBusUtilization();
        turnSparkMax.setPeriodicFramePeriod(
                PeriodicFrame.kStatus2, (int) (1000.0 / Module.ODOMETRY_FREQUENCY));
        turnSparkMax.burnFlash();
    }

    @Override
    public void updateInputs(ModuleIOInputs inputs) {
        BaseStatusSignal.refreshAll(
                drivePosition,
                driveVelocity,
                driveAppliedVolts,
                driveCurrent,
                turnAbsolutePosition);

        inputs.drivePositionRad = Units.rotationsToRadians(drivePosition.getValueAsDouble()) / DRIVE_GEAR_RATIO;
        inputs.driveVelocityRadPerSec = Units.rotationsToRadians(driveVelocity.getValueAsDouble()) / DRIVE_GEAR_RATIO;
        inputs.driveAppliedVolts = driveAppliedVolts.getValueAsDouble();
        inputs.driveCurrentAmps = new double[] { driveCurrent.getValueAsDouble() };

        inputs.turnAbsolutePosition = Rotation2d.fromRotations(turnAbsolutePosition.getValueAsDouble())
                .minus(absoluteEncoderOffset);
        inputs.turnPosition = Rotation2d.fromRotations(turnRelativeEncoder.getPosition() / TURN_GEAR_RATIO);
        inputs.turnVelocityRadPerSec = Units.rotationsPerMinuteToRadiansPerSecond(turnRelativeEncoder.getVelocity())
                / TURN_GEAR_RATIO;
        inputs.turnAppliedVolts = turnSparkMax.getAppliedOutput() * turnSparkMax.getBusVoltage();
        inputs.turnCurrentAmps = new double[] { turnSparkMax.getOutputCurrent() };

        inputs.odometryTimestamps = PhxTimestampQueue.stream().mapToDouble((Double value) -> value).toArray();
        inputs.odometryTimestamps = SpkTimestampQueue.stream().mapToDouble((Double value) -> value).toArray();

        inputs.odometryDrivePositionsRad = drivePositionQueue.stream()
                .mapToDouble((Double value) -> Units.rotationsToRadians(value) / DRIVE_GEAR_RATIO)
                .toArray();
        inputs.odometryTurnPositions = turnPositionQueue.stream()
                .map((Double value) -> Rotation2d.fromRotations(value / TURN_GEAR_RATIO))
                .toArray(Rotation2d[]::new);
        PhxTimestampQueue.clear();
        SpkTimestampQueue.clear();
        drivePositionQueue.clear();
        turnPositionQueue.clear();
    }



    @Override
    public void setDriveVoltage(double volts) {
        driveTalon.setControl(new VoltageOut(volts));
    }

    @Override
    public void setTurnVoltage(double volts) {
        turnSparkMax.setVoltage(volts);
    }

    @Override
    public void setDriveBrakeMode(boolean enable) {
        var config = new MotorOutputConfigs();
        config.Inverted = InvertedValue.CounterClockwise_Positive;
        config.NeutralMode = enable ? NeutralModeValue.Brake : NeutralModeValue.Coast;
        driveTalon.getConfigurator().apply(config);
    }

    @Override
    public void setTurnBrakeMode(boolean enable) {
        turnSparkMax.setIdleMode(enable ? IdleMode.kBrake : IdleMode.kCoast);
    }

}
