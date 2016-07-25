package featurea.platformer.physics;

import com.sun.istack.internal.Nullable;
import featurea.app.Camera;
import featurea.app.Context;
import featurea.platformer.Animation;
import featurea.platformer.CollisionResult;
import featurea.platformer.config.Engine;
import featurea.platformer.overlap.Overlap;
import featurea.util.Colors;
import featurea.util.Selection;
import featurea.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Body extends Animation {

  private static final double SWIPE_TOLERANCE = 2;
  /*private*/ final MotionMath model = new MotionMath(this);

  /*private*/ boolean isJump;
  /*private*/ boolean isClimb;
  private double mass;
  private Body platform;
  private boolean isSolid;
  private boolean isLadder;
  private final List<Body> loads = new ArrayList<>();
  private boolean isDead;

  public Body() {
    setDead(false);
  }

  @Override
  public double getLifeDistance() {
    double lifeDistance = super.getLifeDistance();
    if (lifeDistance != 0) {
      return lifeDistance;
    } else {
      if (!isMotionableHorizontally()) {
        return right() + 2;
      }
    }
    return 0;
  }

  public CollisionResult getCollisionEffect(Body target) {
    return getLayer().getCollisionFilter().collide(this, target);
  }

  public void setSolid(boolean isWall) {
    this.isSolid = isWall;
  }

  public boolean isSolid() {
    return isSolid;
  }

  public void setLadder(boolean isLadder) {
    this.isLadder = isLadder;
  }

  public boolean isLadder() {
    return isLadder;
  }

  public double getMass() {
    return mass;
  }

  public void setMass(double mass) {
    this.mass = mass;
  }

  @Override
  public boolean shouldBeSwipedFromBuffer() {
    if (Context.getRender().isScreenMode) {
      return true;
    }
    Camera camera = getLayer().getCamera();
    return camera.left() - SWIPE_TOLERANCE < right() && camera.right() + SWIPE_TOLERANCE > left();
  }


  public List<Body> getLoads() {
    return loads;
  }

  public void setVelocity(Vector velocity) {
    this.velocity.setValue(velocity);
  }

  public void setPlatform(Body platform) {
    if (this.platform != null) {
      this.platform.loads.remove(this);
    }
    this.platform = platform;
    if (this.platform != null) {
      if (velocity.y > 0) {
        velocity.y = 0;
      }
      this.platform.loads.add(this);
      isJump = false;
    }
  }

  public Body getPlatform() {
    return platform;
  }

  public boolean hasPlatform() {
    return platform != null;
  }


  public void setAngle(double angle) {
    this.angle.setValue(angle);
  }


  @Override
  public boolean isFlipX() {
    return velocity.x < 0;
  }

  public double getHorizontalVelocity() {
    return 100;
  }

  public double getVerticalStep() {
    return 30;
  }

  public boolean insideRectangle(double x, double y) {
    return Math.abs(ox() - x) < sprite.getWidth() / 2 &&
        Math.abs(oy() - y) < sprite.getHeight() / 2;
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    super.onIntersect(animation, horizontal, vertical);
    if (animation instanceof Body) {
      if (vertical != null) {
        // >> this is to avoid accumulation of velocity.y by gravity
        Body body = (Body) animation;
        if (body.isSolid()) {
          if (vertical == Overlap.Y.top) {
            if (body.bottom() <= top()) {
              if (velocity.y < 0) {
                velocity.y = 0;
              }
            }
          } else {
            setPlatform(body);
          }
        }
        // <<
      }
    }
  }

  public void jump() {
    if (hasPlatform()) {
      jumpNow();
      setPlatform(null);
    }
  }

  public void jumpNow() {
    isJump = true;
    isClimb = false;
    model.jump();
  }

  public void longJump() {
    if (hasPlatform()) {
      longJumpNow();
      setPlatform(null);
    }
  }

  public void longJumpNow() {
    isJump = true;
    isClimb = false;
    model.longJump();
  }

  public void smallJump() {
    model.smallJump();
  }

  public boolean isDead() {
    return isDead;
  }

  public void setDead(boolean isDead) {
    this.isDead = isDead;
  }

  public void destroy() {
    this.sprite.setLoop(false);
    this.position.z = Engine.destroyZ;
    timeline.stop();
    setDead(true);
    velocity.setValue(0, 0);
    for (Body load : new ArrayList<>(loads)) {
      load.setPlatform(null);
    }
    if (platform != null) {
      platform.loads.remove(this);
    }
  }

  public boolean isClimb() {
    return isClimb;
  }

  public boolean isJump() {
    return isJump;
  }

  public void fly() {
    isJump = false;
    isClimb = false;
    model.fly();
  }

  public void swim() {
    isJump = true;
    isClimb = false;
    model.swim();
    setPlatform(null);
  }

  public static void bounce(Body body1, Body body2) {
    final Body leftBody;
    final Body rightBody;
    if (body1.position.x < body2.position.x) {
      leftBody = body1;
      rightBody = body2;
    } else {
      leftBody = body2;
      rightBody = body1;
    }
    if (Math.signum(body1.velocity.x) != Math.signum(body2.velocity.x)) {
      Context.getTimer().delay(new Runnable() {
        @Override
        public void run() {
          leftBody.velocity.x = -Math.abs(leftBody.velocity.x);
          rightBody.velocity.x = Math.abs(rightBody.velocity.x);
        }
      });
    }
  }

  @Override
  public void onMotionVerticallyBefore() {
    if (!isClimb()) {
      double seconds = Context.getTimer().getElapsedTime() / 1000;
      velocity.plus(0, getMass() * getLayer().a.y * seconds);
      super.onMotionVerticallyBefore();
    }
  }

  @Override
  public void moveNow(double dx, double dy) {
    super.moveNow(dx, dy);
    if (dx != 0) {
      for (Body load : getLoads()) {
        load.moveNow(dx, 0);
      }
    }
    if (dy != 0) {
      for (Body load : getLoads()) {
        load.setBottom(top());
      }
    }
  }

  @Override
  public void getSelection(Selection result, @Nullable Vector position) {
    super.getSelection(result, position);
    result.color = isSolid ? Colors.blue : Colors.black;
  }

}
