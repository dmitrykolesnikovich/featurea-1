package featurea.audio;

public abstract class Clip {
  public final String file;
  private boolean isLoop;
  Audio audio;

  Clip(Audio audio, String file) {
    this.audio = audio;
    this.file = file;
  }

  public abstract void play();

  public Clip setLoop(boolean isLoop) {
    this.isLoop = isLoop;
    return this;
  }

  public boolean isLoop() {
    return isLoop;
  }

  public abstract void pause();

  public abstract void stop();

  public abstract void release();

  public static class NotLoadException extends Exception {
    public NotLoadException(String file) {
      super(file);
    }
  }
}
