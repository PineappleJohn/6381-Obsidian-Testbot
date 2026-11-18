package first.robot;

import com.ctre.phoenix6.CANBus;

public final class Consants {
  public static class GeneralConstants {
    public static final CANBus kCanBus = new CANBus("CANivore");
    public static final int CONTROLLER_ID = 0;
  }

    public static class IndexerConstants {
    public static final int INDEXER_MOTOR_PORT = 30;
    public static final int UPTAKE_WHEELS_PORT = 28;
    public static final int UPTAKE_BELT_PORT = 10;
  }

  public static class OuttakeConstants {
    public static final int UPPER_MOTOR_PORT = 0;
    public static final int LOWER_MOTOR_PORT = 0;

    public static final double kP = 0;
    public static final double kI = 0;
    public static final double kD = 0;

    public static final double StatorCurrentLimit = 200; // in amps
    public static final double SupplyCurrentLimit = 120;

    public static final double SHOOTER_RPS = 28;
  }
}