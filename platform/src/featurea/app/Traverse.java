package featurea.app;

import java.util.List;

public class Traverse {

  public boolean isEnable = true;
  private final Layer layer;

  public Traverse(Layer layer) {
    this.layer = layer;
  }

  public final void onTraverse(Projection<? extends Area> projection) {
    if (isEnable) {
      projection.clear();
      performTraverse(layer, projection);
    }
  }

  /*private API*/

  private void performTraverse(Area parent, Projection projection) {
    List<? extends Area> children = parent.listAreas();
    if (children != null) {
      for (Area child : children) {
        if (projection.onFilter(child)) {
          projection.add(child);
        }
        performTraverse(child, projection);
      }
    }
  }

}
