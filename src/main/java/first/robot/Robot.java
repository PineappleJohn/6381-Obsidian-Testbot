// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot;

import org.wpilib.command3.Scheduler;
import org.wpilib.command3.button.CommandXboxController;
import org.wpilib.framework.OpModeRobot;

import first.robot.Consants.GeneralConstants;
import first.robot.Consants.OuttakeConstants;
import first.robot.mechanisms.IndexerMechanism;
import first.robot.mechanisms.Outtake;

public class Robot extends OpModeRobot {
  public IndexerMechanism m_indexer = new IndexerMechanism();
  public Outtake m_shooter = new Outtake(m_indexer);
  public CommandXboxController controller = new CommandXboxController(GeneralConstants.CONTROLLER_ID);

  public Robot() {

    controller.rightBumper().onTrue(m_shooter.BeginShooter(OuttakeConstants.SHOOTER_RPS)).onFalse(m_shooter.EndShooter());
    
  }

  @Override
  public void robotPeriodic() {
    Scheduler.getDefault().run();
  }
}
