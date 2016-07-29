package featurea.opengl;

import featurea.util.Angle;
import featurea.util.Vector;

// usage: setValue -> rotate
public class TextureRectangle {

  public final Vector point1 = new Vector();
  public final Vector point2 = new Vector();
  public final Vector point3 = new Vector();
  public final Vector point4 = new Vector();

  public TextureRectangle setValue(double x1, double y1, double x2, double y2) {
    point1.setValue(x1, y1);
    point2.setValue(x2, y1);
    point3.setValue(x2, y2);
    point4.setValue(x1, y2);
    return this;
  }

  public TextureRectangle rotate(double ox, double oy, Angle angle) {
    if (angle != null && angle.getValue() != 0) {
      Vector.rotate(point1, ox, oy, angle);
      Vector.rotate(point2, ox, oy, angle);
      Vector.rotate(point3, ox, oy, angle);
      Vector.rotate(point4, ox, oy, angle);
    }
    return this;
  }

}
