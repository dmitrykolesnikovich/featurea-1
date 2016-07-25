package featurea.platformer.overlap;

import featurea.platformer.Animation;

import java.util.ArrayList;
import java.util.List;

public class OverlapManager {

  private final Animation body;
  private final List<Overlap> prevOverlaps = new ArrayList<>();
  private final List<Overlap> currentOverlaps = new ArrayList<>();

  public OverlapManager(Animation body) {
    this.body = body;
  }

  public void addXOverlap(Animation body, Overlap.X x, double dx) {
    Overlap overlap = findOverlap(body);
    overlap.x = x;
    overlap.dx = dx;
  }

  public void addYOverlap(Animation body, Overlap.Y y, double dy) {
    Overlap overlap = findOverlap(body);
    overlap.y = y;
    overlap.dy = dy;
  }

  private Overlap findOverlap(Animation body) {
    for (Overlap overlap : currentOverlaps) {
      if (overlap.animation == body) {
        return overlap;
      }
    }
    Overlap overlap = new Overlap(body);
    currentOverlaps.add(overlap);
    return overlap;
  }

  public void update() {
    for (Overlap currentOverlap : currentOverlaps) {
      processOverlap(body, currentOverlap.animation, currentOverlap.x, currentOverlap.y, currentOverlap);
      processOverlap(currentOverlap.animation, body, Overlap.X.revert(currentOverlap.x), Overlap.Y.revert(currentOverlap.y), currentOverlap);
    }
    prevOverlaps.clear();
    prevOverlaps.addAll(currentOverlaps);
    for (Overlap overlap : prevOverlaps) {
      overlap.dx = 0;
      overlap.dy = 0;
    }
    currentOverlaps.clear();
  }

  private void processOverlap(Animation body, Animation animation, Overlap.X x, Overlap.Y y, Overlap currentOverlap) {
    if (body.isCountinuesCollisionMode) {
      body.onIntersect(animation, x, y);
    } else {
      if (!prevOverlaps.contains(currentOverlap)) {
        body.onIntersect(animation, x, y);
      }
    }
  }

}
