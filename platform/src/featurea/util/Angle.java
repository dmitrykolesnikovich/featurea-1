package featurea.util;

public final class Angle implements Cloneable {

  private double sin;
  private double cos;
  private double value;

  public Angle() {
    setValue(0);
  }

  public Angle(double angle) {
    setValue(angle);
  }

  public Angle setValue(double angle) {
    value = angle;
    update();
    return this;
  }

  public Angle setValue(Angle angle) {
    this.value = angle.value;
    this.sin = angle.sin;
    this.cos = angle.cos;
    return this;
  }

  public double getValue() {
    return value;
  }

  public double sin() {
    return sin;
  }

  public double cos() {
    return cos;
  }

  public Angle plus(double angle) {
    value += angle;
    update();
    return this;
  }

  public Angle plus(Angle angle) {
    return plus(angle.value);
  }

  private void update() {
    cos = MathUtil.cosDeg(value);
    sin = MathUtil.sinDeg(value);
  }

  @Override
  public Angle clone() {
    Angle result = new Angle();
    result.setValue(this);
    return result;
  }

  public Angle cloneRevert() {
    return new Angle().setValue(this).revert();
  }

  public Angle revert() {
    setValue(-value);
    return this;
  }

  public double radians() {
    return Math.toRadians(value);
  }

  public static Angle valueOf(String primitive) {
    Angle result = new Angle();
    double value = Double.parseDouble(primitive);
    result.setValue(value);
    return result;
  }

}
