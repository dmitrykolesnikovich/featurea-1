package mario.objects.landscape;

import featurea.graphics.Graphics;
import featurea.platformer.Animation;
import featurea.platformer.config.Engine;
import featurea.util.Color;
import mario.config.Gameplay;

public class Rope extends Animation {

  private static final Color ropeColor = new Color().setValue(Gameplay.ropeColor);

  public Rope() {
    setRectangle(-1, 0, 1, Engine.cameraHeight - Gameplay.hudHeight);
  }

  @Override
  protected void onDrawSpriteIfVisible(Graphics graphics) {
    graphics.fillRectangle(ox() - 1, Gameplay.hudHeight, ox() + 1, graphics.layer.getCamera().height(), ropeColor);
  }

  @Override
  public Animation setPosition(double x, double y, double z) {
    return super.setPosition(x, Gameplay.hudHeight, z);
  }

}
