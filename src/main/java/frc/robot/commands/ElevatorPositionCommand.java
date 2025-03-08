package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;

public class ElevatorPositionCommand extends Command {

    private final Elevator m_elevator;
    private double positionTolerance;
    private double targetPosition;
    private int m_level;

    public ElevatorPositionCommand(Elevator elevator, int level) {
        m_elevator = elevator;
        m_level = level;
        positionTolerance = 1.0;
        addRequirements(elevator);
    }

    public void execute() {
        switch(m_level) {
            case 0:
                targetPosition = 0;
                break;
            case 1:
                targetPosition = -22500;
                break;
            case 2:
                targetPosition = -42000;
                break;
            case 3:
                targetPosition = -72000;
                break;
            case 4:
                targetPosition = -32000;
                break;
            default:
                targetPosition = 0;
                break;
        }
        m_elevator.goToPosition(targetPosition);
    }

    public boolean isFinished() {
        return (isAtSetPoint());
    }

    public void end(boolean interrupted) {
        m_elevator.stop();
    }

    private boolean isAtSetPoint() {
        return (Math.abs(m_elevator.getPosition() - this.targetPosition) <= positionTolerance);
    }

}
