package mario.objects.hero;

import featurea.app.Camera;
import featurea.app.Context;
import featurea.graphics.Graphics;
import featurea.motion.Rotation;
import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import mario.Assets;
import mario.Sprites;
import mario.objects.enemies.Enemy;
import mario.objects.enemies.PiranhaPlant;

import static mario.Sprites.Items.Fireball.*;

public class HeroFireball extends Body {

  private static final double VERTICAL_RATIO = 12.0 / 32.0;
  private static final double MAX_LIFT_HEIGHT = 18;
  private static final double MAX_HORIZONTAL_SHIFT = 8;
  private double currentHorizontalShit = 0;

  private boolean isFirsTimeHitSolid = true;
  private double liftHeight;

  private static final String[] deadSprite = {
      Sprites.Items.Fireball.Dead._2(), Sprites.Items.Fireball.Dead._3()
  };
  private boolean isRightBeforeDestroy;

  public HeroFireball() {
    add(new Rotation().setGraph(1000).setVelocity(1).setLoop(true));
    setFps(32);
    this.isCountinuesCollisionMode = true;
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    if (animation instanceof Body) {
      Body body = (Body) animation;
      if (!isDead()) {
        if (horizontal != null) {
          if (body.isSolid()) {
            destroy();
          }
        }
        if (vertical == Overlap.Y.bottom) {
          if (body.isSolid()) {
            if (isFirsTimeHitSolid) {
              isFirsTimeHitSolid = false;
              changeVelocityAfterFirstSoldHit();
            }
            liftHeight = 0;
            velocity.y = -Math.abs(velocity.y);
          }
        }
        if (animation instanceof Enemy) {
          destroy();
          Enemy enemy = (Enemy) body;
          enemy.destroyBecauseOfFire();
        }
        if (animation instanceof PiranhaPlant) {
          destroy();
          PiranhaPlant piranhaPlant = (PiranhaPlant) animation;
          piranhaPlant.removeSelf();
        }
      }
    }
  }

  private void changeVelocityAfterFirstSoldHit() {
    velocity.x *= 1;
    velocity.y = velocity.x * VERTICAL_RATIO;
  }

  @Override
  public void move(double dx, double dy, double dz) {
    if (dy < 0) {
      liftHeight += Math.abs(dy);
    }
    if (velocity.y < 0) {
      if (liftHeight >= MAX_LIFT_HEIGHT) {
        dy = 0;
        currentHorizontalShit += dx;
        System.out.println("currentHorizontalShit: " + currentHorizontalShit);
        if (currentHorizontalShit >= MAX_HORIZONTAL_SHIFT) {
          velocity.y = Math.abs(velocity.y);
        }
      }
    } else {
      currentHorizontalShit = 0;
    }
    super.move(dx, dy, dz);
  }

  @Override
  public void setDead(boolean isDead) {
    super.setDead(isDead);
    if (!isDead) {
      setSize(flyWidth, flyHeight);
      setRectangle(0, 0, flyWidth, flyHeight);
      setSprite(Sprites.Items.Fireball.fly());
    } else {
      setSize(deadWidth, deadHeight);
      /*setRectangle(0, 0, deadWidth, deadHeight);*/ // IMPORTANT
      sprite.reset();
      sprite.setLoop(false);
      sprite.setFile(deadSprite);
      sprite.stopListener = new Runnable() {
        @Override
        public void run() {
          removeSelf();
        }
      };
    }
  }

  private void onTickRemoveSelf() {
    Camera camera = getLayer().getCamera();
    if (top() > camera.bottom() || right() < camera.left()) {
      removeSelf();
    }
  }

  @Override
  public String toString() {
    return "HeroFireball";
  }

  @Override
  public void onMotionFinish() {
    super.onMotionFinish();
    onTickRemoveSelf();
  }

  @Override
  public void destroy() {
    if (!isDead()) {
      isRightBeforeDestroy = velocity.x > 0;
      Context.getAudio().play(Assets.Audio.Sounds.Bump);
    }
    super.destroy();
  }

  @Override
  protected void onDrawSpriteIfVisible(Graphics graphics) {
    if (!isDead()) {
      super.onDrawSpriteIfVisible(graphics);
    } else {
      double ox = isRightBeforeDestroy ? right() : left();
      double oy = oy();
      double x1 = ox - sprite.getWidth() / 2;
      double x2 = ox + sprite.getWidth() / 2;
      double y1 = oy - sprite.getHeight() / 2;
      double y2 = oy + sprite.getHeight() / 2;
      sprite.draw(graphics, x1, y1, x2, y2, ox, oy, angle, isFlipX(), isFlipY());
    }
  }

}
