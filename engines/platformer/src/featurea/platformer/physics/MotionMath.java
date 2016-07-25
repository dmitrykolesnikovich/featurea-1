package featurea.platformer.physics;

import featurea.app.Context;
import featurea.platformer.config.Engine;

public class MotionMath {

  private final Body body;
  private final TurnMath turnMath = new TurnMath();
  private final ClimbMath climbMath;

  public MotionMath(Body body) {
    this.body = body;
    this.climbMath = new ClimbMath();
  }

  public void stay() {
    turnMath.destroy();
    body.velocity.x = 0;
  }

  public void step(boolean isRight) {
    turnMath.destroy();
    double stepVelocity = body.getHorizontalVelocity();
    stepVelocity *= (isRight ? 1 : -1);
    body.velocity.x = stepVelocity;
  }

  public void smallJump() {
    turnMath.destroy();
    body.position.y -= 0.1; // IMPORTANT
    body.velocity.y = Engine.smallJumpVelocityY;
  }

  public void jump() {
    turnMath.destroy();
    body.position.y -= 0.1; // IMPORTANT
    body.velocity.y = Engine.jumpVelocityY;
  }

  public void longJump() {
    turnMath.destroy();
    body.position.y -= 0.1; // IMPORTANT
    body.velocity.y = Engine.jumpVelocityY * 1.75;
  }

  public void fly() {
    turnMath.destroy();
    body.velocity.y = Engine.jumpVelocityY * 1.6;
  }

  public void swim() {
    turnMath.destroy();
    body.velocity.y = Engine.jumpVelocityY * 0.25;
  }

  public void turn() {
    turnMath.perform();
  }

  public void fire() {
    turnMath.destroy();
  }

  public boolean isTurn() {
    return turnMath.isCreate();
  }

  public void climb(double dy) {
    climbMath.move(dy);
  }

  private static class TurnMath {

    private double currentTime;
    private boolean isCreate;

    public void perform() {
      if (isDestroy()) {
        isCreate = true;
        currentTime = Engine.turnTime;
      }
      if (isCreate()) {
        currentTime -= Context.getTimer().getElapsedTime();
        if (currentTime <= 0) {
          stop();
        }
      }
    }

    public void destroy() {
      if (!isDestroy()) {
        isCreate = false;
        currentTime = 0;
      }
    }

    public boolean isCreate() {
      return isCreate && currentTime != 0;
    }

    private void stop() {
      if (isCreate()) {
        isCreate = true;
        currentTime = 0;
      }
    }

    private boolean isDestroy() {
      return !isCreate && currentTime == 0;
    }

  }

  private class ClimbMath {

    private double delta;

    public void move(double dy) {
      delta += dy;
      int progress = (int) (Math.abs(delta) % (Engine.climbStep * body.sprite.sheet.size()));
      body.sprite.sheetIndex = (int) (progress / Engine.climbStep);
    }

  }

}
