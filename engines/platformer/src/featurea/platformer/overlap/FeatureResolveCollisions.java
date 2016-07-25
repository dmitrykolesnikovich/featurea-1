package featurea.platformer.overlap;

import featurea.platformer.physics.WorldLayer;
import featurea.platformer.Animation;

public class FeatureResolveCollisions {

  public void make(WorldLayer world) {
    for (Animation body : world.overlapAnimations) {
      body.overlapManager.update();
    }
    world.overlapAnimations.clear();
  }

}
