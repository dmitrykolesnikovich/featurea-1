package featurea.platformer.physics;

import featurea.platformer.Animation;

public class FeatureInvalidatePlatforms {

  public void make(WorldLayer world) {
    for (Animation animation : world.getBodies()) {
      if (animation instanceof Body) {
        invalidatePlatform((Body) animation);
      }
    }
  }

  public void invalidatePlatform(Body body) {
    if (body.hasPlatform()) {
      if (!body.isClimb() && (body.getPlatform().left() > body.right() || body.getPlatform().right() < body.left())) {
        body.setPlatform(null);
      }
    }
  }

}
