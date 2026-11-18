package first.robot.mechanisms;

import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import first.robot.Consants.GeneralConstants;
import first.robot.Consants.IndexerConstants;

public class IndexerMechanism implements Mechanism {
    public static IndexerMechanism instance;
    public TalonFX index, uptakeWheel, belt;

    private VoltageOut vOut;

    public IndexerMechanism()
    {
        instance = this;

        index = new TalonFX(IndexerConstants.INDEXER_MOTOR_PORT, GeneralConstants.kCanBus);
        uptakeWheel = new TalonFX(IndexerConstants.UPTAKE_BELT_PORT, GeneralConstants.kCanBus);
        belt = new TalonFX(IndexerConstants.UPTAKE_BELT_PORT, GeneralConstants.kCanBus);

        uptakeWheel.setControl(new Follower(IndexerConstants.INDEXER_MOTOR_PORT, MotorAlignmentValue.Aligned));
        belt.setControl(new Follower(IndexerConstants.INDEXER_MOTOR_PORT, MotorAlignmentValue.Opposed));

        TalonFXConfiguration configuration = new TalonFXConfiguration();

        vOut = new VoltageOut(0.0);

        configuration.CurrentLimits.StatorCurrentLimitEnable = true;
        configuration.CurrentLimits.SupplyCurrentLimitEnable = true;
        configuration.CurrentLimits.StatorCurrentLimit = Math.pow(2, 31) - 1;
        configuration.CurrentLimits.SupplyCurrentLimit = Math.pow(2, 31) - 1;

        configuration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        configuration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

        index.getConfigurator().apply(configuration);
        uptakeWheel.getConfigurator().apply(configuration);
        belt.getConfigurator().apply(configuration);
    }

    public void ControlVoltage(double volts)
    {
        index.setControl(vOut.withOutput(volts));
    }


    public Command RunIndexer(double volts)
    {
        return run(coroutine -> {
            ControlVoltage(volts);
        }).named("RunIndexer");
    }
}
