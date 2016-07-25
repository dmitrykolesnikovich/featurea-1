package featurea.audio;

import featurea.app.Context;
import featurea.app.MediaPlayer;

import java.util.*;

public final class Audio {

  private final MediaPlayer mediaPlayer;
  final Map<String, List<Sound>> soundMap = new WeakHashMap<String, List<Sound>>();
  private final Map<String, Music> musicMap = new HashMap<String, Music>();
  private double volume;
  public boolean isEnable;

  public Audio(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }

  public Clip load(String file) {
    int index = file.lastIndexOf('/');
    if (index == -1) {
      return loadSound(file);
    } else {
      String dir = file.substring(0, index);
      index = dir.lastIndexOf('/');
      String prefix = dir.substring(index + 1, dir.length());
      if ("music".equalsIgnoreCase(prefix)) {
        return loadMusic(file);
      } else {
        return loadSound(file);
      }
    }
  }

  private Clip loadSound(String file) {
    List<Sound> sounds = soundMap.get(file);
    if (sounds == null) {
      sounds = new ArrayList<>();
    }
    Sound result = new Sound(this, file);
    sounds.add(result);
    soundMap.put(file, sounds);
    return result;
  }

  private Music loadMusic(String file) {
    Music result = musicMap.get(file);
    if (result == null) {
      final MusicDriver driver = Context.al.newMusic(file);
      result = new Music(this, file, driver);
      musicMap.put(file, result);
    }
    return result;
  }

  public void release(String file) {
    if (musicMap.containsKey(file)) {
      Music music = musicMap.get(file);
      music.driver.release();
      musicMap.remove(music);
    } else if (soundMap.containsKey(file)) {
      Sound sound = soundMap.get(file).get(0);
      sound.driver.releaseAllStreams();
      soundMap.remove(file);
    }
  }

  public Clip get(String file) {
    if (soundMap.containsKey(file)) {
      Sound clip = new Sound(this, file);
      putSound(file, clip);
      return clip;
    } else if (musicMap.containsKey(file)) {
      return musicMap.get(file);
    }
    return null;
  }

  public void play(String file) {
    if (!isEnable) {
      return;
    }
    if (musicMap.containsKey(file)) {
      musicMap.get(file).play();
    } else if (soundMap.containsKey(file)) {
      for (Sound sound : soundMap.get(file)) {
        sound.play();
      }
    } else {
      if (!mediaPlayer.isProduction()) {
        mediaPlayer.loader.load(file);
      } else {
        System.err.println("Audio not load: " + file);
      }
    }
  }

  public void loop(String file) {
    if (!isEnable) {
      return;
    }
    Clip clip = get(file);
    if (clip != null) {
      clip.setLoop(true).play();
    }
  }

  public void pause(String file) {
    if (musicMap.containsKey(file)) {
      musicMap.get(file).pause();
    } else if (soundMap.containsKey(file)) {
      for (Sound sound : soundMap.get(file)) {
        sound.pause();
      }
    }
  }

  public void stop(String file) {
    if (musicMap.containsKey(file)) {
      musicMap.get(file).stop();
    } else if (soundMap.containsKey(file)) {
      for (Sound sound : soundMap.get(file)) {
        sound.stop();
      }
    }
  }

  public void resumeAll() {
    if (!isEnable) {
      return;
    }
    for (List<Sound> soundsClips : soundMap.values()) {
      for (Sound sound : soundsClips) {
        sound.play();
      }
    }
    for (Music music : musicMap.values()) {
      music.play();
    }
  }

  public void pauseAll() {
    for (List<Sound> soundsClips : soundMap.values()) {
      for (Sound sound : soundsClips) {
        sound.pause();
      }
    }
    for (Music music : musicMap.values()) {
      music.pause();
    }
  }

  public void stopAll() {
    for (List<Sound> soundsClips : soundMap.values()) {
      for (Sound sound : soundsClips) {
        sound.stop();
      }
    }
    for (Music music : musicMap.values()) {
      music.pause();
    }
  }

  public void releaseAll() {
    for (List<Sound> soundsClips : soundMap.values()) {
      soundsClips.get(0).driver.releaseAllStreams();
    }
    soundMap.clear();
    for (Music music : musicMap.values()) {
      music.release();
    }
    musicMap.clear();
  }

  public void setVolume(double volume) {
    this.volume = volume;
    for (Music music : musicMap.values()) {
      music.driver.setVolume(volume);
    }
    for (List<Sound> soundsClips : soundMap.values()) {
      Sound sound = soundsClips.get(0);
      sound.driver.setVolumeStream(sound.id, volume);
    }
  }

  public double getVolume() {
    return volume;
  }

  private void putSound(String file, Sound sound) {
    List<Sound> fileSounds = soundMap.get(file);
    if (fileSounds == null) {
      fileSounds = new ArrayList<Sound>();
    }
    fileSounds.add(sound);
    soundMap.put(file, fileSounds);
  }

  public Iterable<String> getClips() {
    Set<String> result = new HashSet<String>();
    result.addAll(soundMap.keySet());
    result.addAll(musicMap.keySet());
    return result;
  }

  public void onCreate() {
    Context.al.init();
  }

  public void onDestroy() {
    for (String file : getClips()) {
      release(file);
    }
    if (Context.al != null) {
      Context.al.destroy();
    }
  }

}
