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

import java.util.function.BooleanSupplier;
import org.littletonrobotics.junction.AutoLog;

public interface LimitSwitchIO {
  @AutoLog
  public static class LimitSwitchIOInputs {}

  public default void updateInputs(LimitSwitchIOInputs inputs) {}

  /** Returns whether the switch is pressed */
  public BooleanSupplier is_pressed = () -> false;
}
