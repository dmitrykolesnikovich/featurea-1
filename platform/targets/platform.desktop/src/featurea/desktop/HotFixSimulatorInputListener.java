package featurea.desktop;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HotFixSimulatorInputListener extends MouseAdapter {

  private final Simulator simulator;

  public HotFixSimulatorInputListener(Simulator simulator) {
    this.simulator = simulator;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      simulator.onDoubleClick();
    }
  }

}
