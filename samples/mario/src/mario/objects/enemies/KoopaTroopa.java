package mario.objects.enemies;

import featurea.app.Context;
import featurea.motion.Movement;
import featurea.motion.Tweens;
import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import featurea.platformer.util.Rectangle;
import mario.Assets;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.hero.Hero;
import mario.objects.hero.World;
import mario.objects.landscape.GiantMushroom;

import static mario.Sprites.Enemies.KoopaTroopa.*;

public class KoopaTroopa extends Enemy {

  private boolean isRun;

  public enum Fly {
    horizontal, vertical
  }

  public enum ColorScheme {
    red,
  }

  private static final Rectangle walkRectangle = new Rectangle(0, 0, walkWidth, walkHeight);
  private static final Rectangle deadRectangle = new Rectangle(0, 0, deadWidth, deadHeight);
  private Fly fly;
  private boolean isWaitJump;
  private boolean isWalkOnGiantMushroom;
  private ColorScheme colorScheme;

  private final Movement verticalBounce = (Movement) new Movement().
      setGraph(0, Gameplay.koopaTroopaFlyVerticalVelocity, 0).setVelocity(Gameplay.koopaTroopaVelocity).setBounce(true).setTween(Tweens.square);

  public ColorScheme getColorScheme() {
    return colorScheme;
  }

  public void setColorScheme(ColorScheme colorScheme) {
    this.colorScheme = colorScheme;
  }

  public void setFly(Fly fly) {
    this.fly = fly;
    if (fly == Fly.vertical) {
      setMass(0);
    } else {
      setMass(1);
    }
    if (getMass() == 0) {
      if (!timeline.contains(verticalBounce)) {
        timeline.add(verticalBounce);
      }
    } else {
      if (timeline.contains(verticalBounce)) {
        timeline.remove(verticalBounce);
      }
    }
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    if (isFly()) {
      switch (fly) {
        case horizontal: {
          if (hasPlatform()) {
            if (!isWaitJump) {
              isWaitJump = true;
              Context.getTimer().delay(new Runnable() {
                @Override
                public void run() {
                  if (isFly()) {
                    jump();
                  }
                  isWaitJump = false;
                }
              }, Gameplay.koopaTroopaWaitJumpTime);
            }
          }
          break;
        }
        case vertical: {

          break;
        }
      }
    }
    if (!isDead()) {
      if (isWalkOnGiantMushroom) {
        Body platform = getPlatform();
        if (platform instanceof GiantMushroom) {
          if (velocity.x < 0) {
            if (left() - platform.left() < 2) {
              velocity.x *= -1;
            }
          }
          if (velocity.x > 0) {
            if (platform.right() - right() < 2) {
              velocity.x *= -1;
            }
          }
        }
      }
    }
  }

  @Override
  public String onUpdateSprite() {
    if (getColorScheme() == null) {
      if (isDead()) {
        return Sprites.Enemies.KoopaTroopa.dead();
      } else {
        if (isFly()) {
          return Sprites.Enemies.KoopaTroopaFly.walk();
        } else {
          return Sprites.Enemies.KoopaTroopa.walk();
        }
      }
    } else if (getColorScheme() == ColorScheme.red) {
      if (isDead()) {
        return Sprites.Enemies.KoopaTroopaRed.dead();
      } else {
        if (isFly()) {
          return Sprites.Enemies.KoopaTroopaFlyRed.walk();
        } else {
          return Sprites.Enemies.KoopaTroopaRed.walk();
        }
      }
    } else {
      throw new IllegalStateException("colorScheme = " + getColorScheme());
    }
  }

  @Override
  public void setDead(boolean isDead) {
    super.setDead(isDead);
    if (isDead) {
      double bottom = bottom();
      setRectangle(deadRectangle);
      setBottom(bottom);
      setSize(deadWidth, deadHeight);
    } else {
      setRectangle(walkRectangle);
      setSize(walkWidth, walkHeight);
    }
  }

  private boolean isFly() {
    return fly != null;
  }

  @Override
  public double getHorizontalVelocity() {
    return getMass() == 0 ? 0 : super.getHorizontalVelocity();
  }

  public void setWalkOnGiantMushroom(boolean isWalkOnGiantMushroom) {
    this.isWalkOnGiantMushroom = isWalkOnGiantMushroom;
  }

  public boolean tryDamage(Hero hero, Overlap.X horizontal, Overlap.Y vertical) {
    if (!isDead()) {
      if (hero.isStrongerThenEnemy()) {
        if (isFly()) {
          setFly(null);
          hero.smallJump();
          velocity.y = 0;
          return false;
        } else {
          hero.smallJump();
          destroy();
          return false;
        }
      }
    } else {
      if (velocity.x == 0) {
        if (hero.velocity.y >= 0) {
          double k = World.isHeroLefter(this) ? 1 : -1;
          velocity.x = k * Gameplay.koopaTroopaSliceVelocity;
          setRun(true);
        }
        return false;
      } else {
        if (hero.hasPlatform()) {
          if (World.isHeroLefter(this)) {
            return velocity.x < 0;
          } else {
            return velocity.x > 0;
          }
        } else if (hero.velocity.y >= 0) {
          return false;
        }
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
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    super.onIntersect(animation, horizontal, vertical);
    if (isDead()) {
      if (animation instanceof Body) {
        Body body = (Body) animation;
        if (horizontal != null && body.isSolid()) {
          Context.getAudio().play(Assets.Audio.Sounds.Bump);
        }
      }
    }
  }

  public boolean isRun() {
    return isRun;
  }

  public void setRun(boolean run) {
    isRun = run;
  }

}
