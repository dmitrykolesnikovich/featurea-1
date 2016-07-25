package featurea.platformer.physics;

import featurea.app.Area;
import featurea.geometry.Shape;
import featurea.platformer.Animation;
import featurea.util.ZOrder;

public class WorldZOrder implements ZOrder<Area> {

  @Override
  public int compare(Area area1, Area area2) {
    double z1 = fetchZ(area1);
    double z2 = fetchZ(area2);
    if (z1 > z2) {
      return 1;
    }
    if (z1 < z2) {
      return -1;
    }
    return 0;
  }

  private double fetchZ(Area area) {
    if (area instanceof Animation) {
      Animation animation = (Animation) area;
      return animation.z();
    }
    if (area instanceof Shape) {
      Shape shape = (Shape) area;
      return shape.oz();
    }
    throw new IllegalArgumentException("area = " + area.toString());
  }

}
