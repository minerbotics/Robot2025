package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AlgaeEffector extends SubsystemBase {

    private final DoubleSolenoid m_algaeSolenoid;

    public AlgaeEffector() {

        m_algaeSolenoid = new DoubleSolenoid(
                PneumaticsModuleType.CTREPCM,
                0,
                1);

    }

    public void armsOut() {
        m_algaeSolenoid.set(Value.kForward);
    }

    public void armsIn() {
        m_algaeSolenoid.set(Value.kReverse);
    }

    public boolean armsExtended() {
        return m_algaeSolenoid.get() == Value.kForward;
    }
}