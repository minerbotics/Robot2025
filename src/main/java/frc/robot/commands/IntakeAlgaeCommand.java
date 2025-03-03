package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeEffector;

public class IntakeAlgaeCommand extends Command {

    private final AlgaeEffector m_algaeEffector;

    public IntakeAlgaeCommand(AlgaeEffector algaeEffector) {
        m_algaeEffector = algaeEffector;
        addRequirements(algaeEffector);
    }

    public void execute() {
        m_algaeEffector.algaeIn();
    }

    public boolean isFinished() {
        return false;
    }

    public void end(boolean interrupted) {
        m_algaeEffector.algaeStop();
    }
}
