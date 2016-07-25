package featurea.util.xmlPrimitives;

import featurea.app.MediaPlayer;
import featurea.util.Angle;
import featurea.util.Selection;
import featurea.util.Vector;
import featurea.xml.XmlPrimitive;
import featurea.xml.XmlTag;

public class TransformPrimitive extends XmlPrimitive {

  private static final String ANGLE = "angle";
  private static final String SCALE = "scale";

  private double x;
  private double y;
  private final Angle angle = new Angle();
  private final Vector position = new Vector();

  public TransformPrimitive(MediaPlayer mediaPlayer, XmlTag xmlTag, String key) {
    super(mediaPlayer, xmlTag, key);
    String primitiveAngle = xmlTag.getAttribute(ANGLE);
    if (primitiveAngle != null) {
      angle.setValue(Angle.valueOf(primitiveAngle));
    }
  }

  @Override
  public boolean onTouchDown(double x, double y, int id) {
    position.setValue(getSelection().position);
    if (id == 0) {
      this.x = x;
      this.y = y;
      System.out.println("TransformPrimitive.onTouchDown: " + (x + ", " + y));
    }
    return false;
  }

  @Override
  public boolean onTouchDrag(double x, double y, int id) {
    if (id == 0) {
      double dx = x - this.x;
      double dy = y - this.y;
      drag(dx, dy);
      this.x = x;
      this.y = y;
    }
    return false;
  }

  private void drag(double dx, double dy) {
    if (dx != 0 || dy != 0) {
      if (mediaPlayer.input.keyboard.isAltDown()) {
        editAngle(dx / mediaPlayer.render.zoom.scale);
      } else if (mediaPlayer.input.keyboard.isShiftDown()) {
        // no op
      } else {
        editPosition(dx, dy);
      }
    }
  }

  @Override
  public boolean onTouchUp(double x, double y, int id) {
    if (id == 0) {
      super.onTouchUp(x, y, id);
      save();
    }
    return false;
  }

  /*@Override
  public void onKeyDown(Key key) {
    if (key == Key.DPAD_RIGHT) {
      drag(1, 0);
      Context.getInput().keyboard.keyUp(key);
    }
    if (key == Key.DPAD_LEFT) {
      drag(-1, 0);
      Context.getInput().keyboard.keyUp(key);
    }
    if (key == Key.DPAD_UP) {
      drag(0, -1);
      Context.getInput().keyboard.keyUp(key);
    }
    if (key == Key.DPAD_DOWN) {
      drag(0, 1);
      Context.getInput().keyboard.keyUp(key);
    }
  }*/

  private void editPosition(double dx, double dy) {
    position.plus(dx, dy);
    double x = roundValue(position.x);
    double y = roundValue(position.y);
    double z = roundValue(position.z);
    xmlTag.setAttribute(key, x + ", " + y + ", " + z);
  }

  private void editAngle(double dx) {
    angle.plus(dx / 20 * 360);
    double value = roundValue(angle.getValue());
    xmlTag.setAttribute(ANGLE, value + "");
  }

}
