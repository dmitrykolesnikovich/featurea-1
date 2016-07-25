package featurea.audio;

class Music extends Clip {
  MusicDriver driver;

  Music(Audio audio, String file, MusicDriver driver) {
    super(audio, file);
    this.driver = driver;
  }

  @Override
  public void play() {
    driver.setLoop(isLoop());
    driver.play();
  }

  @Override
  public void pause() {
    driver.pause();
  }

  @Override
  public void stop() {
    driver.stop();
  }

  @Override
  public void release() {
    audio.release(file);
  }
}
