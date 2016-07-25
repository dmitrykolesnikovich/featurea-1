package featurea.swing;

import javax.swing.*;
import java.awt.*;

public class MyMenu extends JMenu {

  public MyMenu() {
    super();
  }

  public MyMenu(String s) {
    super(s);
  }

  public MyMenu(Action a) {
    super(a);
  }

  public MyMenu(String s, boolean b) {
    super(s, b);
  }

  public void add(char hotKey, JMenuItem menu) {
    menu.setAccelerator(KeyStroke.getKeyStroke(hotKey, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    add(menu);
  }

}
