package mario.objects.enemies;

import featurea.app.Context;
import featurea.graphics.Graphics;
import featurea.motion.Movement;
import featurea.platformer.physics.Body;
import featurea.util.Colors;
import mario.Sprites;
import mario.config.Gameplay;

public class Cannon extends Body {

  private double currentFireTime;

  private static final double width = Sprites.Enemies.Cannon.cannonWidth;
  private static final double height = Sprites.Enemies.Cannon.cannonHeight;

  private static final double baseWidth = Sprites.Enemies.Cannon.baseWidth;
  private static final double baseHeight = Sprites.Enemies.Cannon.baseHeight;

  private boolean hasBase;

  public Cannon() {
    setSolid(true);
    setSize(width, height);
    if (hasBase) {
      setRectangle(0, 0, width, height + baseHeight);
    } else {
      setRectangle(0, 0, width, height);
    }
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Enemies.Cannon.cannon();
  }

  public boolean hasBase() {
    return hasBase;
  }

  public void setBase(boolean hasBase) {
    this.hasBase = hasBase;
  }

  @Override
  public void onDrawSpriteIfVisible(Graphics graphics) {
    super.onDrawSpriteIfVisible(graphics);
    if (!graphics.containsDrawTexture()) {
      if (hasBase) {
        double x1 = position.x;
        double x2 = position.x + width;
        double y1 = position.y + height;
        double y2 = y1 + baseHeight;
        graphics.drawTexture(Sprites.Enemies.Cannon.base(), x1, y1, x2, y2, null, 0, 0, Colors.white, false, false);
      }
    }
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    currentFireTime += elapsedTime;
    if (currentFireTime > Gameplay.firePeriod) {
      currentFireTime %= Gameplay.firePeriod;
      fire();
    }
  }

  public void fire() {
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        CannonBullet bullet = new CannonBullet();
        bullet.setPosition(position.x - width(), position.y);
        bullet.timeline.add(new Movement().setGraph(-1, 0, 0).setVelocity(0.1).setLoop(true));
        bullet.build();
        getLayer().add(bullet);
      }
    });
  }

}
