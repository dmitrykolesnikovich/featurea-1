package mario.objects.enemies;

import featurea.app.Context;
import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.hero.Hero;
import mario.objects.hero.World;

public class Octopus extends Body {

  private static final double width = Sprites.Enemies.octopusWidth;
  private static final double height = Sprites.Enemies.octopusHeight;

  public Octopus() {
    setSize(width, height);
    setRectangle(0, 0, width, height);
    setMass(0.2);
  }

  private void setupMotion() {
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        setSprite(Sprites.Enemies.Octopus._1());
        Context.getTimer().delay(new Runnable() {
          @Override
          public void run() {
            Hero hero = getHero();
            double dh = oy() - hero.oy();
            if (dh >= -20) {
              swim();
              if (World.isHeroLefter(Octopus.this)) {
                velocity.x = -20;
              } else {
                velocity.x = 20;
              }
            } else {
              setSprite(Sprites.Enemies.Octopus._0());
            }
            setupMotion();
          }
        }, Gameplay.octopusPrepareSwimPeriod);
      }
    }, Gameplay.octopusPerformSwimPeriod);
  }

  @Override
  public Animation build() {
    super.build();
    setSprite(Sprites.Enemies.Octopus._0());
    if (!Context.isFeaturea()) {
      setupMotion();
    }
    return this;
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    super.onIntersect(animation, horizontal, vertical);
    if (animation instanceof Hero) {
      Hero hero = (Hero) animation;
      hero.damage();
    }
  }

  private World getWorld() {
    return (World) getLayer();
  }

  private Hero getHero() {
    return getWorld().getHero();
  }

  @Override
  public void swim() {
    super.swim();
    setSprite(Sprites.Enemies.Octopus._0());
  }

}
