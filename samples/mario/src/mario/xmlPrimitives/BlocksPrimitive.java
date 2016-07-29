package mario.xmlPrimitives;

import featurea.app.MediaPlayer;
import featurea.graphics.Graphics;
import featurea.util.Colors;
import featurea.xml.XmlPrimitive;
import featurea.xml.XmlTag;

public class BlocksPrimitive extends XmlPrimitive {

  private int x1;
  private int y1;
  private int x2;
  private int y2;
  private String value;

  public BlocksPrimitive(MediaPlayer mediaPlayer, XmlTag xmlTag, String key) {
    super(mediaPlayer, xmlTag, key);
    value = xmlTag.getAttribute(key);
  }

  @Override
  public boolean onTouchDown(double x, double y, int id) {
    if (id == 0) {
      x1 = roundValue(x);
      y1 = roundValue(y);
      x2 = x1;
      y2 = y1;
    }
    return false;
  }

  @Override
  public boolean onTouchDrag(double x, double y, int id) {
    if (id == 0) {
      x2 = roundValue(x);
      y2 = roundValue(y);
      createNewBlocks();
    }
    return false;
  }

  @Override
  public boolean onTouchUp(double x, double y, int id) {
    if (id == 0) {
      createNewBlocks();
      int xMin = Math.min(x1, x2);
      int xMax = Math.max(x1, x2);
      int yMin = Math.min(y1, y2);
      int yMax = Math.max(y1, y2);
      x1 = xMin;
      y1 = yMin;
      x2 = xMax;
      y2 = yMax;
      save();
    }
    return false;
  }

  @Override
  public void onDraw(Graphics graphics) {
    if (!graphics.containsDrawLine()) {
      super.onDraw(graphics);
      graphics.drawRectangle(x1, y1, x2, y2, Colors.orange);
    }
  }

  private void createNewBlocks() {
    if (value == null) {
      value = "";
    }
    String result = value;
    result += ", " + x1 + ", " + y1 + ", " + x2 + ", " + y2;
    result = result.trim();
    if (result.startsWith(",")) {
      result = result.substring(1, result.length());
    }
    xmlTag.setAttribute(key, result);
  }

}
