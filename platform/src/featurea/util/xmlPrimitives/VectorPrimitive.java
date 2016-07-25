package featurea.util.xmlPrimitives;

import featurea.app.MediaPlayer;
import featurea.util.Vector;
import featurea.xml.XmlPrimitive;
import featurea.xml.XmlTag;

public class VectorPrimitive extends XmlPrimitive {

  private double x;
  private double y;
  private final Vector position = new Vector();

  public VectorPrimitive(MediaPlayer mediaPlayer, XmlTag xmlTag, String key) {
    super(mediaPlayer, xmlTag, key);
    String positionPrimitive = xmlTag.getAttribute(key);
    if (positionPrimitive != null) {
      position.setValue(Vector.valueOf(positionPrimitive));
    }
  }

  @Override
  public boolean onTouchDown(double x, double y, int id) {
    if (id == 0) {
      this.x = x;
      this.y = y;
    }
    return false;
  }

  @Override
  public boolean onTouchDrag(double x, double y, int id) {
    if (id == 0) {
      double dx = x - this.x;
      double dy = y - this.y;
      editPosition(dx, dy);
      this.x = x;
      this.y = y;
    }
    return false;
  }

  @Override
  public boolean onTouchUp(double x, double y, int id) {
    if (id == 0) {
      save();
    }
    return false;
  }

  private void editPosition(double dx, double dy) {
    position.plus(dx, dy);
    double x = roundValue(position.x);
    double y = roundValue(position.y);
    double z = roundValue(position.z);
    xmlTag.setAttribute(key, x + ", " + y + ", " + z);
  }

}
