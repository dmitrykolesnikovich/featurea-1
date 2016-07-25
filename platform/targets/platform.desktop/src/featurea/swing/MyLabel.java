package featurea.swing;

import javax.swing.*;
import java.awt.*;

public class MyLabel extends JLabel {

  @Override
  public Insets getInsets() {
    Insets result = super.getInsets();
    result.left = 0;
    result.right = 0;
    result.bottom = 0;
    result.top = 0;
    return result;
  }

  @Override
  public Insets getInsets(Insets insets) {
    Insets result = super.getInsets(insets);
    result.left = 0;
    result.right = 0;
    result.bottom = 0;
    result.top = 0;
    return result;
  }

}
