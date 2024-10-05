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

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import java.util.function.BooleanSupplier;

public class LimitSwitchIONO implements LimitSwitchIO {

  private DigitalInput m_limitSwitch = new DigitalInput(0);

  public LimitSwitchIONO(int channel) {
    // Define the hardware
    m_limitSwitch = new DigitalInput(channel);
  }

  /**
   * For a Normally Open switch (signal connected to ground), pressing the switch connects the White
   * Signal line on the DigitalInput to the Red VCC line.
   */
  public BooleanSupplier is_pressed = new Trigger(m_limitSwitch::get);
}
