package featurea.motion;

import featurea.util.MathUtil;

public class Tweens {

  public static final Tween linear = new Tween() {
    @Override
    public double f(double x) {
      return x;
    }
  };
  public static final Tween cos = new Tween() {
    @Override
    public double f(double x) {
      return -Math.cos(x * MathUtil.HALF_PI);
    }
  };
  public static final Tween sin = new Tween() {
    @Override
    public double f(double x) {
      return Math.sin(x * MathUtil.HALF_PI);
    }
  };
  public static final Tween square = new Tween.ExponentialTween(2);

  public static Tween exp(double power) {
    return new Tween.ExponentialTween(power);
  }

}
