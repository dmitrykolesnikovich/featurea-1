package featurea.util;

public class MathUtil {

  public static final double HALF_PI = Math.PI / 2;

  public static double sinDeg(double degrees) {
    return Math.sin(Math.toRadians(degrees));
  }

  public static double cosDeg(double degrees) {
    return Math.cos(Math.toRadians(degrees));
  }

  public static double square(double value) {
    return Math.pow(value, 2);
  }

}
