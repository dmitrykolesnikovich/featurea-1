package featurea.util;

import featurea.app.MediaPlayer;

import java.util.Iterator;
import java.util.Map;

public class Timer {

  private final MediaPlayer mediaPlayer;
  private final BufferedMap<Runnable, Double> modifications = new BufferedMap<>();
  private double elapsedTime;
  public double scale = 1;

  public Timer(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }

  public double getElapsedTime() {
    return elapsedTime * scale;
  }

  public void setElapsedTime(double elapsedTime) {
    this.elapsedTime = elapsedTime;
  }

  public void delay(Runnable modification, double delta) {
    modifications.putBuffer(modification, delta);
  }

  public void remove(Runnable modification) {
    modifications.remove(modification);
  }

  public void delay(Runnable modification) {
    delay(modification, 0);
  }

  public void onTick(double elapsedTime) {
    modifications.flush();
    Iterator<Map.Entry<Runnable, Double>> iterator = modifications.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<Runnable, Double> entry = iterator.next();
      Runnable task = entry.getKey();
      double delta = entry.getValue();
      delta -= elapsedTime;
      if (delta <= 0) {
        try {
          task.run();
        } finally {
          iterator.remove();
        }
      } else {
        entry.setValue(delta);
      }
    }
  }

  public void clearModifications() {
    modifications.clear();
  }

}
