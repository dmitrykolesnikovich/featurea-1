package featurea.platformer.overlap;

import featurea.platformer.Animation;

public class Overlap {

  public final Animation animation;
  public X x;
  public Y y;
  public double dx;
  public double dy;

  public Overlap(Animation animation) {
    this.animation = animation;
  }

  public enum X {
    left, right;

    public static X revert(X x) {
      if (x == null) {
        return null;
      }
      switch (x) {
        case left:
          return right;
        case right:
          return left;
      }
      throw new IllegalStateException();
    }
  }

  public enum Y {
    top, bottom;

    public static Y revert(Y y) {
      if (y == null) {
        return null;
      }
      switch (y) {
        case top:
          return bottom;
        case bottom:
          return top;
      }
      throw new IllegalStateException();
    }
  }

}
