package featurea.app;

import featurea.audio.Audio;
import featurea.audio.OpenALManager;
import featurea.input.Input;
import featurea.opengl.OpenGLManager;
import featurea.opengl.Render;
import featurea.util.Files;
import featurea.util.Performance;
import featurea.util.Properties;
import featurea.util.Timer;
import featurea.xml.XmlSchema;

import java.util.Map;
import java.util.WeakHashMap;

public final class Context {

  public static final Map<Thread, MediaPlayer> mediaPlayers = new WeakHashMap<>();
  public static OpenGLManager gl;
  public static OpenALManager al;

  private Context() {
    // no op
  }

  public static MediaPlayer getMediaPlayer() {
    return mediaPlayers.get(Thread.currentThread());
  }

  public static Audio getAudio() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.audio;
  }

  public static Input getInput() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.input;
  }

  public static ProjectClassLoader getClassLoader() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.getClassLoader();
  }

  public static Files getFiles() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.getFiles();
  }

  public static Application getApplication() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.app;
  }

  public static Timer getTimer() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.timer;
  }

  public static Performance getPerformance() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.performance;
  }

  public static XmlResources getResources() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.getResources();
  }

  public static Loader getLoader() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.loader;
  }

  public static Render getRender() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.render;
  }

  public static boolean isProduction() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.isProduction();
  }

  /*config*/

  public static Properties getPack() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.project.packProperties;
  }

  public static XmlSchema getXmlSchema() {
    MediaPlayer mediaPlayer = getMediaPlayer();
    if (mediaPlayer == null) {
      throw new MediaPlayerNotFoundException();
    }
    return mediaPlayer.getXmlSchema();
  }

  public static boolean isFeaturea() {
    String isEditMode = System.getProperty("featurea.editMode");
    if ("true".equals(isEditMode)) {
      return true;
    } else {
      return false;
    }
  }

}
