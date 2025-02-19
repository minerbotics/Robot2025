package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.generated.TunerConstants;


public class CoralEffector extends SubsystemBase {
    
    private final SparkMax m_coralEffectorMotor;

    public CoralEffector() {
        m_coralEffectorMotor = new SparkMax(TunerConstants.kCoralEffectorId, MotorType.kBrushless);
    }

    public void intake() {
        m_coralEffectorMotor.set(-0.5);
    }

    public void outtake() {
        m_coralEffectorMotor.set(-0.5);
    }

    public void stop() {
        m_coralEffectorMotor.set(0);
    }
}
