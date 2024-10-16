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
import frc.robot.FieldConstants.AprilTagLayoutType;
import frc.robot.commands.AmpmechCommands;
import frc.robot.commands.ClimbCommand;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.Target;
import frc.robot.commands.intake.IntakeCommand;
import frc.robot.commands.intake.IntakeCommandauto;
import frc.robot.commands.shooter.Pivot;
import frc.robot.commands.shooter.QuickShoot;
import frc.robot.commands.shooter.SpinUpCommands;
import frc.robot.commands.shooter.regressionCommand;
import frc.robot.commands.shooter.setpoint;
import frc.robot.subsystems.ampmech.elevator;
import frc.robot.subsystems.ampmech.roller;
import frc.robot.subsystems.apriltagvision.AprilTagVision;
import frc.robot.subsystems.apriltagvision.AprilTagVisionIO;
import frc.robot.subsystems.apriltagvision.AprilTagVisionIOPhotonVision;
import frc.robot.subsystems.climb.climb;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIONutBlend;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.intake.Intake2;
import frc.robot.subsystems.shooter.pivot;
import frc.robot.subsystems.shooter.shooter;
import java.util.function.BooleanSupplier;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

public class RobotContainer {
  // Subsystems
  private final Drive m_robotDrive;
  private final climb climb = new climb();
  private final pivot pivot = new pivot();
  private final elevator elevator = new elevator();
  private final roller roller = new roller();
  private final Intake2 intake = new Intake2();
  private final shooter shooter = new shooter();
  private final AprilTagVision aprilTagVision;
  private final DigitalInput intakeStop = new DigitalInput(0);
  private final DigitalInput elevatorStop = new DigitalInput(1);
  private final DigitalInput elevatorStop2 = new DigitalInput(2);
  private final DigitalInput pivotBottom = new DigitalInput(4);
  private final DigitalInput pivotTop = new DigitalInput(5);
  private Trigger intakeTrigger = new Trigger(intakeStop::get);
  private Trigger elevatorTrigger = new Trigger(elevatorStop::get);
  private Trigger elevatorTrigger2 = new Trigger(elevatorStop2::get);
  private Trigger pivotTrigger = new Trigger(pivotBottom::get);
  private Trigger pivotTrigger2 = new Trigger(pivotTop::get);
  private BooleanSupplier intakeTrue = intakeStop::get;
  // private BooleanSupplier topthng = pivotTop::get;

  // Controller Setups
  private final CommandXboxController driver = new CommandXboxController(0);
  private final CommandXboxController codriver = new CommandXboxController(1);
  private final Trigger climbTriggerUp = new Trigger(() -> codriver.getRightY() > .1);
  private final Trigger climbTriggerDown = new Trigger(() -> codriver.getRightY() < -.1);
  // Dashboard Inputs
  private final LoggedDashboardChooser<Command> autoChooser;
  private final LoggedDashboardNumber flywheelSpeedInput =
      new LoggedDashboardNumber("Flywheel Speed", 1500.0);

  /** Returns the current AprilTag layout type. */
  public AprilTagLayoutType getAprilTagLayoutType() {
    return FieldConstants.defaultAprilTagType;
  }

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    switch (Constants.getMode()) {
      case REAL:

        // Real robot, instantiate hardware IO implementation
        /*
         * Do we have a flywheel?
         * And whats a flywheel?
         * Otherwise I dont think this code is being used
         */
        m_robotDrive =
            new Drive(
                new GyroIOPigeon2(),
                new ModuleIONutBlend(0),
                new ModuleIONutBlend(1),
                new ModuleIONutBlend(2),
                new ModuleIONutBlend(3));
        m_robotDrive.setPose(new Pose2d());
        aprilTagVision =
            new AprilTagVision(
                this::getAprilTagLayoutType,
                new AprilTagVisionIOPhotonVision(this::getAprilTagLayoutType, "Photon_BW1"));

        break;

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        m_robotDrive =
            new Drive(
                new GyroIO() {},
                new ModuleIOSim(),
                new ModuleIOSim(),
                new ModuleIOSim(),
                new ModuleIOSim());
        aprilTagVision = new AprilTagVision(this::getAprilTagLayoutType);
        break;

      default:
        // Replayed robot, disable IO implementations
        m_robotDrive =
            new Drive(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {});
        aprilTagVision =
            new AprilTagVision(
                this::getAprilTagLayoutType, new AprilTagVisionIO() {}, new AprilTagVisionIO() {});

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
        new IntakeCommandauto(
                roller,
                intake,
                0.75,
                () -> 0,
                () -> 0,
                () -> intakeStop.get(),
                () -> elevatorStop2.get())
            .withTimeout(5));

    // NamedCommands.registerCommand("shoot", new ShooterCommands(roller, 1.0).withTimeout(0.5));
    NamedCommands.registerCommand("shoot", new QuickShoot(roller, shooter));
    NamedCommands.registerCommand("shoot1", new QuickShoot(roller, shooter).withTimeout(2.4));
    NamedCommands.registerCommand("shoot2", new QuickShoot(roller, shooter).withTimeout(2.2));

