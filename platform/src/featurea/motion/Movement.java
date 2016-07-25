package featurea.motion;

import featurea.util.TransformMove;
import featurea.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Movement extends Motion {

  private List<Vector> vectors;
  private double[] deltas;
  private double range;

  public Movement setGraph(double... points) {
    if (vectors == null) {
      vectors = new ArrayList<>();
    }
    vectors.clear();
    deltas = new double[points.length / 3];
    for (int i = 0; i < points.length; i += 3) {
      double x = points[i];
      double y = points[i + 1];
      double z = points[i + 2];
      Vector delta = new Vector(x, y, z);
      vectors.add(delta);
      double deltaLength = delta.length();
      range += deltaLength;
      deltas[i / 3] = deltaLength;
    }
    return this;
  }

  @Override
  public double range() {
    return range;
  }

  @Override
  public void step(double from, double to) {
    double[] array = compute(from, to);
    int index1 = (int) array[0];
    int index2 = (int) array[1];
    double progress1 = array[2];
    double progress2 = array[3];
    double dx = 0, dy = 0, dz = 0;
    if (index1 == index2) {
      double progress = (to - from) / deltas[index1];
      Vector vector = vectors.get(index1);
      dx = vector.x * progress;
      dy = vector.y * progress;
      dz = vector.z * progress;
    } else {
      {
        Vector vector = vectors.get(index1);
        dx += vector.x * progress1;
        dy += vector.y * progress1;
        dz += vector.z * progress1;
      }
      {
        for (int i = index1 + 1; i <= index2 - 1; i++) {
          Vector vector = vectors.get(i);
          dx += vector.x;
          dy += vector.y;
          dz += vector.z;
        }
      }
      {
        try {
          Vector vector = vectors.get(index2);
          dx += vector.x * progress2;
          dy += vector.y * progress2;
          dz += vector.z * progress2;
        } catch (ArrayIndexOutOfBoundsException e) {
          e.printStackTrace();
        }
      }
    }
    if (timeline != null && timeline.transform instanceof TransformMove) {
      TransformMove transformMove = (TransformMove) timeline.transform;
      transformMove.move(dx, dy, dz);
      this.onMove(dx, dy, dz);
    }
  }

  private final double[] computeResult = new double[4];

  private double[] compute(double from, double to) {
    int index1 = -1;
    int index2 = -1;
    double progress1 = 0;
    double progress2 = 0;
    double delta = 0;
    for (int i = 0; i < deltas.length; i++) {
      delta += deltas[i];
      if (index1 == -1 && delta >= from) {
        index1 = i;
        progress1 = 1 - (delta - from) / deltas[i];
      }
      if (index2 == -1 && delta >= to) {
        index2 = i;
        progress2 = 1 - (delta - to) / deltas[i];
      }
      if (index1 != -1 && index2 != -1) {
        break;
      }
    }
    if (index2 > index1) {
      progress1 = 1 - progress1;
    } else {
      progress2 = 1 - progress2;
    }
    computeResult[0] = index1;
    computeResult[1] = index2;
    computeResult[2] = progress1;
    computeResult[3] = progress2;
    return computeResult;
  }

  protected void onMove(double dx, double dy, double dz){

  }

}
