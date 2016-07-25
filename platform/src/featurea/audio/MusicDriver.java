package featurea.audio;

public interface MusicDriver {
  void play();

  void pause();

  void stop();

  boolean isPlay();

  void release();

  void setLoop(boolean isLoop);

  boolean isLoop();

  void setVolume(double volume);

  double getVolume();
}
