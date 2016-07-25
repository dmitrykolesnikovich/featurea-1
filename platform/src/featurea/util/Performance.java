package featurea.util;

import featurea.app.MediaPlayer;

public class Performance {

  private final MediaPlayer mediaPlayer;
  public double fps = 0;
  public int drawTextureCount = 0;
  public long framesCount = 0;

  public Performance(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }

  public void onStartApplicationTick(double elapsedTime) {
    framesCount++;
    if (elapsedTime == 0) {
      return;
    }
    double currentFps = 1000.0d / elapsedTime;
    if (fps == 0) {
      fps = currentFps;
    }
    fps = (fps + currentFps) / 2;
  }

  public void onFinishApplicationTick() {
    drawTextureCount = 0;
  }

}
