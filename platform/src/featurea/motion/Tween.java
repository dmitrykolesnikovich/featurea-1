package featurea.motion;

public abstract class Tween {

  public abstract double f(double x);

  public static class ExponentialTween extends Tween {
    private double power;

    public ExponentialTween(double power) {
      this.power = power;
    }

    @Override
    public double f(double x) {
      return Math.pow(x, power);
    }
  }

}
