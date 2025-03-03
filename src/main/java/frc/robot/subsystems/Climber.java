package frc.robot.subsystems;


import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.generated.TunerConstants;

public class Climber extends SubsystemBase {

    private final SparkMax m_climber;
    private SparkMaxConfig config;

    public Climber() {
        m_climber = new SparkMax(TunerConstants.kClimberId, MotorType.kBrushless);
        config = new SparkMaxConfig();
        config.idleMode(IdleMode.kBrake);
        m_climber.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
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