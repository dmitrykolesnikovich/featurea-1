package featurea.geometry;

import featurea.util.*;

import java.util.Arrays;

public class Polygon implements Bounds {

  public double[] points;
  public boolean isLine;
  public double left;
  public double top;
  public double right;
  public double bottom;

  public Polygon(double... points) {
    this.points = points;
    update();
  }

  public Polygon(Polygon polygon) {
    if (polygon.points != null) {
      points = Arrays.copyOf(polygon.points, polygon.points.length);
    }
    this.isLine = polygon.isLine;
    update();
  }

  public Polygon setLine(boolean isLine) {
    this.isLine = isLine;
    return this;
  }

  @Override
  public boolean intersects(Shape shape, double tolerance) {
    if (shape.polygon != null) {
      return Intersector.collide(this, shape.polygon, tolerance);
    }
    if (shape.circle != null) {
      return Intersector.collide(this, shape.circle, tolerance);
    }
    return false;
  }

  public boolean contains(double x, double y) {
    boolean result = false;
    double oldX = points[points.length - 2];
    double oldY = points[points.length - 1];
    for (int i = 0; i < points.length; i += 2) {
      double newX = points[i];
      double newY = points[i + 1];
      double x1, y1, x2, y2;
      if (newX > oldX) {
        x1 = oldX;
        y1 = oldY;
        x2 = newX;
        y2 = newY;
      } else {
        x1 = newX;
        y1 = newY;
        x2 = oldX;
        y2 = oldY;
      }
      if ((newX < x) == (x <= oldX) && (y - y1) * (x2 - x1) < (y2 - y1) * (x - x1)) {
        result = !result;
      }
      oldX = newX;
      oldY = newY;
    }
    return result;
  }

  @Override
  public void move(double dx, double dy, double dz) {
    for (int i = 0, j = i + 1; i < points.length; i += 2, j = i + 1) {
      points[i] += dx;
      points[j] += dy;
    }
    left += dx;
    right += dx;
    top += dy;
    bottom += dy;
  }

  public void move(Vector vector) {
    move(vector.x, vector.y, vector.z);
  }

  @Override
  public void rotate(Angle angle) {
    rotate(ox(), oy(), angle);
  }

  public void rotate(double ox, double oy, Angle angle) {
    double cos = angle.cos();
    double sin = angle.sin();
    for (int i = 0, j = i + 1; i < points.length; i += 2, j = i + 1) {
      double x2 = ox + (points[i] - ox) * cos - (points[j] - oy) * sin;
      double y2 = oy + (points[i] - ox) * sin + (points[j] - oy) * cos;
      points[i] = x2;
      points[j] = y2;
    }
    update();
  }

  @Override
  public void scaleX(double deltaScaleX) {
    scaleX(ox(), deltaScaleX);
  }

  @Override
  public void scaleX(double ox, double deltaScaleX) {
    for (int i = 0, j = i + 1; i < points.length; i += 2, j = i + 1) {
      double dx = points[i] - ox;
      dx *= deltaScaleX;
      points[i] = ox + dx;
    }
    update();
  }

  @Override
  public void scaleY(double deltaScaleY) {
    scaleY(oy(), deltaScaleY);
  }

  @Override
  public void scaleY(double oy, double deltaScaleY) {
    for (int i = 0, j = i + 1; i < points.length; i += 2, j = i + 1) {
      double dy = (points[j] - oy);
      dy *= deltaScaleY;
      points[j] = oy + dy;
    }
    update();
  }

  @Override
  public void flipX() {
    flipX(ox());
  }

  @Override
  public void flipX(double ox) {
    for (int i = 0; i < points.length; i += 2) {
      points[i] += 2 * (ox - points[i]);
    }
    update();
  }

  @Override
  public void flipY() {
    flipY(oy());
  }

  @Override
  public void flipY(double oy) {
    for (int i = 0, j = i + 1; i < points.length; i += 2, j = i + 1) {
      points[j] += 2 * (oy - points[j]);
    }
    update();
  }

  public double ox() {
    return (left + right) / 2;
  }

  public double oy() {
    return (top + bottom) / 2;
  }

  public double left() {
    return left;
  }

  public double top() {
    return top;
  }

  public double right() {
    return right;
  }

  public double bottom() {
    return bottom;
  }

  private void update() {
    left = Integer.MAX_VALUE;
    top = Integer.MAX_VALUE;
    right = Integer.MIN_VALUE;
    bottom = Integer.MIN_VALUE;
    for (int x = 0, y = 1; x < points.length - 1; x += 2, y += 2) {
      if (points[x] < left) {
        left = points[x];
      }
      if (points[x] > right) {
        right = points[x];
      }
      if (points[y] < top) {
        top = points[y];
      }
      if (points[y] > bottom) {
        bottom = points[y];
      }
    }
  }

  public void setValue(String value) {
    String[] tokens = ArrayUtil.split(value);
    if (tokens.length % 2 == 0) {
      points = new double[tokens.length];
      for (int i = 0; i < tokens.length; i++) {
        points[i] = Float.valueOf(tokens[i].trim());
      }
    }
    throw new IllegalArgumentException("value");
  }

}
