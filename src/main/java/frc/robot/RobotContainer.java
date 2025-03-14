// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.CoralIntakeCommand;
import frc.robot.commands.CoralOuttakeCommand;
import frc.robot.commands.CoralReverseCommand;
import frc.robot.commands.ElevatorManualMoveCommand;
import frc.robot.commands.ElevatorPositionCommand;
import frc.robot.commands.IntakeAlgaeCommand;
import frc.robot.commands.OuttakeAlgaeCommand;
import frc.robot.commands.RetractAlgaeEffectorCommand;
import frc.robot.commands.ExtendAlgaeEffectorCommand;
import frc.robot.commands.TurnToReefCommand;
import frc.robot.commands.UnwindCommand;
import frc.robot.commands.WindCommand;
import frc.robot.commands.AlignOn;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.AlgaeEffector;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.CoralEffector;
import frc.robot.subsystems.Elevator;

public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond)*0.5; // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric()
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController driveController = new CommandXboxController(0);
    private final CommandXboxController operatorController = new CommandXboxController(1);

    public final CommandSwerveDrivetrain drivetrain;

    public final CoralEffector coralEffector;
    public final AlgaeEffector algaeEffector;
    public final Elevator elevator;
    public final Climber climber = new Climber();

    /* Path follower */
    private final SendableChooser<Command> autoChooser;

    public RobotContainer() {
        
        drivetrain = TunerConstants.createDrivetrain();

        coralEffector = new CoralEffector();
        algaeEffector = new AlgaeEffector();
        elevator = new Elevator();

        NamedCommands.registerCommand("elevatorL1", new ElevatorPositionCommand(elevator, 1));
        NamedCommands.registerCommand("elevatorL2", new ElevatorPositionCommand(elevator, 2));
        NamedCommands.registerCommand("elevatorL3", new ElevatorPositionCommand(elevator, 3));
        NamedCommands.registerCommand("coralOuttake", new CoralOuttakeCommand(coralEffector));

        autoChooser = AutoBuilder.buildAutoChooser("Basic Move");
        SmartDashboard.putData("Auto Mode", autoChooser);


        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(driveController.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(driveController.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-driveController.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        driveController.a().whileTrue(drivetrain.applyRequest(() -> brake));
        driveController.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(driveController.getLeftY(), -driveController.getLeftX()))
        ));

        driveController.pov(0).whileTrue(drivetrain.applyRequest(() ->
            forwardStraight.withVelocityX(-0.5).withVelocityY(0))
        );
        driveController.pov(180).whileTrue(drivetrain.applyRequest(() ->
            forwardStraight.withVelocityX(0.5).withVelocityY(0))
        );
        driveController.pov(90).whileTrue(drivetrain.applyRequest(() ->
            forwardStraight.withVelocityX(0).withVelocityY(0.5))
        );
        driveController.pov(270).whileTrue(drivetrain.applyRequest(() ->
            forwardStraight.withVelocityX(0).withVelocityY(-0.5))
        );
        driveController.pov(315).whileTrue(drivetrain.applyRequest(() ->
            forwardStraight.withVelocityX(0.5).withVelocityY(-0.5))
        );

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        driveController.back().and(driveController.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        driveController.back().and(driveController.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        driveController.start().and(driveController.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        driveController.start().and(driveController.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        driveController.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));
        driveController.leftTrigger().whileTrue(new AlignOn(drivetrain, "left"));
        driveController.rightTrigger().whileTrue(new AlignOn(drivetrain, "right"));

        driveController.x().whileTrue(new WindCommand(climber));
        driveController.y().whileTrue(new UnwindCommand(climber));

        driveController.rightStick().whileTrue(new TurnToReefCommand(drivetrain));

        drivetrain.registerTelemetry(logger::telemeterize);

        operatorController.rightBumper().whileTrue(new CoralOuttakeCommand(coralEffector));
        operatorController.leftBumper().whileTrue(new CoralIntakeCommand(coralEffector));
        operatorController.leftTrigger().whileTrue(new CoralReverseCommand(coralEffector));
        operatorController.a().whileTrue(new ElevatorPositionCommand(elevator, 1));
        operatorController.b().whileTrue(new ElevatorPositionCommand(elevator, 2));
        operatorController.x().whileTrue(new ElevatorPositionCommand(elevator, 3));
        operatorController.y().whileTrue(new ElevatorPositionCommand(elevator, 4));
        operatorController.start().whileTrue(new ElevatorPositionCommand(elevator, 0));
        operatorController.pov(0).whileTrue(new ElevatorManualMoveCommand(elevator));

        operatorController.rightStick().onTrue(new ExtendAlgaeEffectorCommand(algaeEffector));
        operatorController.leftStick().onTrue(new RetractAlgaeEffectorCommand(algaeEffector));
        operatorController.pov(90).whileTrue(new IntakeAlgaeCommand(algaeEffector));
        operatorController.pov(270).whileTrue(new OuttakeAlgaeCommand(algaeEffector));
    }

    public Command getAutonomousCommand() {
        /* Run the path selected from the auto chooser */
        return autoChooser.getSelected();
    }
}
