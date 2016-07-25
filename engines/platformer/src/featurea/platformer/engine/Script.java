package featurea.platformer.engine;

import featurea.platformer.overlap.FeatureHorizontalMotion;
import featurea.platformer.overlap.FeatureResolveCollisions;
import featurea.platformer.overlap.FeatureVerticalMotion;
import featurea.platformer.physics.FeatureMotionFinish;
import featurea.platformer.physics.FeatureInvalidatePlatforms;
import featurea.platformer.physics.WorldLayer;

public class Script {

  public final FeatureVerticalMotion featureVerticalMotion = new FeatureVerticalMotion();
  public final FeatureHorizontalMotion featureHorizontalMotion = new FeatureHorizontalMotion();
  public final FeatureResolveCollisions featureResolveCollisions = new FeatureResolveCollisions();
  private final FeatureInvalidatePlatforms featureInvalidatePlatforms = new FeatureInvalidatePlatforms();
  private final FeatureMotionFinish featureMotionFinish = new FeatureMotionFinish();

  public void make(WorldLayer world) {
    featureHorizontalMotion.make(world);
    featureInvalidatePlatforms.make(world);
    featureVerticalMotion.make(world);
    featureResolveCollisions.make(world);
  }

  public void make2(WorldLayer world){
    featureMotionFinish.make(world);
  }

}

