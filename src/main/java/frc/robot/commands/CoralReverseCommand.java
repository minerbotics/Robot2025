package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralEffector;

public class CoralReverseCommand extends Command {
    private final CoralEffector m_CoralEffector;

    public CoralReverseCommand(CoralEffector coralEffector) {
        m_CoralEffector = coralEffector;
        addRequirements(coralEffector);
    }

    public void execute() {
        m_CoralEffector.reverse();
    }

    public boolean isFinished() {
        return false;
    }

    public void end(boolean interrupted) {
        m_CoralEffector.stop();
    }
}
