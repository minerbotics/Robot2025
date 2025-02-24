package frc.robot.commands;

import static edu.wpi.first.units.Units.*;


import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Limelight;

public class AlignOn extends Command {

    private final CommandSwerveDrivetrain m_drivetrain;
    private final Limelight m_limelight;
    private final String m_direction;

    private boolean m_inPosition;
    private double txMin, txMax, taMin, taMax;

    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond)*0.5;
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    public AlignOn(CommandSwerveDrivetrain drivetrain, String direction) {
        m_drivetrain = drivetrain;
        m_direction = direction;
        m_limelight = new Limelight();
        addRequirements(drivetrain);
    }

    public void execute() {

        if (m_direction.equals("right")) {
            txMin = -4;
            txMax = -2;
        }
        if (m_direction.equals("left")) {
            txMin = 2;
            txMax = 4;
        }

        double tx = m_limelight.getTX();
        double ta = m_limelight.getTA();
        double tv = m_limelight.getTV();

        double vx = 0.0;
        double vy = 0.0;
        double omega = 0.0;

        boolean inRange = false;
        boolean linedUp = false;

        if (tx > txMax || tx < txMin) {
            vy = tx * 0.035;
            linedUp = false;
        } else {
            vy = 0.0;
            linedUp = true;
        }
      
        if (ta > taMax) {
            // Forward
            vx = 0.5;
            linedUp = false;
        } else if (ta < taMin) {
            // Backward
            vx = -0.5;
            linedUp = false;
        } else {
            vx = 0.0;
            inRange = true;
        }
      
        if(linedUp && inRange) {
            m_inPosition = true;
        } else {
            m_inPosition = false;
        }

        move(vx,  vy, omega);
    }

    public boolean isFinished() {
        return m_inPosition;
    }

    public void end(boolean interrupted) {
        m_drivetrain.applyRequest(() -> new SwerveRequest.SwerveDriveBrake());
    }

    private void move(double vx, double vy, double omega) {
        
        m_drivetrain.applyRequest(() -> new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1)
            .withRotationalDeadband(MaxAngularRate * 0.1)
            .withVelocityX(-vx * MaxSpeed) // Drive forward with negative Y (forward)
            .withVelocityY(-vy * MaxSpeed) // Drive left with negative X (left)
            .withRotationalRate(-omega * MaxAngularRate)); // Drive counterclockwise with negative X (left));
    
    }
}
