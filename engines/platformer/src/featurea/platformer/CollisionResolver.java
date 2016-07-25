package featurea.platformer;

import featurea.platformer.physics.Body;

public class CollisionResolver {

  public boolean shouldDetectIntersection(Animation body1, Animation body2) {
    return true;
  }

  public final CollisionResult collide(Body body, Body target) {
    if (target.isSolid()) {
      return CollisionResult.REVERSE;
    }
    if (body.isSolid()) {
      return CollisionResult.SHIFTS_TARGET;
    }
    return CollisionResult.NULL;
  }

}