    NamedCommands.registerCommand("regress1", new setpoint(pivot, 0.968));
    NamedCommands.registerCommand("regress2", new setpoint(pivot, 0.975));
    NamedCommands.registerCommand("regress3", new setpoint(pivot, 0.975));
    NamedCommands.registerCommand("target", new Target(m_robotDrive, aprilTagVision));

    NamedCommands.registerCommand("spinUp", new SpinUpCommands(shooter).withTimeout(0.6));
    NamedCommands.registerCommand("spinUp1", new SpinUpCommands(shooter).withTimeout(2.1));

    // NamedCommands.registerCommand("ampspinup", new SpinUpCommands(shooter).withTimeout(0.75));
    // NamedCommands.registerCommand("spinUp0", new SpinUpCommands(shooter).withTimeout(0.25));
    // NamedCommands.registerCommand("spinUp1", new SpinUpCommands(shooter).withTimeout(1.12));
    // NamedCommands.registerCommand("spinUp2", new SpinUpCommands(shooter).withTimeout(1.5));
    // NamedCommands.registerCommand("spinUp3", new SpinUpCommands(shooter).withTimeout(2.64));

    // NamedCommands.registerCommand("spinUpAmp1", new SpinUpCommands(shooter).withTimeout(3.04));
    // NamedCommands.registerCommand("spinUpAmp2", new SpinUpCommands(shooter).withTimeout(3.2));
    // NamedCommands.registerCommand("spinUpAmpSet", new SpinUpCommands(shooter).withTimeout(0.69));

    autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

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

    // Target the speaker
    // driver
    //     .a()
    //     .whileTrue(
    //         new TargetTagCommand(
    //             m_robotDrive,
    //             () -> driver.getLeftY(),
    //             () -> driver.getLeftX(),
    //             aprilTagVision.getSpeakerYaw().getDegrees()));

    // codriver.x().whileTrue(new regressionCommand(pivot, aprilTagVision));
    // driver.a().whileTrue(new Target(m_robotDrive, aprilTagVision));
    codriver
        .x()
        .whileTrue(
            Commands.parallel(
                new regressionCommand(pivot, aprilTagVision, 0.954),
                new Target(m_robotDrive, aprilTagVision)));

    // shooter commands

    // spin up
    codriver.rightBumper().whileTrue(new QuickShoot(roller, shooter));

    // shoot
    // codriver.leftBumper().toggleOnTrue(new ShooterCommands(roller, 0.75));

    // pivot

    // codriver.x().whileTrue(new Pivot(pivot, pivotTrigger, pivotTrigger2, 1));

    codriver
        .povUp()
        .whileTrue(new Pivot(0, pivot, pivotTrigger, pivotTrigger2, 1, 0.1))
        .whileFalse(new Pivot(0, pivot, pivotTrigger, pivotTrigger2, 1, 0));
    codriver
        .povDown()
        .whileTrue(new Pivot(0, pivot, pivotTrigger, pivotTrigger2, 1, -0.05))
        .whileFalse(new Pivot(0, pivot, pivotTrigger, pivotTrigger2, 1, 0));


    driver
        .leftBumper()
        .whileTrue(Commands.parallel(new setpoint(pivot, 0), new QuickShoot(roller, shooter)));

    codriver.y().whileTrue(new setpoint(pivot, 0.88));

    // codriver.y().toggleOnTrue(new Pivot(0, pivot, pivotTrigger, pivotTrigger2, 2, 0.50));
    // driver
    //     .x()
    //     .toggleOnTrue(new Pivot(0.5, pivot, pivotTrigger, pivotTrigger2, 1))
    //     .whileFalse(new Pivot(0, pivot, climbTriggerDown, elevatorTrigger, 0));

    // ampmech Command
    codriver.
    a()
        .toggleOnTrue(
            new AmpmechCommands(
                    elevator, roller, elevatorTrigger, elevatorTrigger2, () -> intakeStop.get(), 1)
                .withTimeout(3));
    // elevator emergency down
    codriver
        .b()
        .toggleOnTrue(
            new AmpmechCommands(
                    elevator, roller, elevatorTrigger, elevatorTrigger2, () -> intakeStop.get(), 3)
                .withTimeout(3));

     driver
         .x()
        .toggleOnTrue(
             new AmpmechCommands(
                 elevator, roller, elevatorTrigger, elevatorTrigger2, () -> intakeStop.get(), 4));

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
                () -> intakeStop.get(),
                () -> elevatorStop2.get()))
        .whileTrue(
            new IntakeCommand(
                roller,
                intake,
                0.5,
                () -> 0,
                () -> 0,
                () -> intakeStop.get(),
                () -> elevatorStop2.get()));

    // Climb Command
    climbTriggerUp.whileTrue(new ClimbCommand(climb, () -> codriver.getRightY()));
    climbTriggerDown.whileTrue(new ClimbCommand(climb, () -> codriver.getRightY()));

    // Drive Command
    m_robotDrive.setDefaultCommand(
        DriveCommands.joystickDrive(
            m_robotDrive,
            () -> driver.getLeftY(),
            () -> driver.getLeftX(),
            () -> driver.getRightX()));

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
    driver.y().toggleOnTrue(Commands.runOnce(() -> m_robotDrive.zero(), m_robotDrive));
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
