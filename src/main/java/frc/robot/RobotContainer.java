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

// Do we need all the copyrights? If not I'll delete 'em

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.commands.AmpmechCommands;
// import frc.robot.commands.ClimbCommand;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.intake.IntakeCommand;
import frc.robot.commands.intake.IntakeCommandauto;
import frc.robot.commands.shooter.ShooterCommands;
import frc.robot.commands.shooter.SpinUpCommands;
// import frc.robot.subsystems.climb.climb;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIONutBlend;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.elevator.elevator;
import frc.robot.subsystems.flywheel.Flywheel;
import frc.robot.subsystems.flywheel.FlywheelIO;
import frc.robot.subsystems.flywheel.FlywheelIOSim;
import frc.robot.subsystems.flywheel.FlywheelIOTalonFX;
import frc.robot.subsystems.indexer.indexer;
import frc.robot.subsystems.intake.Intake2;
import frc.robot.subsystems.shooter.Shooter;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

public class RobotContainer {
  // Subsystems
  private final Drive drive;
  //   private final climb climb = new climb();
  private final Flywheel flywheel;
  private final elevator elevator = new elevator();
  private final indexer roller = new indexer();
  private final Intake2 intake = new Intake2();
  private final Shooter shooter = new Shooter();
  private final DigitalInput intakeStop = new DigitalInput(0);
  private final DigitalInput elevatorStop = new DigitalInput(1);
  private final DigitalInput elevatorStop2 = new DigitalInput(2);
  private Trigger intakeTrigger = new Trigger(intakeStop::get);
  private Trigger elevatorTrigger = new Trigger(elevatorStop::get);
  private Trigger elevatorTrigger2 = new Trigger(elevatorStop2::get);

  // Controller Setups
  private final CommandXboxController driver = new CommandXboxController(0);
  private final CommandXboxController codriver = new CommandXboxController(1);

  // Dashboard Inputs
  private final LoggedDashboardChooser<Command> autoChooser;
  private final LoggedDashboardNumber flywheelSpeedInput =
      new LoggedDashboardNumber("Flywheel Speed", 1500.0);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    switch (Constants.currentMode) {
      case REAL:

        // Real robot, instantiate hardware IO implementation
        /*
         * Do we have a flywheel?
         * And whats a flywheel?
         * Otherwise I dont think this code is being used
         */
        flywheel = new Flywheel(new FlywheelIOTalonFX());
        drive =
            new Drive(
                new GyroIOPigeon2(),
                new ModuleIONutBlend(0),
                new ModuleIONutBlend(1),
                new ModuleIONutBlend(2),
                new ModuleIONutBlend(3));
        drive.setPose(new Pose2d());
        break;

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIOSim(),
                new ModuleIOSim(),
                new ModuleIOSim(),
                new ModuleIOSim());
        flywheel = new Flywheel(new FlywheelIOSim());
        break;

      default:
        // Replayed robot, disable IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {});
        flywheel = new Flywheel(new FlywheelIO() {});
        break;
    }

    // Set up auto routines
    // NamedCommands.registerCommand(
    //     "Run Flywheel",
    //     Commands.startEnd(
    //             () -> flywheel.runVelocity(flywheelSpeedInput.get()), flywheel::stop, flywheel)
    //         .withTimeout(5.0));

    NamedCommands.registerCommand(
        "runIntake",
        new IntakeCommandauto(roller, intake, 0.50, () -> 0, () -> 0, () -> intakeStop.get()));

    NamedCommands.registerCommand("shoot", new ShooterCommands(roller, 1.0).withTimeout(1));

    NamedCommands.registerCommand("spinUp", new SpinUpCommands(shooter).withTimeout(0.5));

    autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

    // Set up SysId routines
    autoChooser.addOption(
        "Drive SysId (Quasistatic Forward)",
        drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Drive SysId (Quasistatic Reverse)",
        drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
    autoChooser.addOption(
        "Drive SysId (Dynamic Forward)", drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Drive SysId (Dynamic Reverse)", drive.sysIdDynamic(SysIdRoutine.Direction.kReverse));
    autoChooser.addOption(
        "Flywheel SysId (Quasistatic Forward)",
        flywheel.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Flywheel SysId (Quasistatic Reverse)",
        flywheel.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
    autoChooser.addOption(
        "Flywheel SysId (Dynamic Forward)", flywheel.sysIdDynamic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Flywheel SysId (Dynamic Reverse)", flywheel.sysIdDynamic(SysIdRoutine.Direction.kReverse));

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // shooter commands

    // spin up
    codriver.rightBumper().whileTrue(new SpinUpCommands(shooter));

    // shoot
    codriver.leftBumper().toggleOnTrue(new ShooterCommands(roller, 0.75));

    // ampmech Command
    driver
        .a()
        .toggleOnTrue(
            new AmpmechCommands(elevator, roller, elevatorTrigger, elevatorTrigger2, 0)
                .withTimeout(5));

    // Intake Command

    driver
        .rightBumper()
        .whileFalse(
            new IntakeCommand(
                roller,
                intake,
                0,
                () -> driver.getRightTriggerAxis(),
                () -> driver.getLeftTriggerAxis(),
                () -> intakeStop.get()))
        .whileTrue(
            new IntakeCommand(roller, intake, 0.33, () -> 0, () -> 0, () -> intakeStop.get()));

    // Climb Command
    // climb.setDefaultCommand(new ClimbCommand(climb,() -> codriver.getRightY()));

    // Drive Command
    drive.setDefaultCommand(
        DriveCommands.joystickDrive(
            drive, () -> driver.getLeftY(), () -> driver.getLeftX(), () -> driver.getRightX()));
    // driver.x().onTrue(Commands.runOnce(drive::stopWithX, drive));
    /* controller
    *   .y()
    *     .onTrue(
    *         Commands.runOnce(
    *                 () ->
    *                     drive.setPose(
    *                         new Pose2d(drive.getPose().getTranslation(), new Rotation2d())),
    *                drive)
               .ignoringDisable(true));  */

    // Gryo reset button
    driver.y().onTrue(Commands.runOnce(() -> drive.zero(), drive));
    // driver
    //     .a()
    //     .whileTrue(
    //         Commands.startEnd(
    //             () -> flywheel.runVelocity(flywheelSpeedInput.get()), flywheel::stop, flywheel));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.get();
  }
}
