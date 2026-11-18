package first.robot.mechanisms;

import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.command3.Trigger;

import com.ctre.phoenix6.configs.ClosedLoopGeneralConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.CoastOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import first.robot.Consants.GeneralConstants;
import first.robot.Consants.OuttakeConstants;

public class Outtake implements Mechanism {
    public static Outtake instance;
    public IndexerMechanism indexer;
    public TalonFX upper, lower;
    public Trigger CanShoot;

    public Outtake(IndexerMechanism indexer)
    {
        instance = this;
        this.indexer = indexer;

        upper = new TalonFX(OuttakeConstants.UPPER_MOTOR_PORT, GeneralConstants.kCanBus);
        lower = new TalonFX(OuttakeConstants.LOWER_MOTOR_PORT, GeneralConstants.kCanBus);

        TalonFXConfiguration config = new TalonFXConfiguration();

        config.CurrentLimits.StatorCurrentLimitEnable = true;
        config.CurrentLimits.StatorCurrentLimit = OuttakeConstants.StatorCurrentLimit;
        config.CurrentLimits.SupplyCurrentLimitEnable = true;
        config.CurrentLimits.SupplyCurrentLimit = OuttakeConstants.SupplyCurrentLimit;

        config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        config.withClosedLoopGeneral(new ClosedLoopGeneralConfigs().withContinuousWrap(true));

        config.withSlot0(new Slot0Configs().withKP(OuttakeConstants.kP).withKI(OuttakeConstants.kI).withKD(OuttakeConstants.kI));

        upper.getConfigurator().apply(config);
        lower.setControl(new Follower(upper.getDeviceID(), MotorAlignmentValue.Opposed));

        CanShoot = new Trigger(this::UpToSpeed);
    }

    public boolean UpToSpeed()
    {
        return (Math.abs(upper.getVelocity().getValueAsDouble() - OuttakeConstants.SHOOTER_RPS) < 1); // less than 50 rpm
    }

    public Command BeginShooter(double speed)
    {
        return run(coroutine -> {
            VelocityVoltage request = new VelocityVoltage(speed).withSlot(0);
            upper.setControl(request);
            indexer.ControlVoltage(5);
        }).named("Shooter begin");
    }

    public Command EndShooter()
    {
        return run(coroutine -> {
            upper.setControl(new CoastOut());
            indexer.ControlVoltage(0);
        }).named("Stop shooter");
    }
}
