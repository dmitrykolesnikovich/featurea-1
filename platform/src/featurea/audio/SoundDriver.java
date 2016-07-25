package featurea.audio;

public interface SoundDriver {
  long playNewStream();

  long playNewStream(double volume);

  long loopNewStream();

  long loopNewStream(double volume);

  void stopStream(long streamId);

  void pauseStream(long streamId);

  void resumeStream(long streamId);

  void setLoopStream(long streamId, boolean isLoop);

  void setVolumeStream(long streamId, double volume);

  void stopAllStreams();

  void pauseAllStreams();

  void resumeAllStreams();

  void releaseAllStreams();
}
