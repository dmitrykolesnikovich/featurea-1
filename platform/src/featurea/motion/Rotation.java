package featurea.motion;

import featurea.util.Angle;
import featurea.util.TransformRotate;

public class Rotation extends Motion {

  private double[] angles;
  private double range;

  public Rotation setGraph(double... points) {
    this.angles = points;
    for (double angle : angles) {
      range += Math.abs(angle);
    }
    return this;
  }

  @Override
  public double range() {
    return range;
  }

  private final Angle stepAngle = new Angle();

  @Override
  public void step(double r1, double r2) {
    double result = 0;
    int index1 = -1;
    int index2 = -1;
    double progress1 = 0;
    double progress2 = 0;
    double delta = 0;
    for (int i = 0; i < angles.length; i++) {
      double angle = Math.abs(angles[i]);
      delta += angle;
      if (index1 == -1 && delta >= r1) {
        index1 = i;
        progress1 = 1 - (delta - r1) / angle;
      }
      if (index2 == -1 && delta >= r2) {
        index2 = i;
        progress2 = 1 - (delta - r2) / angle;
      }
      if (index1 != -1 && index2 != -1) {
        break;
      }
    }
    if (index1 < index2) {
      progress1 = 1 - progress1;
    } else {
      progress2 = 1 - progress2;
    }
    if (index1 == index2) {
      double angle = Math.abs(angles[index1]);
      double progress = (r2 - r1) / angle;
      result = angles[index1] * progress;
    } else {
      {
        result += angles[index1] * progress1;
      }
      {
        for (int i = index1 + 1; i <= index2 - 1; i++) {
          result += angles[i];
        }
      }
      {
        result += angles[index2] * progress2;
      }
    }
    stepAngle.setValue(result);
    if (timeline != null && timeline.transform instanceof TransformRotate) {
      TransformRotate rotate = (TransformRotate) timeline.transform;
      rotate.rotate(stepAngle);
    }
  }

}
