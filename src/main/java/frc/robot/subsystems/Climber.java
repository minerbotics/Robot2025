package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.generated.TunerConstants;

public class Climber extends SubsystemBase {

    private final SparkMax m_climber;

    public Climber() {
        m_climber = new SparkMax(TunerConstants.kClimberId, MotorType.kBrushless);
    }

    public void wind() {
        m_climber.set(0.5);
    }

    public void unwind() {
        m_climber.set(-0.5);
    }

    public void stop() {
        m_climber.set(0);
}
}