package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import au.grapplerobotics.LaserCan;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.generated.TunerConstants;

public class CoralEffector extends SubsystemBase {

    private final SparkMax m_coralEffectorMotor;

    private LaserCan m_LaserCan;

    public CoralEffector() {
        m_coralEffectorMotor = new SparkMax(TunerConstants.kCoralEffectorId, MotorType.kBrushless);
        m_LaserCan = new LaserCan(30);
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

    public double getDistance() {
        return m_LaserCan.getMeasurement().distance_mm;
    }
}
