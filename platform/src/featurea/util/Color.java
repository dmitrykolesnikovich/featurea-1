package featurea.util;

public final class Color {

  public double r;
  public double g;
  public double b;
  public double a;

  public Color() {
    setValue(Colors.white);
  }

  public Color(Color color) {
    setValue(color);
  }

  public Color setValue(Color color) {
    this.r = color.r;
    this.g = color.g;
    this.b = color.b;
    this.a = color.a;
    return this;
  }

  public Color setValue(double r, double g, double b, double a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
    return this;
  }

  public Color(double r, double g, double b, double a) {
    setValue(r, g, b, a);
  }

  public Color r(double r) {
    this.r = r;
    return this;
  }

  public Color g(double g) {
    this.g = g;
    return this;
  }

  public Color b(double b) {
    this.b = b;
    return this;
  }

  public Color a(double a) {
    this.a = a;
    return this;
  }

  public Color(long rgba) {
    setRGBA(rgba);
  }

  public void setRGBA(long rgba) {
    r = ((double) ((rgba >> 24) & 0xFF)) / 255;
    g = ((double) ((rgba >> 16) & 0xFF)) / 255;
    b = ((double) ((rgba >> 8) & 0xFF)) / 255;
    a = ((double) (rgba & 0xFF)) / 255;
  }

  public long getRGBA() {
    long red = ((int) (r * 255)) << 24;
    long green = ((int) (g * 255)) << 16;
    long blue = ((int) (b * 255)) << 8;
    long alpha = (int) (a * 255);
    return red + green + blue + alpha;
  }

  public Color setValue(String value) {
    if (value.startsWith("#")) {
      String hexValue = value.replaceAll("#", "");
      Long rgba = Long.parseLong(hexValue, 16);
      setRGBA(rgba);
    } else {
      throw new IllegalArgumentException("value");
    }
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Color) {
      Color color = (Color) obj;
      return color.r == r && color.g == g && color.b == b && color.a == a;
    }
    return false;
  }

  public static Color multiply(Color color1, Color color2) {
    Color result = new Color();
    result.a = color1.a * color2.a;
    result.r = color1.r * color2.r;
    result.g = color1.g * color2.g;
    result.b = color1.b * color2.b;
    return result;
  }

  public void scale(double scale) {
    a *= scale;
    r *= scale;
    g *= scale;
    b *= scale;
  }

  public static Color valueOf(String primitive) {
    return new Color().setValue(primitive);
  }

}
