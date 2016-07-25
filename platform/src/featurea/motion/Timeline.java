package featurea.motion;

import java.util.ArrayList;
import java.util.List;

public class Timeline {

  public final Object transform;
  private final List<Motion> motions = new ArrayList<>();
  private double timeCursor;
  private double currentStepStartTime;

  public Timeline(Object transform) {
    this.transform = transform;
  }

  public Timeline add(List<Motion> motions) {
    for (Motion motion : motions) {
      add(motion);
    }
    return this;
  }

  public Timeline add(Motion motion) {
    add(motion, 0);
    return this;
  }

  public Timeline add(Motion motion, double delay) {
    if (!motions.contains(motion)) {
      motions.add(motion);
      motion.startTime = currentStepStartTime + delay;
      motion.timeline = this;
    }
    return this;
  }

  public Timeline remove(List<Motion> motions) {
    for (Motion motion : motions) {
      remove(motion);
    }
    return this;
  }

  public Timeline remove(Motion motion) {
    motions.remove(motion);
    motion.timeline = null;
    return this;
  }

  public Timeline next() {
    return next(0);
  }

  public Timeline next(double delay) {
    double max = -1;
    for (Motion motion : motions) {
      double endTime = motion.startTime + motion.getDuration();
      if (endTime > max) {
        max = endTime;
      }
    }
    currentStepStartTime = max + delay;
    return this;
  }

  public final void onTick(double elapsedTime) {
    if (motions.isEmpty() && timeCursor == 0) {
      return;
    }
    if (!isStop()) {
      timeCursor += elapsedTime;
    }
    for (Motion motion : motions) {
      if (motion.isStop) {
        continue;
      }
      if (!motion.isStart) {
        double startTime = motion.computeStartTime();
        if (timeCursor > startTime) {
          elapsedTime = Math.min(elapsedTime, timeCursor - startTime);
        }
      }
      if (!motion.isStart) {
        motion.start();
      }
      motion.onTick(elapsedTime);
    }
  }

  public Timeline start() {
    for (Motion motion : motions) {
      motion.start();
    }
    return this;
  }

  public Timeline stop() {
    for (Motion motion : motions) {
      motion.stop();
    }
    return this;
  }

  public Timeline begin() {
    for (Motion motion : motions) {
      motion.begin();
    }
    return this;
  }

  public Timeline finish() {
    for (Motion motion : motions) {
      motion.finish();
    }
    return this;
  }

  public Timeline setReverse(boolean isReverse) {
    for (Motion motion : motions) {
      motion.setReverse(isReverse);
    }
    return this;
  }

  public boolean isStop() {
    for (Motion motion : motions) {
      if (!motion.isStop) {
        return false;
      }
    }
    return true;
  }

  public boolean contains(Motion motion) {
    return motions.contains(motion);
  }

  public boolean isEmpty() {
    return motions.isEmpty();
  }

  public void clear() {
    for (Motion motion : motions) {
      motion.stop();
    }
    motions.clear();
  }

}
