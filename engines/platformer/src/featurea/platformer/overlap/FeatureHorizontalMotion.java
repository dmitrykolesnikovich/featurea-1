package featurea.platformer.overlap;

import featurea.platformer.Animation;
import featurea.platformer.CollisionResult;
import featurea.platformer.physics.Body;
import featurea.platformer.physics.WorldLayer;
import featurea.util.Vector;

import java.util.List;

public class FeatureHorizontalMotion {

  public void make(WorldLayer world) {
    List<Animation> animations = world.getBodies();
    for (Animation animation : animations) {
      if (animation.isMotionableHorizontally()) {
        animation.onMotionHorizontallyBefore();
        moveByX(animation, animation.moveVector.x);
      }
    }
  }

  public void moveByX(Animation animation, double dx) {
    if (dx != 0) {
      animation.moveNow(dx, 0);
      resolveHorizontalCollisionsRecursively(animation);
    }
  }

  public void resolveHorizontalCollisionsRecursively(Animation animation) {
    WorldLayer world = animation.getLayer();
    for (Animation target : world.getBodies(animation)) {
      if (animation != target) {
        if (world.getCollisionFilter().shouldDetectIntersection(animation, target)) {
          resolveShiftHorizontal(target, animation);
        }
      }
    }
  }

  private void resolveShiftHorizontal(Animation target, Animation animation) {
    CollisionResult result = null;

    if (target instanceof Body && animation instanceof Body) {
      Body body = (Body) animation;
      Body targetBody = (Body) target;
      result = body.getCollisionEffect(targetBody);
      if (result.isReverse() || !targetBody.isMotionableHorizontally()) {
        Animation temp = target;
        target = animation;
        animation = temp;
      }
    }

    Vector overlap = target.getOverlap(animation);
    if (overlap.x != 0) {
      // collision
      WorldLayer world = animation.getLayer();
      world.overlapAnimations.add(target);
      if (overlap.x > 0) {
        target.overlapManager.addXOverlap(animation, Overlap.X.left, overlap.x);
      } else if (overlap.x < 0) {
        target.overlapManager.addXOverlap(animation, Overlap.X.right, overlap.x);
      }
      if (result != null && result.isSolid()) {
        Body targetBody = (Body) target;
        Body body = (Body) animation;
        if (targetBody.getPlatform() != body) {
          target.moveNow(overlap.x, 0);
        }
      }
    }
  }

}
