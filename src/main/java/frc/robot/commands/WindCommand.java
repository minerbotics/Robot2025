package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;

public class WindCommand extends Command {
    private final Climber m_climber;

    public WindCommand(Climber climber) {
        m_climber = climber;
        addRequirements(climber);
    }

    public void execute() {
        m_climber.wind();
    }

    public boolean isFinished() {
        return false;
    }

    public void end(boolean interrupted) {
        m_climber.stop();
    }
}