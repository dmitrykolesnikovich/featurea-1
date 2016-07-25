package featurea.xmlEditor.util;

import featurea.graphics.Graphics;
import featurea.util.Colors;

public final class SelectionRender {

  private SelectionRender(){
    // no op
  }

  public static void draw(Graphics graphics, double x1, double y1, double x2, double y2) {
    graphics.drawLine(x1, -1_000_000, x1, 1_000_000, Colors.cyan);
    graphics.drawLine(x2, -1_000_000, x2, 1_000_000, Colors.cyan);
    graphics.drawLine(-1_000_000, y1, 1_000_000, y1, Colors.cyan);
    graphics.drawLine(-1_000_000, y2, 1_000_000, y2, Colors.cyan);
    graphics.drawRectangle(x1, y1, x2, y2, Colors.focus);
  }

}
