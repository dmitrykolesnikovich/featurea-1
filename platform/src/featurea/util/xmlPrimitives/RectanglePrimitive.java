package featurea.util.xmlPrimitives;

import featurea.app.MediaPlayer;
import featurea.graphics.Graphics;
import featurea.util.Colors;
import featurea.util.Selection;
import featurea.util.Vector;
import featurea.xml.XmlPrimitive;
import featurea.xml.XmlTag;

public class RectanglePrimitive extends XmlPrimitive {

  private int x1;
  private int y1;
  private int x2;
  private int y2;

  public RectanglePrimitive(MediaPlayer mediaPlayer, XmlTag xmlTag, String key) {
    super(mediaPlayer, xmlTag, key);
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
    }
    return false;
  }

  @Override
  public boolean onTouchUp(double x, double y, int id) {
    if (id == 0) {
      createRectangle();
    }
    return false;
  }

  @Override
  public void onDraw(Graphics graphics) {
    super.onDraw(graphics);
    graphics.drawRectangle(x1, y1, x2, y2, Colors.orange);
  }

  private void createRectangle() {
    int xMin = Math.min(x1, x2);
    int xMax = Math.max(x1, x2);
    int yMin = Math.min(y1, y2);
    int yMax = Math.max(y1, y2);
    x1 = xMin;
    y1 = yMin;
    x2 = xMax;
    y2 = yMax;

    Vector position = Vector.valueOf(xmlTag.getAttribute("position"));
    x1 -= position.x;
    x2 -= position.x;
    y1 -= position.y;
    y2 -= position.y;

    String value = x1 + ", " + y1 + ", " + x2 + ", " + y2;
    xmlTag.setAttribute(key, value);
    save();
  }

}