package featurea.util;

public class Size {

  public double width;
  public double height;

  public Size() {
    // no op
  }

  public Size(double width, double height) {
    this.width = width;
    this.height = height;
  }

  public Size setValue(double width, double height) {
    this.width = width;
    this.height = height;
    return this;
  }

  public Size setValue(Size value) {
    this.width = value.width;
    this.height = value.height;
    return this;
  }

  public Size setValue(String string) {
    String[] tokens = string.split(",");
    if (tokens.length != 2) {
      throw new IllegalArgumentException("Size format: tokens.length!=2");
    }
    try {
      width = Float.valueOf(tokens[0].trim());
      height = Float.valueOf(tokens[1].trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(e);
    }
    return this;
  }

  public boolean isEmpty() {
    return width == 0 && height == 0;
  }

  public static Size valueOf(String primitive) {
    return new Size().setValue(primitive);
  }

}
