package frc.robot.commands;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.generated.TunerConstants;

public class TurnToReefCommand extends Command{

    private final CommandSwerveDrivetrain m_drivetrain;
    private final Pigeon2 pidgey = new Pigeon2(TunerConstants.kPigeonId);
    private final SwerveRequest.FieldCentric m_driveRequest;

    private double angleTolerance = 5.0;

    public TurnToReefCommand(CommandSwerveDrivetrain drivetrain) {
        m_drivetrain = drivetrain;
        m_driveRequest = new SwerveRequest.FieldCentric();
    }

    public void execute() {
        double current = getCurrentPigeonAngle();
        double target = getNearestReefAngle();

        double error = target - current;

        double turnSpeed = error * 0.1;

        m_drivetrain.setControl(
            m_driveRequest.withRotationalRate(turnSpeed)
        );
    }

    public boolean isFinished() {
        return atTargetAngle();
    }

    public void end(boolean interrupted) {
        m_drivetrain.setControl(
            m_driveRequest
                .withVelocityX(0)
                .withVelocityY(0)
                .withRotationalRate(0)
        );
    }

    private double getCurrentPigeonAngle() {
        return pidgey.getYaw().getValueAsDouble() % 360;
    }

    private double getNearestReefAngle() {
        return (Math.round(getCurrentPigeonAngle()/60))*60;
    }

    private boolean atTargetAngle() {
        return (Math.abs(getCurrentPigeonAngle() - getNearestReefAngle()) < angleTolerance);
    }
}
