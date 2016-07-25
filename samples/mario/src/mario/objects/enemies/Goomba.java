package mario.objects.enemies;

import featurea.app.Context;
import featurea.graphics.Graphics;
import featurea.graphics.Sprite;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.util.Rectangle;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.hero.Hero;
import mario.objects.hero.World;

import static mario.Sprites.Enemies.Goomba.*;

public class Goomba extends Enemy {

  private static final Rectangle walkRectangle = new Rectangle(-1, 0, walkWidth + 1, walkHeight);
  private static final Rectangle deadRectangle = new Rectangle(0, 0, deadWidth, deadHeight);

  @Override
  public String onUpdateSprite() {
    if (isDead() && !isDeadBecauseOfFire()) {
      return Sprites.Enemies.Goomba.dead();
    } else {
      return Sprites.Enemies.Goomba.walk();
    }
  }

  @Override
  public void setDead(boolean isDead) {
    super.setDead(isDead);
    if (isDead && !isDeadBecauseOfFire()) {
      double bottom = bottom();
      setRectangle(deadRectangle);
      setBottom(bottom);
      setSize(deadWidth, deadHeight);
    } else {
      setRectangle(walkRectangle);
      setSize(walkWidth, walkHeight);
    }
  }

  public boolean tryDamage(Hero hero, Overlap.X horizontal, Overlap.Y vertical) {
    if (isDead()) {
      return false;
    } else {
      if (hero.isStrongerThenEnemy()) {
        hero.smallJump();
        destroy();
        Context.getTimer().delay(new Runnable() {
          @Override
          public void run() {
            removeSelf();
          }
        }, Gameplay.goombaDeathPeriod);
        return false;
      }
    }
    return true;
  }

  @Override
  public void destroyBecauseOfFire() {
    super.destroyBecauseOfFire();
    setAngle(180);
    setMass(1.4);
    smallJump();
    velocity.x = (World.isHeroLefter(this) ? 1 : -1) * 60;
  }

  @Override
  public String toString() {
    return "Goomba";
  }

  @Override
  protected void onDrawSpriteIfVisible(Graphics graphics) {
    super.onDrawSpriteIfVisible(graphics);
  }

}
