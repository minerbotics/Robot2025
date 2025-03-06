package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.generated.TunerConstants;

public class Elevator extends SubsystemBase {
    private final SparkMax m_leftElevatorMotor, m_rightElevatorMotor;
    private SparkMaxConfig leftMaxConfig;
    private SparkMaxConfig rightMaxConfig;
    private SparkClosedLoopController m_leftPid;


    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;

    public Elevator() {
        m_leftElevatorMotor = new SparkMax(TunerConstants.kLeftElevatorId, MotorType.kBrushless);
        m_rightElevatorMotor = new SparkMax(TunerConstants.kRightElevatorId, MotorType.kBrushless);
        rightMaxConfig = new SparkMaxConfig();
        leftMaxConfig = new SparkMaxConfig();

        // PID coefficients
        kP = 1.5;
        kI = 0;
        kD = 0;
        kIz = 0;
        kFF = 0.05;
        kMaxOutput = 0.5;
        kMinOutput = -0.5;

        leftMaxConfig
                .smartCurrentLimit(80)
                .idleMode(IdleMode.kBrake);
        leftMaxConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(kP, kI, kD)
                .velocityFF(kFF)
                .maxOutput(kMaxOutput)
                .minOutput(kMinOutput);
        rightMaxConfig
                .follow(TunerConstants.kLeftElevatorId)
                .smartCurrentLimit(80)
                .idleMode(IdleMode.kBrake);
        m_leftElevatorMotor.configure(leftMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_rightElevatorMotor.configure(rightMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        m_leftPid = m_leftElevatorMotor.getClosedLoopController();
        
        // display PID coefficients on SmartDashboard
        SmartDashboard.putNumber("P Gain", kP);
        SmartDashboard.putNumber("I Gain", kI);
        SmartDashboard.putNumber("D Gain", kD);
        SmartDashboard.putNumber("I Zone", kIz);
        SmartDashboard.putNumber("Feed Forward", kFF);
        SmartDashboard.putNumber("Max Output", kMaxOutput);
        SmartDashboard.putNumber("Min Output", kMinOutput);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Encoder Position Degrees", this.getPosition());
        SmartDashboard.putNumber("Encoder Position Rotations", m_leftElevatorMotor.getEncoder().getPosition());
        SmartDashboard.putNumber("Alt Encoder Velocity", m_leftElevatorMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("Applied Output", m_leftElevatorMotor.getAppliedOutput());
    }

    public double getPosition() {
        return (m_leftElevatorMotor.getEncoder().getPosition() * 360);
    }

    public void raise() {
        m_leftElevatorMotor.set(-0.5);
    }

    public void stop() {
        m_leftElevatorMotor.set(0);
    }

    public void goToPosition(double position) {
        double rotations = degreesToRotation(position);
        SmartDashboard.putNumber("Set Point Rotations", rotations);
        SmartDashboard.putNumber("Set Point Degrees", position);
        m_leftPid.setReference(rotations, ControlType.kPosition);
    }

    private double degreesToRotation(double degrees) {
        return (degrees / 360);
    }

}
