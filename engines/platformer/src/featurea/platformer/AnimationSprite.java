package featurea.platformer;

import featurea.graphics.Sprite;
import featurea.platformer.physics.WorldLayer;

public class AnimationSprite extends Sprite {

  private final Animation animation;

  public AnimationSprite(Animation animation) {
    this.animation = animation;
  }

  @Override
  public void onTick(double elapsedTime) {
    WorldLayer layer = animation.getLayer();
    if (layer != null) {
      if (!layer.isTimeStop()) {
        int startIndex = sheetIndex;
        super.onTick(elapsedTime);
        int finishIndex = sheetIndex;
        if (startIndex != finishIndex) {
          animation.redraw();
        }
      }
    }
  }

}
