// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ampmech.roller;
import frc.robot.subsystems.intake.Intake2;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class IntakeCommand extends Command {
  private final Intake2 m_subsystem;
  private final DoubleSupplier rightTrigger;
  private final DoubleSupplier leftTrigger;
  private final double bumper;

  private final BooleanSupplier limit;

  public IntakeCommand(
      roller subsystem2,
      Intake2 subsystem,
      double bumper,
      DoubleSupplier rightTrigger,
      DoubleSupplier leftTrigger,
      BooleanSupplier limit) {
    m_subsystem = subsystem;
    this.bumper = bumper;
    this.rightTrigger = rightTrigger;
    this.leftTrigger = leftTrigger;
    this.limit = limit;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    if (limit.getAsBoolean() == true) {
      Intake2.intakeFunction(0, rightTrigger.getAsDouble(), -leftTrigger.getAsDouble());
      roller.rollerFunction(0, rightTrigger.getAsDouble(), -leftTrigger.getAsDouble());
    } else {
      Intake2.intakeFunction(bumper, rightTrigger.getAsDouble(), -leftTrigger.getAsDouble());
      roller.rollerFunction(bumper, rightTrigger.getAsDouble(), -leftTrigger.getAsDouble());
    }
  }

  @Override
  public void end(boolean interrupted) {}
}
