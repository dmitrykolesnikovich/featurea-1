package featurea.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MyScrollPanel extends JScrollPane {

  public MyScrollPanel() {
    setLayout(new ScrollPaneLayout() {
      @Override
      public Dimension preferredLayoutSize(Container parent) {
        Dimension dim = super.preferredLayoutSize(parent);
        JScrollPane pane = (JScrollPane) parent;
        Component comp = pane.getViewport().getView();
        Dimension viewPref = comp.getPreferredSize();
        Dimension port = pane.getViewport().getExtentSize();
        // **Edit 2** changed condition to <= to prevent jumping
        if (port.height < viewPref.height) {
          dim.width += pane.getVerticalScrollBar().getPreferredSize().width;
        }
        return dim;
      }
    });
    getVerticalScrollBar().setBlockIncrement(24);
    getVerticalScrollBar().setUnitIncrement(24);
    setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    setBackground(Color.white);
  }

  public static MyScrollPanel wrap(Component component) {
    MyScrollPanel myScrollPanel = new MyScrollPanel();
    myScrollPanel.setViewportView(component);
    return myScrollPanel;
  }

  @Override
  public void processMouseEvent(MouseEvent e) {
    super.processMouseEvent(e);
  }

}
