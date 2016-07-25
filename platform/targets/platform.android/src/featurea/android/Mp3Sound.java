package featurea.android;

import android.annotation.TargetApi;
import android.media.SoundPool;
import android.os.Build;
import featurea.audio.SoundDriver;

public class Mp3Sound implements SoundDriver {
  final SoundPool soundPool;
  final int soundId;
  final IntArray streamIds = new IntArray(8);

  public Mp3Sound(android.media.SoundPool pool, int soundId) {
    this.soundPool = pool;
    this.soundId = soundId;
  }

  @Override
  public void releaseAllStreams() {
    soundPool.unload(soundId);
  }

  @Override
  public long playNewStream() {
    return playNewStream(1);
  }

  @Override
  public long playNewStream(double volume) {
    if (streamIds.size == 8) streamIds.pop();
    int streamId = soundPool.play(soundId, (float) volume, (float) volume, 1, 0, 1);
    if (streamId == 0) return -1;
    streamIds.add(streamId);
    return streamId;
  }

  public void stopAllStreams() {
    for (int i = 0, n = streamIds.size; i < n; i++)
      soundPool.stop(streamIds.get(i));
  }

  @Override
  public void stopStream(long streamId) {
    soundPool.stop((int) streamId);
  }

  @TargetApi(Build.VERSION_CODES.FROYO)
  @Override
  public void pauseAllStreams() {
    soundPool.autoPause();
  }

  @Override
  public void pauseStream(long streamId) {
    soundPool.pause((int) streamId);
  }

  @TargetApi(Build.VERSION_CODES.FROYO)
  @Override
  public void resumeAllStreams() {
    soundPool.autoResume();
  }

  @Override
  public void resumeStream(long streamId) {
    soundPool.resume((int) streamId);
  }

  @Override
  public void setVolumeStream(long streamId, double volume) {
    soundPool.setVolume((int) streamId, (float) volume, (float) volume);
  }

  @Override
  public long loopNewStream() {
    return loopNewStream(1);
  }

  @Override
  public long loopNewStream(double volume) {
    if (streamIds.size == 8) streamIds.pop();
    int streamId = soundPool.play(soundId, (float) volume, (float) volume, 1, -1, 1);
    if (streamId == 0) return -1;
    streamIds.add(streamId);
    return streamId;
  }

  @Override
  public void setLoopStream(long streamId, boolean isLoop) {
    soundPool.setLoop((int) streamId, isLoop ? -1 : 0);
  }
}
