package mario.objects.enemies;

import featurea.app.Context;
import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import featurea.util.Vector;
import mario.Assets;
import mario.config.Gameplay;
import mario.objects.hero.World;

public class Enemy extends Body {

  private boolean isFirstTimeShow = true;
  private double currentDistance;
  private double walkDistance;
  private boolean isDeadBecauseOfFire;

  public Enemy() {
    setMass(1);
    setFps(6);
  }

  public boolean isDeadBecauseOfFire() {
    return isDeadBecauseOfFire;
  }

  public void destroyBecauseOfFire() {
    if (!isDeadBecauseOfFire) {
      isDeadBecauseOfFire = true;
      destroy();
    }
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    double horizontalVelocity = getHorizontalVelocity();
    if (horizontalVelocity != 0) {
      if (shouldBeSwipedFromBuffer()) {
        if (isFirstTimeShow) {
          isFirstTimeShow = false;
          if (position.x - getLayer().getCamera().right() < 16) {
            setVelocity(new Vector((walkDistance >= 0 ? 1 : -1) * -horizontalVelocity, 0)); // todo fix this shit
          }
        }
      }
    }
  }

  @Override
  public double getHorizontalVelocity() {
    return Gameplay.enemyStep;
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    super.onIntersect(animation, horizontal, vertical);
    if (animation instanceof Body) {
      Body body = (Body) animation;
      if (animation instanceof KoopaTroopa) {
        KoopaTroopa koopaTroopa = (KoopaTroopa) animation;
        if (koopaTroopa.isRun()) {
          destroyBecauseOfFire();
        }
      }
      if (horizontal != null) {
        if (body.isSolid()) {
          Body.bounce(this, body);
        } else {
          if (body instanceof Enemy) {
            Enemy enemy = (Enemy) body;
            if (enemy.isBounce() && isBounce()) {
              Enemy.bounce(this, body);
            }
          }
        }
      }
    }
  }

  public void setWalkDistance(double walkDistance) {
    this.walkDistance = walkDistance;
  }

  @Override
  public void onMotionFinish() {
    if (walkDistance != 0 && !isDead()) {
      currentDistance += Math.abs(moveVector.x);
      if (currentDistance > Math.abs(walkDistance)) {
        currentDistance = 0;
        velocity.x *= -1;
      }
    }
    if (top() > getLayer().getCamera().bottom()) {
      removeSelf();
    }
    super.onMotionFinish();
  }

  private boolean isBounce() {
    return !isDead();
  }

  public World getWorld() {
    return (World) getLayer();
  }

  @Override
  public void destroy() {
    super.destroy();
    Context.getAudio().play(Assets.Audio.Sounds.Kick);
  }

  @Override
  public void setPlatform(Body platform) {
    if (!isDeadBecauseOfFire()) {
      super.setPlatform(platform);
    }
  }

}
