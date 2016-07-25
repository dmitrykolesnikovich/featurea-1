package featurea.platformer;

import featurea.graphics.Sprite;

public class AnimationSprite extends Sprite {

  private final Animation animation;

  public AnimationSprite(Animation animation) {
    this.animation = animation;
  }

  @Override
  public void onTick(double elapsedTime) {
    if (!animation.getLayer().isTimeStop()) {
      super.onTick(elapsedTime);
    }
  }

}
