package featurea.platformer.util;

import com.sun.istack.internal.Nullable;
import featurea.platformer.Animation;
import featurea.platformer.physics.WorldLayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BodyIndex {

  private int step;

  private final WorldLayer worldLayer;
  private final Map<Point, Set<Animation>> point2animations = new HashMap<>();
  private final Map<Animation, Set<Point>> animation2points = new HashMap<>();

  public BodyIndex(WorldLayer worldLayer) {
    this.worldLayer = worldLayer;
  }

  public BodyIndex setStep(int step) {
    this.step = step;
    return this;
  }

  // >> temps vars do not reuse
  private final Set<Animation> result = new HashSet<>();
  // <<

  public Iterable<Animation> getBodies(Animation animation) {
    result.clear();
    Set<Point> points = animation2points.get(animation);
    if (points != null) {
      for (Point point : points) {
        Set<Animation> animations = point2animations.get(point);
        if (animations != null) {
          result.addAll(animations);
        }
      }
    } else {
      // if animation has no rectangle
    }
    return result;
  }

  @Nullable
  public Set<Animation> getAnimations(double x, double y) {
    int keyX = getKey(x);
    int keyY = getKey(y);
    return point2animations.get(new Point(keyX, keyY));
  }

  public void update(Animation animation) {
    if (animation.isDirty) {
      animation.isDirty = false;
      clear(animation);
      put(animation);
    }
  }

  public void clear(Animation animation) {
    Set<Point> points = animation2points.get(animation);
    if (points != null) {
      for (Point point : points) {
        Set<Animation> animations = point2animations.get(point);
        if (animations != null) {
          animations.remove(animation);
        }
      }
      points.clear();
    }
  }

  private void put(Animation animation) {
    for (int x = getKey(animation.left()); x < animation.right(); x += step) {
      for (int y = getKey(animation.top()); y < animation.bottom(); y += step) {
        Point point = new Point(x, y);

        // animations
        Set<Animation> animations = point2animations.get(point);
        if (animations == null) {
          animations = new HashSet<>();
          point2animations.put(point, animations);
        }
        animations.add(animation);

        // points
        Set<Point> points = animation2points.get(animation);
        if (points == null) {
          points = new HashSet<>();
          animation2points.put(animation, points);
        }
        points.add(point);
      }
    }
  }

  private int getKey(double value) {
    value = ((int) (value / step)) * step;
    return (int) value;
  }

  private static class Point {

    public final int x;
    public final int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Point) {
        Point point = (Point) obj;
        return point.x == x && point.y == y;
      }
      return super.equals(obj);
    }

    @Override
    public int hashCode() {
      return x * y;
    }

    @Override
    public String toString() {
      return x + ", " + y;
    }
  }

}
