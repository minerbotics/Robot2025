package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.generated.TunerConstants;

public class AlgaeEffector extends SubsystemBase {
    
    private final DoubleSolenoid m_algaeSolenoid;
    private final SparkMax m_algaeMotor;

    public AlgaeEffector() {

        m_algaeSolenoid = new DoubleSolenoid(
            PneumaticsModuleType.CTREPCM,
            4,
            7);

        m_algaeMotor = new SparkMax(TunerConstants.kAlgaeMotorId, MotorType.kBrushless);
    }

    public void armsOut() {
        m_algaeSolenoid.set(Value.kForward);
    }

    public void armsIn() {
        m_algaeSolenoid.set(Value.kReverse);
    }

    public void algaeIn() {
        m_algaeMotor.set(0.5);
    }

    public void algaeOut() {
        m_algaeMotor.set(-0.5);
    }

    public void algaeStop() {
        m_algaeMotor.set(0);
    }

    public boolean armsExtended() {
        return m_algaeSolenoid.get() == Value.kForward;
    }
}