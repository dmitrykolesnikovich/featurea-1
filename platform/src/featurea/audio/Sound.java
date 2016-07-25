package featurea.audio;

import featurea.app.Context;

import java.util.List;

class Sound extends Clip {
  final SoundDriver driver;
  long id = -1;

  Sound(Audio audio, String file) {
    super(audio, file);
    List<Sound> array = audio.soundMap.get(file);
    if (array != null) {
      this.driver = array.get(0).driver;
    } else {
      this.driver = Context.al.newSoundPool(file);
    }
  }

  @Override
  public void play() {
    if (isLoop()) {
      id = driver.loopNewStream();
    } else {
      id = driver.playNewStream();
    }
  }

  @Override
  public void pause() {
    if (id != -1) {
      driver.pauseStream(id);
    }
  }

  @Override
  public void stop() {
    if (id != -1) {
      driver.stopStream(id);
    }
  }

  @Override
  public void release() {
    audio.release(file);
  }
}
