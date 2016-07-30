package featurea.platformer.util;

import featurea.app.Area;
import featurea.app.Context;
import featurea.app.Projection;
import featurea.platformer.Animation;
import featurea.platformer.physics.Body;
import featurea.platformer.physics.WorldLayer;

import java.util.ArrayList;
import java.util.List;

public class BufferedAreasAndBodiesProjection extends Projection<Area> {

  private WorldLayer worldLayer;
  private final List<Animation> bodies = new ArrayList<>();

  public BufferedAreasAndBodiesProjection(WorldLayer worldLayer) {
    this.worldLayer = worldLayer;
  }

  @Override
  public boolean onFilter(Area area) {
    if (!(area instanceof Animation)) {
      return true;
    }
    // >>
    // 1)
    final Animation animation = (Animation) area;
    animation.setLayer(worldLayer);
    boolean result = animation.shouldBeSwipedFromBuffer() || Context.isFeaturea();
    boolean isRemove = false;
    double lifeDistance = animation.getLifeDistance();
    if (Context.isProduction()) {
      if (lifeDistance != 0) {
        if (lifeDistance < worldLayer.getCamera().left()) {
          Context.getTimer().delay(new Runnable() {
            @Override
            public void run() {
              animation.removeSelf();
            }
          });
          isRemove = true;
        }
      }
    }
    if (!isRemove) {
      worldLayer.bodyIndex.update(animation);
      if (worldLayer.getCamera().left() > animation.right() || !animation.timeline.isEmpty()) {
        bodies.add(animation);
      } else {
        if (animation instanceof Body) {
          Body body = (Body) animation;
          if (!body.velocity.isEmpty() || body.isClimb() || (body.getMass() != 0 && body.getPlatform() == null)) {
            bodies.add(animation);
          }
        }
      }
    }
    return result;
  }

  @Override
  public void clear() {
    super.clear();
    bodies.clear();
  }

  public List<Animation> getBodies() {
    return bodies;
  }

}
