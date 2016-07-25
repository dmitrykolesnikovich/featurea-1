package featurea.motion;

import featurea.util.TransformScale;

public class Pulse extends Motion {

  private double[] scales;
  private double range;

  public Pulse setGraph(double... points) {
    scales = new double[points.length];
    for (int i = 0; i < points.length; i++) {
      double scale = points[i];
      if (scale < 1) {
        scale = -1 / scale;
      }
      scales[i] = scale;
      range += Math.abs(scale);
    }
    return this;
  }

  @Override
  public double range() {
    return range;
  }

  @Override
  public void step(double r1, double r2) {
    final double[] array = compute(r1, r2);
    final int index1 = (int) array[0];
    final int index2 = (int) array[1];
    final double progress1 = array[2];
    final double progress2 = array[3];
    final double marker1 = array[4];
    final double marker2 = array[5];
    double result = 1;
    if (index1 == index2) {
      double scale = scales[index1];
      double delta = Math.abs(scale);
      double exp1 = marker1 / delta;
      double exp2 = marker2 / delta;
      double k = Math.pow(delta, exp2) / Math.pow(delta, exp1);
      if (scale < 0) {
        k = 1 / k;
      }
      result *= k;
    } else {
      {
        double scale = scales[index1];
        double delta = Math.abs(scale);
        double exp1 = marker1 / delta;
        double exp2 = 1;
        double k = Math.pow(delta, exp2) / Math.pow(delta, exp1);
        if (scale < 0) {
          k = 1 / k;
        }
        result *= k;
      }
      {
        for (int i = index1 + 1; i <= index2 - 1; i++) {
          double scale = scales[i];
          double delta = Math.abs(scale);
          double exp1 = 0;
          double exp2 = 1;
          double k = Math.pow(delta, exp2) / Math.pow(delta, exp1);
          if (scale < 0) {
            k = 1 / k;
          }
          result *= k;
        }
      }
      try {
        {
          double scale = scales[index2];
          double delta = Math.abs(scale);
          double exp1 = 0;
          double exp2 = marker2 / delta;
          double k = Math.pow(delta, exp2) / Math.pow(delta, exp1);
          if (scale < 0) {
            k = 1 / k;
          }
          result *= k;
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        e.printStackTrace();
      }
    }
    if (timeline != null && timeline.transform instanceof TransformScale) {
      TransformScale transformScale = (TransformScale) timeline.transform;
      transformScale.scaleX(result);
      transformScale.scaleY(result);
    }
  }

  private final double[] computeResult = new double[6];

  private double[] compute(double r1, double r2) {
    int index1 = -1;
    int index2 = -1;
    double progress1 = 0;
    double progress2 = 0;
    double marker1 = 0;
    double marker2 = 0;
    double delta = 0;
    for (int i = 0; i < scales.length; i++) {
      double scale = Math.abs(scales[i]);
      delta += scale;
      if (index1 == -1 && delta >= r1) {
        index1 = i;
        marker1 = scale - (delta - r1);
        progress1 = marker1 / scale;
      }
      if (index2 == -1 && delta >= r2) {
        index2 = i;
        marker2 = scale - (delta - r2);
        progress2 = marker2 / scale;
      }
      if (index1 != -1 && index2 != -1) {
        if (index2 > index1) {
          progress1 = 1 - progress1;
        } else {
          progress2 = 1 - progress2;
        }
      }
    }
    computeResult[0] = index1;
    computeResult[1] = index2;
    computeResult[2] = progress1;
    computeResult[3] = progress2;
    computeResult[4] = marker1;
    computeResult[5] = marker2;
    return computeResult;
  }
}
