// Copyright 2024 FRC 2486
// https://github.com/Coconuts2486-FRC/
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

package frc.robot.subsystems.limitswitch;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.BooleanSupplier;
import org.littletonrobotics.junction.AutoLogOutput;

public class LimitSwitch extends SubsystemBase {

  private final LimitSwitchIO io;
  private final LimitSwitchIOInputsAutoLogged inputs = new LimitSwitchIOInputsAutoLogged();

  public LimitSwitch(LimitSwitchIO io) {
    this.io = io;
  }

  public void periodic() {
    io.updateInputs(inputs);
  }

  @AutoLogOutput
  public BooleanSupplier is_pressed() {
    return io.is_pressed;
  }
}
