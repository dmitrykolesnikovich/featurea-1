package featurea.geometry;

import featurea.util.Vector;

public final class Intersector {

  private Intersector() {
    // no op
  }

  public static boolean collide(Polygon polygon1, Polygon polygon2, double tolerance) {
    for (int i = 0, iNext = i + 1; i < polygon1.points.length / 2; i++, iNext = i + 1) {
      if (iNext >= polygon1.points.length / 2) {
        iNext = 0;
      }
      double x1 = polygon1.points[i * 2];
      double y1 = polygon1.points[i * 2 + 1];
      double x2 = polygon1.points[iNext * 2];
      double y2 = polygon1.points[iNext * 2 + 1];
      x1 = cut(x1, polygon1.ox(), tolerance);
      y1 = cut(y1, polygon1.oy(), tolerance);
      x2 = cut(x2, polygon1.ox(), tolerance);
      y2 = cut(y2, polygon1.oy(), tolerance);
      for (int j = 0, jNext = j + 1; j < polygon2.points.length / 2; j++, jNext = j + 1) {
        if (jNext >= polygon2.points.length / 2) {
          jNext = 0;
        }
        double x3 = polygon2.points[j * 2];
        double y3 = polygon2.points[j * 2 + 1];
        double x4 = polygon2.points[jNext * 2];
        double y4 = polygon2.points[jNext * 2 + 1];
        x3 = cut(x3, polygon2.ox(), tolerance);
        y3 = cut(y3, polygon2.oy(), tolerance);
        x4 = cut(x4, polygon2.ox(), tolerance);
        y4 = cut(y4, polygon2.oy(), tolerance);
        if (hasIntersection(x1, y1, x2, y2, x3, y3, x4, y4)) {
          return true;
        }
      }
    }
    return false;
  }

  public static double cut(double value, double origin, double tolerance) {
    double delta = value - origin;
    delta -= tolerance;
    value = origin + delta;
    return value;
  }

  public static boolean collide(Polygon polygon1, Polygon polygon2) {
    return collide(polygon1, polygon2, 0);
  }

  public static boolean collide(Circle circle1, Circle circle2) {
    return collide(circle1, circle2, 0);
  }

  public static boolean collide(Circle circle1, Circle circle2, double tolerance) {
    if (circle1.angle.getValue() != 0 && circle1.width != circle1.height) {
      throw new IllegalStateException();
    }
    if (circle2.angle.getValue() != 0 && circle2.width != circle2.height) {
      throw new IllegalStateException();
    }
    double r1 = circle1.width / 2 - tolerance;
    double r2 = circle2.height / 2 - tolerance;
    double dx = Math.abs(circle1.ox() - circle2.ox());
    double dy = Math.abs(circle1.oy() - circle2.oy());
    return Vector.length(dx, dy) <= (r1 + r2);
  }

  public static boolean collide(Polygon polygon, Circle circle, double tolerance) {
    for (int i = 0, iNext = i + 1; i < polygon.points.length / 2; i++, iNext = i + 1) {
      if (iNext >= polygon.points.length / 2) {
        iNext = 0;
      }
      double x1 = polygon.points[i * 2];
      double y1 = polygon.points[i * 2 + 1];
      double x2 = polygon.points[iNext * 2];
      double y2 = polygon.points[iNext * 2 + 1];
      if (hasIntersection(circle, x1, y1, x2, y2, tolerance)) {
        return true;
      }
    }
    return false;
  }

  public static boolean collide(Polygon polygon, Circle circle) {
    return collide(polygon, circle, 0);
  }

  public static boolean hasIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
    double UA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
    double UB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
    return UA >= 0 && UA <= 1 && UB >= 0 && UB <= 1;
  }

  private static boolean hasIntersection(Circle circle, double x1, double y1, double x2, double y2, double tolerance) {
    double a = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
    double b = 2 * ((circle.ox() - x1) * (x2 - x1) + (circle.oy() - y1) * (y2 - y1));
    double width = circle.width - 2 * tolerance;
    double height = circle.height - 2 * tolerance;
    double c = ((circle.ox() - x1) * (circle.ox() - x1) + (circle.oy() - y1) * (circle.oy() - y1)) - width / 2 * height / 2;
    double discriminant = b * b - 4 * a * c;
    if (discriminant > 0) {
      discriminant = Math.sqrt(discriminant);
      double t1 = (-b - discriminant) / (2 * a);
      double t2 = (-b + discriminant) / (2 * a);
      if (t1 >= 0 && t1 <= 1) {
        return true;
      }
      if (t2 >= 0 && t2 <= 1) {
        return true;
      }
    }
    return false;
  }
}