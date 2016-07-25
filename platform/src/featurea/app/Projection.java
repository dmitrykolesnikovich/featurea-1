package featurea.app;

import featurea.util.Filter;

import java.util.ArrayList;
import java.util.List;

public class Projection<T extends Area> extends ArrayList<T> implements Filter {

  @Override
  public boolean onFilter(Area area) {
    return true;
  }

  public List<T> subList(Projection<?> projection) {
    clear();
    for (Area element : projection) {
      if (onFilter(element)) {
        add((T) element);
      }
    }
    return this;
  }

  public Projection project(Layer layer) {
    layer.traverse.onTraverse(this);
    return this;
  }

}
