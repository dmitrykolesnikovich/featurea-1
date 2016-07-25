package featurea.util;

public interface TransformScale {
  void scaleX(double deltaScaleX);

  void scaleX(double ox, double deltaScaleX);

  void scaleY(double deltaScaleY);

  void scaleY(double oy, double deltaScaleY);
}
