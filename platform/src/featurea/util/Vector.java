package featurea.util;

import java.util.List;

import static featurea.util.MathUtil.square;

public class Vector {

  public double x;
  public double y;
  public double z;

  public Vector() {
    // no op
  }

  public Vector(double x, double y, double z) {
    setValue(x, y, z);
  }

  public Vector(double x, double y) {
    setValue(x, y);
  }

  public Vector(Vector vector) {
    this(vector.x, vector.y, vector.z);
  }

  public static double[] rotate(double x, double y, double ox, double oy, Angle angle) {
    double resultX = ((x - ox) * angle.cos() - (y - oy) * angle.sin()) + ox;
    double resultY = ((x - ox) * angle.sin() + (y - oy) * angle.cos()) + oy;
    return new double[]{resultX, resultY};
  }

  public static double[] scale(double x, double y, double ox, double oy, double scale) {
    double dx = x - ox;
    dx *= scale;
    x = ox + dx;
    double dy = y - oy;
    dy *= scale;
    y = oy + dy;
    return new double[]{x, y};
  }

  public Vector setValue(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
    return this;
  }

  public Vector setValue(double x, double y) {
    setValue(x, y, this.z);
    return this;
  }

  public Vector setValue(Vector vector) {
    setValue(vector.x, vector.y, vector.z);
    return this;
  }

  public Vector plus(Vector vector) {
    return plus(vector.x, vector.y, vector.z);
  }

  public Vector plus(double x, double y) {
    this.x += x;
    this.y += y;
    return this;
  }

  public Vector plus(double x, double y, double z) {
    this.x += x;
    this.y += y;
    this.z += z;
    return this;
  }

  public Vector minus(double x, double y) {
    this.x -= x;
    this.y -= y;
    return this;
  }

  public Vector minus(Vector vector) {
    return minus(vector.x, vector.y, vector.z);
  }

  public Vector minus(double x, double y, double z) {
    this.x -= x;
    this.y -= y;
    this.z -= z;
    return this;
  }

  public double length() {
    return Math.sqrt(x * x + y * y);
  }

  public static double length(double x, double y) {
    return Math.sqrt(x * x + y * y);
  }

  private final Angle poolAngle = new Angle();

  public Vector rotate(double angle) {
    poolAngle.setValue(angle);
    rotate(this, poolAngle);
    return this;
  }

  public void rotate(double ox, double oy, Angle angle) {
    Vector.rotate(this, ox, oy, angle);
  }

  public Vector setLength(double length) {
    return multiply(length / length());
  }

  public Vector multiply(double value) {
    x *= value;
    y *= value;
    z *= value;
    return this;
  }

  public static void rotate(Vector result, double cos, double sin) {
    rotate(result, 0, 0, cos, sin);
  }

  public static void rotate(Vector result, Vector origin, double cos, double sin) {
    rotate(result, origin.x, origin.y, cos, sin);
  }

  public static void rotate(Vector result, Vector origin, double angle) {
    rotate(result, origin, new Angle(angle));
  }

  public static void rotate(Vector result, Vector origin, Angle angle) {
    rotate(result, origin, angle.cos(), angle.sin());
  }

  public static void rotate(Vector result, double ox, double oy, Angle angle) {
    Vector.rotate(result, ox, oy, angle.cos(), angle.sin());
  }

  public static void rotate(Vector result, double ox, double oy, double cos, double sin) {
    double x2 = ((result.x - ox) * cos - (result.y - oy) * sin);
    double y2 = ((result.x - ox) * sin + (result.y - oy) * cos);
    result.setValue(ox + x2, oy + y2);
  }

  public double distanceTo(Vector vector) {
    double distance2 = (square(x - vector.x) + square(y - vector.y) + square(z - vector.z));
    double distance = Math.sqrt(distance2);
    return distance;
  }

  public static Vector minus(double x2, double y2, double x1, double y1) {
    Vector result = new Vector().setValue(x2, y2);
    result.plus(-x1, -y1);
    return result;
  }

  public static Vector minus(Vector vector1, Vector vector2) {
    Vector result = new Vector(vector1);
    result.minus(vector2);
    return result;
  }

  public static Vector plus(Vector vector1, Vector vector2) {
    Vector result = new Vector(vector1);
    result.plus(vector2);
    return result;
  }

  public double dot(Vector vector) {
    return x * vector.x + y * vector.y;
  }

  public static Vector multiply(Vector vector, double value) {
    return new Vector().setValue(vector).multiply(value);
  }

  public Vector cloneRevert() {
    return new Vector().setValue(this).revert();
  }

  public Vector revert() {
    this.x *= -1;
    this.y *= -1;
    this.z *= -1;
    return this;
  }

  public void scaleX(double ox, double deltaScaleX) {
    if (deltaScaleX != 1) {
      double dx = x - ox;
      dx *= deltaScaleX;
      x = ox + dx;
    }
  }

  public void scaleY(double oy, double deltaScaleY) {
    if (deltaScaleY != 1) {
      double dy = y - oy;
      dy *= deltaScaleY;
      y = oy + dy;
    }
  }

  public static void rotate(Vector vector, Angle angle) {
    rotate(vector, angle.cos(), angle.sin());
  }

  public static void scale(Vector result, Vector origin, double scale) {
    double dx = (result.x - origin.x);
    double dy = (result.y - origin.y);
    dx *= scale;
    dy *= scale;
    result.x = origin.x + dx;
    result.y = origin.y + dy;
  }

  public static String toString(List<Vector> list) {
    String result = "";
    for (Vector vector : list) {
      result += vector.x + ", " + vector.y + ", ";
    }
    result = result.substring(0, result.length() - 2);
    return result;
  }

  public void flipX(double ox) {
    x += 2 * (ox - x);
  }

  public void flipY(double oy) {
    y += 2 * (oy - y);
  }

  @Override
  public String toString() {
    return x + "," + y + "," + z;
  }

  public boolean isEmpty() {
    return x == 0 && y == 0 && z == 0;
  }

  public static Vector valueOf(String primitive) {
    Vector result = new Vector();
    String[] tokens = ArrayUtil.split(primitive);
    try {
      if (tokens.length == 3) {
        result.x = Double.valueOf(tokens[0]);
        result.y = Double.valueOf(tokens[1]);
        result.z = Double.valueOf(tokens[2]);
      } else if (tokens.length == 2) {
        result.x = Double.valueOf(tokens[0]);
        result.y = Double.valueOf(tokens[1]);
      } else {
        throw new IllegalArgumentException("primitive");
      }
    } catch (NumberFormatException skip) {
      // no op
    }
    return result;
  }

  public double cos() {
    return x / length();
  }

  public double sin() {
    return y / length();
  }

}
