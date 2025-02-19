package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralEffector;

public class CoralIntakeCommand extends Command {
    private final CoralEffector m_CoralEffector;

    public CoralIntakeCommand(CoralEffector coralEffector) {
        m_CoralEffector = coralEffector;
        addRequirements(coralEffector);
    }

    public void execute() {
        m_CoralEffector.intake();
    }

    public boolean isFinished() {
        return (m_CoralEffector.getDistance() < 100);
    }

    public void end(boolean interrupted) {
        m_CoralEffector.stop();
    }
}
