package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeEffector;

public class ToggleAlgaeEffectorCommand extends Command {
    
    private final AlgaeEffector m_algaeEffector;

    public ToggleAlgaeEffectorCommand(AlgaeEffector algaeEffector) {
        m_algaeEffector = algaeEffector;
        addRequirements(algaeEffector);
    }

    public void execute() {
        if (m_algaeEffector.armsExtended()) {
            m_algaeEffector.armsIn();
        } else {
            m_algaeEffector.armsOut();
        }
    }
}
