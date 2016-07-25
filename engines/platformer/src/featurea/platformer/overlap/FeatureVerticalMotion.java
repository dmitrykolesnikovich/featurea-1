package featurea.platformer.overlap;

import featurea.platformer.Animation;
import featurea.platformer.CollisionResult;
import featurea.platformer.physics.Body;
import featurea.platformer.physics.WorldLayer;
import featurea.util.Vector;

import java.util.List;

public class FeatureVerticalMotion {

  public void make(WorldLayer world) {
    List<Animation> animations = world.getBodies();
    for (Animation animation : animations) {
      makeBody(animation);
    }
  }

  public void makeBody(Animation animation) {
    if (animation.isMotionableVertically()) {
      animation.onMotionVerticallyBefore();
      moveByY(animation, animation.moveVector.y);
    }
  }

  public void moveByY(Animation animation, double dy) {
    if (dy != 0) {
      animation.moveNow(0, dy);
      resolveVerticalCollisionsRecursively(animation);
    }
  }

  private void resolveVerticalCollisionsRecursively(Animation animation) {
    WorldLayer world = animation.getLayer();
    Iterable<? extends Animation> bodies = world.getBodies(animation);
    for (Animation target : bodies) {
      if (animation != target) {
        if (world.getCollisionFilter().shouldDetectIntersection(animation, target)) {
          resolveShiftVertical(target, animation);
        }
      }
    }
  }

  public static void resolveShiftVertical(Animation target, Animation animation) {
    CollisionResult result = null;
    if (target instanceof Body && animation instanceof Body) {
      Body body = (Body) animation;
      Body targetBody = (Body) target;
      result = body.getCollisionEffect(targetBody);
      if (result.isReverse() || !targetBody.isMotionableVertically()) {
        Animation temp = target;
        target = animation;
        animation = temp;
      }
    }

    Vector overlap = target.getOverlap(animation);
    if (overlap.y != 0) {
      WorldLayer world = animation.getLayer();
      world.overlapAnimations.add(target);
      if (overlap.y > 0) {
        target.overlapManager.addYOverlap(animation, Overlap.Y.top, overlap.y);
      } else if (overlap.y < 0) {
        target.overlapManager.addYOverlap(animation, Overlap.Y.bottom, overlap.y);
      }

      if (result != null && result.isSolid()) {
        target.moveNow(0, overlap.y);
      }
    }
  }

}
