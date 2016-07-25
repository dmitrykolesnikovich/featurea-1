package mario.layers;

import featurea.platformer.physics.Body;
import featurea.platformer.Animation;
import featurea.util.Vector;

import java.util.List;

public abstract class BodyLayer extends Animation {

  public Body findBlock(int x, int y) {
    for (Body body : listAreas()) {
      if (body.insideRectangle(x, y)) {
        return body;
      }
    }
    return null;
  }

  private final Vector tempDoNotReuse = new Vector();

  public Vector getVector(Body body, int[] array, int size) {
    if (array != null) {
      for (int i = 0; i < array.length; i += size) {
        int x = array[i];
        int y = array[i + 1];
        if (body.insideRectangle(x, y)) {
          if (size == 3) {
            tempDoNotReuse.z = array[i + 2];
          }
          tempDoNotReuse.setValue(x, y);
          return tempDoNotReuse;
        }
      }
    }
    return null;
  }

  public Vector getVector(Body body, int[] array) {
    return getVector(body, array, 2);
  }


  @Override
  public List<? extends Body> listAreas() {
    return (List<Body>) super.listAreas();
  }

}
