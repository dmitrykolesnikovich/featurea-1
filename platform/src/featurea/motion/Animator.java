package featurea.motion;

import featurea.app.Context;

public class Animator {

  private final Motion motion;
  private double t;
  private double r;

  public Animator(Motion motion) {
    this.motion = motion;
  }

  public void onTick(double elapsedTime) {
    if (motion.timeline == null) {
      System.err.println("[Animator] No timeline found");
      return;
    }
    if (elapsedTime == 0 || motion.getDuration() == 0) {
      return;
    }
    while (!motion.isStop) {
      if (!motion.isReverse) {
        if (t + elapsedTime < motion.getDuration()) {
          step(elapsedTime);
          return;
        }
        double stepTime = motion.getDuration() - t;
        step(stepTime);
        elapsedTime -= stepTime;
      } else {
        if (t - elapsedTime >= 0) {
          step(-elapsedTime);
          return;
        }
        step(-t);
        elapsedTime -= t;
      }
      tryStop();
    }
  }

  void step(double dt) {
    double f1 = motion.tween.f(t / motion.getDuration());
    t += dt;
    t = Math.max(0, t);
    t = Math.min(motion.getDuration(), t);
    double f2 = motion.tween.f(t / motion.getDuration());
    double df = f2 - f1;
    double dr = df * motion.range();
    double r2 = Math.min(r + dr, motion.range());
    motion.step(r, r2);
    r = r2;
  }

  private void tryStop() {
    if (!motion.isReverse) {
      if (motion.isLoop) {
        setProgress(0);
      } else if (motion.isBounce) {
        motion.isReverse = true;
        setProgress(1);
      } else {
        motion.stop();
        setProgress(1);
      }
    } else {
      if (motion.isLoop) {
        setProgress(1);
      } else if (motion.isBounce) {
        motion.isReverse = false;
        setProgress(0);
      } else {
        motion.stop();
        setProgress(0);
      }
    }
  }

  void setProgress(double progress) {
    t = motion.getDuration() * progress;
    r = motion.range() * progress;
  }

  void finish() {
    boolean isStop = motion.isStop;
    motion.isStop = false;
    double elapsedTime = motion.getDuration() - t;
    if (motion.isReverse) {
      elapsedTime *= -1;
    }
    onTick(elapsedTime);
    motion.isStop = isStop;
  }

  void begin() {
    boolean isStop = motion.isStop;
    motion.isStop = false;
    double elapsedTime = -t;
    if (motion.isReverse) {
      elapsedTime *= -1;
    }
    onTick(elapsedTime);
    motion.isStop = isStop;
  }
}
