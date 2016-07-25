package featurea.platformer.physics;

import featurea.app.Context;
import featurea.platformer.Animation;
import featurea.platformer.View;
import featurea.platformer.overlap.Overlap;

public class HeroBody extends Body {

  private View view;

  public HeroBody() {
    reset();
  }

  private boolean isStepRight = true;

  public void joystickNothing() {
    model.stay();
    resetView();
  }

  public void joystickLeft() {
    step(false);
  }

  public void joystickRight() {
    step(true);
  }

  public void joystickUp() {
    if (isClimb) {
      double dy = -getVerticalStep() * Context.getTimer().getElapsedTime() / 1000;
      move(0, dy, 0);
      model.climb(dy);
    } else {
      // no op
    }
  }

  public void joystickDown() {
    if (isClimb) {
      double dy = getVerticalStep() * Context.getTimer().getElapsedTime() / 1000;
      move(0, dy, 0);
      model.climb(dy);
    } else {
      model.stay();
      setView(View.sit);
    }
  }

  public void fire() {
    setView(View.fire);
    model.fire();
  }

  public void destroy() {
    super.destroy();
    joystickNothing();
    setView(View.dead);
  }

  public void performMovement(boolean isRight) {
    model.step(isRight);
  }

  private void onMovementHorizontalStep(boolean isRight) {
    if (hasPlatform()) {
      setView(View.walk);
    } else {
      resetView();
    }
    performMovement(isRight);
  }

  private void resetView() {
    if (isJump) {
      setView(View.stay);
    } else if (isClimb) {
      setView(View.climb);
    } else {
      setView(View.stay);
    }
  }

  private void step(boolean isRight) {
    this.isStepRight = isRight;
    if (isClimb()) {
      onClimbHorizontalStep(isRight);
    } else {
      onMovementHorizontalStep(isRight);
    }
  }


  private void onClimbHorizontalStep(boolean isRight) {
    Body ladder = getPlatform();
    if (left() < ladder.left()) {
      if (isRight) {
        double dx = width() + ladder.width();
        position.x += dx;
      } else {
        isClimb = false;
        onMovementHorizontalStep(false);
      }
    } else {
      if (isRight) {
        isClimb = false;
        onMovementHorizontalStep(true);
      } else {
        double dx = width() + ladder.width();
        position.x -= dx;
      }
    }
  }


  public View getView() {
    return view;
  }

  public void setView(View view) {
    this.view = view;
  }

  protected void climb() {
    this.sprite.setProgress(0);
    isClimb = true;
    isJump = false;
    setView(View.climb);
    velocity.x = velocity.y = 0;
  }

  public void setLadder(Body platform) {
    setPlatform(platform);
    if (platform != null) {
      climb();
    }
  }

  private void reset() {
    this.isJump = false;
    this.isClimb = false;
    resetView();
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    super.onIntersect(animation, horizontal, vertical);
    if (animation instanceof Body) {
      if (horizontal != null) {
        Body body = (Body) animation;
        if (body.isLadder()) {
          setLadder(body);
        }
      }
    }
  }

  @Override
  public boolean isFlipX() {
    return !isStepRight;
  }

  public boolean isRight() {
    return isStepRight;
  }

}
