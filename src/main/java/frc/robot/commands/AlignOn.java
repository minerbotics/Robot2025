package frc.robot.commands;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Limelight;

public class AlignOn extends Command {

    private final CommandSwerveDrivetrain m_drivetrain;
    private final Limelight m_limelight;
    private final String m_direction;
    private final SwerveRequest.RobotCentric m_alignOnRequest;

    private double targetX, targetArea = 0.35;
    private static final double distanceTolerance = 0.1;
    private static final double angleTolerance = 1.0;

    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond)*0.5;

    private double lastValidX = 0.0;
    private double lastValidArea = 0.0;

    private double lastHorizontalAdjust = 0.0;

    public AlignOn(CommandSwerveDrivetrain drivetrain, String direction) {
        m_drivetrain = drivetrain;
        m_direction = direction;
        m_limelight = new Limelight();
        m_alignOnRequest = new SwerveRequest.RobotCentric()
            .withDeadband(0.1)
            .withRotationalDeadband(0.1)
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

        addRequirements(drivetrain);
    }

    public void execute() {

        double currentX = m_limelight.getTX();
        double currentArea = m_limelight.getTA();

        if (m_direction.equals("right")) {
            targetX = -3;
        }
        if (m_direction.equals("left")) {
            targetX = 3;
        }

        if (currentX != targetX || currentArea != targetArea) {
            lastValidX = currentX;
            lastValidArea = currentArea;
        } else {
            currentX = 0.0;
            currentArea = 0.0;
        }

        double distanceError = targetArea - currentArea;
        double horizontalError = -currentX; // Invert TX for horizontal adjustment
        
        double targetingForwardSpeed = distanceError * -0.1;
        System.out.println("Calculated targetingForwardSpeed: " + targetingForwardSpeed);
        targetingForwardSpeed *= MaxSpeed;
        targetingForwardSpeed *= -1.0;
        double distanceAdjust = targetingForwardSpeed;
        
        double horizontalAdjust = horizontalError * 0.05;
        
        // Smoothing the horizontal adjustment to prevent jerky movement
        horizontalAdjust = (horizontalAdjust + lastHorizontalAdjust) / 2;
        lastHorizontalAdjust = horizontalAdjust;

        System.out.println("AlignCommand executing");
        System.out.println("Distance Adjust: " + distanceAdjust);
        System.out.println("Horizontal Adjust: " + horizontalAdjust);

        move(distanceAdjust, horizontalAdjust, 0);
    }

    public boolean isFinished() {
        return Math.abs(lastValidArea - targetArea) < distanceTolerance && Math.abs(lastValidX - targetX) < angleTolerance;
    }

    public void end(boolean interrupted) {
        m_drivetrain.setControl(
            m_alignOnRequest
                .withVelocityX(0)
                .withVelocityY(0)
                .withRotationalRate(0)
        );
    }

    private void move(double vx, double vy, double omega) {
        m_drivetrain.setControl(
            m_alignOnRequest
                .withVelocityX(vx)  // Forward/backward movement
                .withVelocityY(vy) // Horizontal (lateral) movement
                .withRotationalRate(omega) // Rotational correction
        );
    }
}
