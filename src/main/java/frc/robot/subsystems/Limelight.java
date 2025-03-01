package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {
    
    private NetworkTable m_limelightTable;
    private double ty, tx, ta, tv, ts, tid;

    public Limelight() {
        m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public void periodic() {
        ty = m_limelightTable.getEntry("ty").getDouble(0);
        tx = m_limelightTable.getEntry("tx").getDouble(0);
        ta = m_limelightTable.getEntry("ta").getDouble(2.5);
        tv = m_limelightTable.getEntry("tv").getDouble(0);
        ts = m_limelightTable.getEntry("ts").getDouble(0);
        tid = m_limelightTable.getEntry("tid").getDouble(0);
        boolean m_limelightHasTarget = (tv < 1.0) ? false : true;

        SmartDashboard.putNumber("Limelight X", tx);
        SmartDashboard.putNumber("Limelight Y", ty);
        SmartDashboard.putNumber("Limelight Area", ta);
        SmartDashboard.putBoolean("Limelight Sees Target", m_limelightHasTarget);
        SmartDashboard.putNumber("Limelight Skew", ts);
        SmartDashboard.putNumber("Target ID", tid);
    }

    public double getTX() {
        return tx;
    }

    public double getTY() {
        return ty;
    }

    public double getTA() {
        return ta;
    }

    public double getTV() {
        return tv;
    }

    public double getTS() {
        return ts;
    }

    public double getTargetId() {
        return tid;
    }
}
