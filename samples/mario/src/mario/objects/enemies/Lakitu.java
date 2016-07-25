package mario.objects.enemies;

import featurea.app.Context;
import featurea.platformer.physics.Body;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.hero.Hero;
import mario.objects.hero.World;

public class Lakitu extends Body {

  private static final double width = Sprites.Enemies.lakituWidth;
  private static final double height = Sprites.Enemies.lakituHeight;
  private double currentFireTime;
  private boolean isFire;

  public Lakitu() {
    setSprite(Sprites.Enemies.Lakitu._0());
    setSize(width, height);
    setRectangle(0, 0, width, height);
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    onTickPosition();
    onTickFire(elapsedTime);
  }

  private void onTickFire(double elapsedTime) {
    if (!isFire) {
      currentFireTime += elapsedTime;
      if (currentFireTime > Gameplay.lakituFirePeriod) {
        isFire = true;
        setSprite(Sprites.Enemies.Lakitu._1());
        currentFireTime %= Gameplay.lakituFirePeriod;
        Context.getTimer().delay(new Runnable() {
          @Override
          public void run() {
            setSprite(Sprites.Enemies.Lakitu._0());
            isFire = false;
            Spiny spiny = new Spiny();
            spiny.setPosition(position);
            spiny.position.y -= spiny.height();
            spiny.velocity.x = 2;
            spiny.velocity.y = 20;
            getWorld().add(spiny);
          }
        }, Gameplay.lakituFireWaitPeriod);
      }
    }

  }

  private void onTickPosition() {
    if (!isFire) {
      Hero hero = getWorld().getHero();
      if (Math.abs(hero.position.x - position.x) > width * 2) {
        double sign = Math.signum(hero.position.x - position.x);
        velocity.x = Gameplay.lakituVelocity * sign;
      } else {
        velocity.x = 0;
      }
    }
  }

  private World getWorld() {
    return (World) super.getLayer();
  }

}
