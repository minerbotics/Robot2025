package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;

public class ElevatorManualMoveCommand extends Command {
    private final Elevator m_elevator;

    public ElevatorManualMoveCommand(Elevator elevator) {
        m_elevator = elevator;
    }

    public void execute() {
        m_elevator.raise();
    }

    public boolean isFinished() {
        return false;
    }

    public void end(boolean interrupted) {
        m_elevator.stop();
    }
}
