package featurea.audio;

public interface OpenALManager {

  void init();

  void destroy();

  SoundDriver newSoundPool(String file);

  MusicDriver newMusic(String file);

}
