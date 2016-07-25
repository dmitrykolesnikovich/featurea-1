package featurea.geometry;

import featurea.util.Transform;

public interface Bounds extends Transform {

  boolean contains(double x, double y);

  boolean intersects(Shape shape, double tolerance);

}
