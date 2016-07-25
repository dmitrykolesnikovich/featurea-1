package featurea.android;

import android.media.MediaPlayer;
import featurea.audio.MusicDriver;

import java.io.IOException;

public class Mp3Music implements MusicDriver {
  private final OpenALImpl audio;
  private MediaPlayer player;
  private boolean isPrepared = true;
  public boolean wasPlaying = false;
  private double volume = 1f;

  public Mp3Music(OpenALImpl audio, MediaPlayer player) {
    this.audio = audio;
    this.player = player;
  }

  @Override
  public void release() {
    if (player == null) return;
    try {
      if (player.isPlaying()) player.stop();
      player.release();
    } catch (Throwable t) {
      t.printStackTrace();
    } finally {
      player = null;
      synchronized (audio.musics) {
        audio.musics.remove(this);
      }
    }
  }

  @Override
  public boolean isLoop() {
    if (player == null) return false;
    return player.isLooping();
  }

  @Override
  public boolean isPlay() {
    if (player == null) return false;
    return player.isPlaying();
  }

  @Override
  public void pause() {
    if (player == null) return;
    if (player.isPlaying()) {
      player.pause();
    }
    wasPlaying = false;
  }

  @Override
  public void play() {
    if (player == null) return;
    if (player.isPlaying()) return;
    try {
      if (!isPrepared) {
        player.prepare();
        isPrepared = true;
      }
      player.start();
    } catch (IllegalStateException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setLoop(boolean isLoop) {
    if (player == null) return;
    player.setLooping(isLoop);
  }

  @Override
  public void setVolume(double volume) {
    if (player == null) return;
    this.volume = volume;
    player.setVolume((float)volume, (float)volume);
  }

  @Override
  public double getVolume() {
    return volume;
  }

  @Override
  public void stop() {
    if (player == null) return;
    if (isPrepared) {
      player.seekTo(0);
    }
    player.stop();
    isPrepared = false;
  }

  public void setPosition(double position) {
    if (player == null) return;
    try {
      if (!isPrepared) {
        player.prepare();
        isPrepared = true;
      }
      player.seekTo((int) (position * 1000));
    } catch (IllegalStateException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public double getDuration() {
    if (player == null) return 0.0f;
    return player.getDuration() / 1000f;
  }
}
