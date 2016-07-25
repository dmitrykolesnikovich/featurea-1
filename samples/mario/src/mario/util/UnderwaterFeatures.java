package mario.util;

import featurea.app.Context;
import featurea.platformer.Animation;
import featurea.platformer.physics.Body;
import featurea.util.Color;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.background.Bubble;
import mario.objects.hero.World;

public class UnderwaterFeatures {

  public static UnderwaterFeatures instance = new UnderwaterFeatures();
  private static final Color underWaterColor = new Color().setValue(Gameplay.underwaterColor);
  private final Bubble bubble = new Bubble();
  private static final double WATER_LINE = 32;
  private static final double WAVE_LINE = WATER_LINE - Sprites.Background.waterHeight;

  public void onAttach(final World world) {
    world.getScreen().background = new Color().setValue(Gameplay.overworldColor);
    world.add(bubble);

    // bubble
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        Bubble.make(world.getHero());
        Context.getTimer().delay(this, Gameplay.bubblePeriod);
      }
    }, Gameplay.bubblePeriod);
  }

  public void onDraw(World world) {
    world.getGraphics().fillRectangle(0, WATER_LINE, world.getSize().width, 224, underWaterColor);
    for (Animation animation : world.getBodies()) {
      if (animation instanceof Body) {

      }
    }
  }

  public void onMotionFinish(World world) {
    for (Animation animation : world.getBodies()) {
      if (animation instanceof Body) {
        Body body = (Body) animation;
        if (!body.isSolid()) {
          if (body.top() < WAVE_LINE) {
            body.setTop(WAVE_LINE);
            body.velocity.y = 0;
          }
        }
      }
    }
  }

}
