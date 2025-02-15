package frc.robot.subsystems;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.generated.TunerConstants;


public class Elevator extends SubsystemBase {
    private final SparkMax m_leftElevatorMotor, m_rightElevatorMotor;
    private SparkMaxConfig leftMaxConfig;
    private SparkMaxConfig rightMaxConfig;
    private SparkClosedLoopController m_leftPid;

    public Elevator() {
        m_leftElevatorMotor = new SparkMax(TunerConstants.kLeftElevatorId, MotorType.kBrushless);
        m_rightElevatorMotor = new SparkMax(TunerConstants.kRightElevatorId, MotorType.kBrushless);
        rightMaxConfig = new SparkMaxConfig();
        leftMaxConfig = new SparkMaxConfig();
        
        leftMaxConfig
            .smartCurrentLimit(60);
        leftMaxConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(0.1,0.0,0.0);
        rightMaxConfig
            .follow(TunerConstants.kLeftElevatorId, true)
            .smartCurrentLimit(60);
        m_leftElevatorMotor.configure(leftMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_rightElevatorMotor.configure(rightMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        
        m_leftPid = m_leftElevatorMotor.getClosedLoopController();
    }

    public double getPosition() {
        return m_leftElevatorMotor.getEncoder().getPosition();
    }

    public void raise() {
        m_leftElevatorMotor.set(-0.5);
    }

    public void stop() {
        m_leftElevatorMotor.set(0);
    }

    public void goToPosition(double position) {
        double rotations = degreesToRotation(position);
        m_leftPid.setReference(rotations, ControlType.kPosition);
    }

    private double degreesToRotation(double degrees) {
        return (degrees / 360);
    }

}
