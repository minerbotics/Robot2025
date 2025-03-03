package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeEffector;

public class OuttakeAlgaeCommand extends Command {

    private final AlgaeEffector m_algaeEffector;

    public OuttakeAlgaeCommand(AlgaeEffector algaeEffector) {
        m_algaeEffector = algaeEffector;
        addRequirements(algaeEffector);
    }

    public void execute() {
        m_algaeEffector.algaeOut();
    }

    public boolean isFinished() {
        return false;
    }

    public void end(boolean interrupted) {
        m_algaeEffector.algaeStop();
    }
}
