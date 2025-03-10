package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeEffector;

public class ExtendAlgaeEffectorCommand extends Command {
    
    private final AlgaeEffector m_algaeEffector;

    public ExtendAlgaeEffectorCommand(AlgaeEffector algaeEffector) {
        m_algaeEffector = algaeEffector;
        addRequirements(algaeEffector);
    }

    public void execute() {
        m_algaeEffector.armsOut();
    }
}
