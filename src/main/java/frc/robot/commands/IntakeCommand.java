// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.Intake2;
import java.util.function.DoubleSupplier;

public class IntakeCommand extends Command {
  private final Intake2 m_subsystem;
  private final DoubleSupplier rightTrigger;
  private final DoubleSupplier leftTrigger;

  public IntakeCommand(Intake2 subsystem, DoubleSupplier rightTrigger, DoubleSupplier leftTrigger) {
    m_subsystem = subsystem;
    this.rightTrigger = rightTrigger;
    this.leftTrigger = leftTrigger;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    Intake2.intakeFunction(rightTrigger.getAsDouble(), -leftTrigger.getAsDouble());
  }

  @Override
  public void end(boolean interrupted) {}
}
