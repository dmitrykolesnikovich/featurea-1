package featurea.android;

import android.annotation.TargetApi;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import featurea.app.Context;
import featurea.audio.MusicDriver;
import featurea.audio.OpenALManager;
import featurea.audio.SoundDriver;
import featurea.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class OpenALImpl implements OpenALManager {

  public final SoundPool soundPoolPool;
  public final AudioManager manager;
  public final List<Mp3Music> musics = new ArrayList<Mp3Music>();

  public OpenALImpl() {
    this.soundPoolPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 100);
    this.manager = (AudioManager) (FeatureaApplication.instance.getSystemService(android.content.Context.AUDIO_SERVICE));
  }

  @TargetApi(Build.VERSION_CODES.FROYO)
  protected void pause() {
    if (soundPoolPool == null) {
      return;
    }
    synchronized (musics) {
      for (Mp3Music music : musics) {
        if (music.isPlay()) {
          music.pause();
          music.wasPlaying = true;
        } else
          music.wasPlaying = false;
      }
    }
    this.soundPoolPool.autoPause();
  }

  @TargetApi(Build.VERSION_CODES.FROYO)
  protected void resume() {
    if (soundPoolPool == null) {
      return;
    }
    synchronized (musics) {
      for (int i = 0; i < musics.size(); i++) {
        if (musics.get(i).wasPlaying == true) musics.get(i).play();
      }
    }
    this.soundPoolPool.autoResume();
  }

  @Override
  public MusicDriver newMusic(String file) {
    MediaPlayer mediaPlayer = new MediaPlayer();
    String absolutePath = ApkFileUtil.getInstance().getCacheDir() + "/" + file;
    Context.getFiles().cacheIfNotExists(file, absolutePath);
    FileUtil.copyPaste(Context.getFiles().getStream(file), absolutePath);
    try {
      mediaPlayer.setDataSource(absolutePath);
      mediaPlayer.prepare();
      Mp3Music music = new Mp3Music(this, mediaPlayer);
      synchronized (musics) {
        musics.add(music);
      }
      return music;
    } catch (Exception ex) {
      throw new RuntimeException("Error loading audio file: " + file, ex);
    }
  }

  @Override
  public void init() {
  }

  @Override
  public void destroy() {
  }

  @Override
  public SoundDriver newSoundPool(String file) {
    String absolutePath = ApkFileUtil.getInstance().getCacheDir() + "/" + file;
    Context.getFiles().cacheIfNotExists(file, absolutePath);
    FileUtil.copyPaste(Context.getFiles().getStream(file), absolutePath);
    try {
      return new Mp3Sound(soundPoolPool, soundPoolPool.load(absolutePath, 1));
    } catch (Exception ex) {
      throw new RuntimeException("Error loading audio file: " + file, ex);
    }
  }

  public void dispose() {
    if (soundPoolPool == null) {
      return;
    }
    synchronized (musics) {
      ArrayList<Mp3Music> musicsCopy = new ArrayList<Mp3Music>(musics);
      for (Mp3Music music : musicsCopy) {
        music.release();
      }
    }
    soundPoolPool.release();
  }
}
