package featurea.motion;

import featurea.app.Context;
import featurea.xml.XmlResource;

public abstract class Motion implements XmlResource {

  public Tween tween = Tweens.linear;
  public Runnable onStart;
  public Runnable onStop;
  public double startTime;
  public double velocity;
  Timeline timeline;
  boolean isStart;
  boolean isStop;
  boolean isLoop;
  boolean isBounce;
  boolean isReverse;
  private final Animator animator = new Animator(this);
  private boolean isRemoveOnStop;

  public abstract double range();

  public abstract void step(double r1, double r2);

  public Motion setVelocity(double velocity) {
    if (velocity < 0) {
      throw new IllegalArgumentException("velocity < 0");
    }
    this.velocity = velocity;
    return this;
  }

  public Motion setLoop(boolean isLoop) {
    this.isLoop = isLoop;
    if (isLoop) {
      isBounce = false;
    }
    return this;
  }

  public Motion setBounce(boolean isBounce) {
    this.isBounce = isBounce;
    if (isBounce) {
      isLoop = false;
    }
    return this;
  }

  public Motion setReverse(boolean isReverse) {
    this.isReverse = isReverse;
    return this;
  }

  public boolean isReverse() {
    return isReverse;
  }

  public Motion setTween(Tween tween) {
    this.tween = tween;
    return this;
  }

  public void start() {
    if (!isStart) {
      isStart = true;
      if (onStart != null) {
        onStart.run();
      }
    }
    if (isStop) {
      isStop = false;
    }
  }

  public void stop() {
    if (!isStop) {
      isStop = true;
      Context.getTimer().delay(
          new Runnable() {
            @Override
            public void run() {
              if (isRemoveOnStop) {
                removeSelf();
              }
              if (onStop != null) {
                onStop.run();
              }
            }
          });
    }
  }

  public void onTick(double elapsedTime) {
    animator.onTick(elapsedTime);
  }

  public void setProgress(double progress) {
    animator.setProgress(progress);
  }

  double getDuration() {
    return range() / Math.abs(velocity);
  }

  public double computeStartTime() {
    if (startTime == 0) {
      startTime = getDuration();
    }
    return startTime;
  }

  public void removeSelf() {
    timeline.remove(this);
  }

  public Motion setRemoveOnStop(boolean isRemoveOnStop) {
    this.isRemoveOnStop = isRemoveOnStop;
    return this;
  }

  public boolean isStop() {
    return isStop;
  }

  public void begin() {
    animator.begin();
  }

  public void finish() {
    animator.finish();
  }

  @Override
  public Motion build() {
    return this;
  }

}
