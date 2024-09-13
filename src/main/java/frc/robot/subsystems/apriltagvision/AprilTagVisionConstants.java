// Copyright (c) 2024 FRC 2486
// http://github.com/Coconuts2486-FRC
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

package frc.robot.subsystems.apriltagvision;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.util.Units;
import frc.robot.Constants;

public class AprilTagVisionConstants {
  public static final double ambiguityThreshold = 0.4;
  public static final double targetLogTimeSecs = 0.1;
  public static final double fieldBorderMargin = 0.5;
  public static final double zMargin = 0.75;
  public static final double xyStdDevCoefficient = 0.005;
  public static final double thetaStdDevCoefficient = 0.01;

  public static final Pose3d[] cameraPoses =
      switch (Constants.getRobot()) {
        case COMPBOT -> new Pose3d[] {
          new Pose3d(
              Units.inchesToMeters(-1.0),
              Units.inchesToMeters(0),
              Units.inchesToMeters(23.5),
              new Rotation3d(0.0, Units.degreesToRadians(-20), 0.0)),
        };
        case DEVBOT -> new Pose3d[] {};
        default -> new Pose3d[] {};
      };
}
