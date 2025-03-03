package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeEffector;

public class RetractAlgaeEffectorCommand extends Command {
    
    private final AlgaeEffector m_algaeEffector;

    public RetractAlgaeEffectorCommand(AlgaeEffector algaeEffector) {
        m_algaeEffector = algaeEffector;
        addRequirements(algaeEffector);
    }

    public void execute() {
        m_algaeEffector.armsIn();
    }
}
