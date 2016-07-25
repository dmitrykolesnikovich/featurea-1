package mario.util;

import featurea.app.Area;
import featurea.app.Projection;
import mario.objects.landscape.Tube;

public class TubeProjection extends Projection<Tube> {

  @Override
  public boolean onFilter(Area area) {
    return area instanceof Tube;
  }

}
