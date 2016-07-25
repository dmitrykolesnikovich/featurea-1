package featurea.desktop;

import featurea.audio.OpenALManager;
import org.lwjgl.*;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class OpenALImpl implements OpenALManager {

  private IntArray idleSources, allSources;
  private LongMap<Integer> soundIdToSource = new LongMap<Integer>();
  private IntMap<Long> sourceToSoundId = new IntMap<Long>();
  private long nextSoundId = 0;
  private Mp3Sound[] recentSounds;
  private int mostRecetSound = -1;
  public List<Mp3Music> music = new ArrayList<Mp3Music>(1);
  private final FloatBuffer orientation = (FloatBuffer) BufferUtils.createFloatBuffer(6).put(new float[]{0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f}).flip();
  private final FloatBuffer velocity = (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f}).flip();
  private final FloatBuffer position = (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f}).flip();
  private static final int SIMULTANEOUS_SOURCES_COUNT = 16;

  @Override
  public void init() {
    if (!AL.isCreated()) {
      try {
        AL.create();
      } catch (LWJGLException e) {
        e.printStackTrace();
      }
    }
    allSources = new IntArray(SIMULTANEOUS_SOURCES_COUNT);
    for (int i = 0; i < SIMULTANEOUS_SOURCES_COUNT; i++) {
      int sourceID = AL10.alGenSources();
      if (AL10.alGetError() == AL10.AL_NO_ERROR) {
        allSources.add(sourceID);
      }
    }
    idleSources = new IntArray(allSources);
    recentSounds = new Mp3Sound[SIMULTANEOUS_SOURCES_COUNT];
    AL10.alListener(AL10.AL_ORIENTATION, orientation);
    AL10.alListener(AL10.AL_VELOCITY, velocity);
    AL10.alListener(AL10.AL_POSITION, position);
  }

  @Override
  public void destroy() {
    try {
      if (AL.isCreated()) {
        for (int i = 0; i < music.size(); i++) {
          Mp3Music mp3Music = music.get(i);
          if (mp3Music != null) {
            mp3Music.release();
          }
        }
        for (int i = 0, n = allSources.size; i < n; i++) {
          int sourceID = allSources.get(i);
          int state = AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE);
          if (state != AL10.AL_STOPPED) AL10.alSourceStop(sourceID);
          AL10.alDeleteSources(sourceID);
        }
        sourceToSoundId.clear();
        soundIdToSource.clear();
        AL.destroy();
        while (AL.isCreated()) {
          try {
            Thread.sleep(10);
          } catch (InterruptedException skip) {
            skip.printStackTrace();
          }
        }
      }
    } catch (UnsatisfiedLinkError e) {
      e.printStackTrace();
    }
  }

  private MyThread myThread;

  public void reload() {
    if (myThread != null) {
      myThread.isRun = false;
      while (myThread.isAlive()) ;
    }
    myThread = new MyThread();
    myThread.start();
  }

  @Override
  public Mp3Sound newSoundPool(String file) {
    return new Mp3Sound(this, file);
  }

  @Override
  public Mp3Music newMusic(String file) {
    return new Mp3Music(this, file);
  }

  public int obtainSource(boolean isMusic) {
    if (AL.isCreated()) {
      for (int i = 0, n = idleSources.size; i < n; i++) {
        int sourceId = idleSources.get(i);
        int state = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
        if (state != AL10.AL_PLAYING && state != AL10.AL_PAUSED) {
          if (isMusic) {
            idleSources.removeIndex(i);
          } else {
            if (sourceToSoundId.containsKey(sourceId)) {
              long soundId = sourceToSoundId.get(sourceId);
              sourceToSoundId.remove(sourceId);
              soundIdToSource.remove(soundId);
            }
            long soundId = nextSoundId++;
            sourceToSoundId.put(sourceId, soundId);
            soundIdToSource.put(soundId, sourceId);
          }
          AL10.alSourceStop(sourceId);
          AL10.alSourcei(sourceId, AL10.AL_BUFFER, 0);
          AL10.alSourcef(sourceId, AL10.AL_GAIN, 1);
          AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
          AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 1f);
          return sourceId;
        }
      }
    }
    return -1;
  }

  public void freeSource(int sourceID) {
    if (AL.isCreated()) {
      AL10.alSourceStop(sourceID);
      AL10.alSourcei(sourceID, AL10.AL_BUFFER, 0);
      if (sourceToSoundId.containsKey(sourceID)) {
        long soundId = sourceToSoundId.remove(sourceID);
        soundIdToSource.remove(soundId);
      }
      idleSources.add(sourceID);
    }
  }

  public void freeBuffer(int bufferID) {
    if (AL.isCreated()) {
      for (int i = 0, n = idleSources.size; i < n; i++) {
        int sourceID = idleSources.get(i);
        if (AL10.alGetSourcei(sourceID, AL10.AL_BUFFER) == bufferID) {
          if (sourceToSoundId.containsKey(sourceID)) {
            long soundId = sourceToSoundId.remove(sourceID);
            soundIdToSource.remove(soundId);
          }
          AL10.alSourceStop(sourceID);
          AL10.alSourcei(sourceID, AL10.AL_BUFFER, 0);
        }
      }
    }
  }

  public void stopSourcesWithBuffer(int bufferID) {
    if (AL.isCreated()) {
      for (int i = 0, n = idleSources.size; i < n; i++) {
        int sourceID = idleSources.get(i);
        if (AL10.alGetSourcei(sourceID, AL10.AL_BUFFER) == bufferID) {
          if (sourceToSoundId.containsKey(sourceID)) {
            long soundId = sourceToSoundId.remove(sourceID);
            soundIdToSource.remove(soundId);
          }
          AL10.alSourceStop(sourceID);
        }
      }
    }
  }

  public void pauseSourcesWithBuffer(int bufferID) {
    if (AL.isCreated()) {
      for (int i = 0, n = idleSources.size; i < n; i++) {
        int sourceID = idleSources.get(i);
        if (AL10.alGetSourcei(sourceID, AL10.AL_BUFFER) == bufferID) {
          AL10.alSourcePause(sourceID);
        }
      }
    }
  }

  public void resumeSourcesWithBuffer(int bufferID) {
    if (AL.isCreated()) {
      for (int i = 0, n = idleSources.size; i < n; i++) {
        int sourceID = idleSources.get(i);
        if (AL10.alGetSourcei(sourceID, AL10.AL_BUFFER) == bufferID) {
          if (AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PAUSED) {
            AL10.alSourcePlay(sourceID);
          }
        }
      }
    }
  }

  public void update() {
    if (AL.isCreated()) {
      for (int i = 0; i < music.size(); i++) {
        Mp3Music mp3Music = music.get(i);
        if (mp3Music != null) {
          mp3Music.update();
        }
      }
    }
  }

  public long getSoundId(int sourceId) {
    if (!sourceToSoundId.containsKey(sourceId)) return -1;
    return sourceToSoundId.get(sourceId);
  }

  public void stopSound(long soundId) {
    if (AL.isCreated()) {
      if (soundIdToSource.containsKey(soundId)) {
        int sourceId = soundIdToSource.get(soundId);
        AL10.alSourceStop(sourceId);
      }
    }
  }

  public void pauseSound(long soundId) {
    if (AL.isCreated()) {
      if (soundIdToSource.containsKey(soundId)) {
        int sourceId = soundIdToSource.get(soundId);
        AL10.alSourcePause(sourceId);
      }
    }
  }

  public void resumeSound(long soundId) {
    if (AL.isCreated()) {
      if (soundIdToSource.containsKey(soundId)) {
        int sourceId = soundIdToSource.get(soundId);
        if (AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PAUSED) {
          AL10.alSourcePlay(sourceId);
        }
      }
    }
  }

  public void setSoundGain(long soundId, double volume) {
    if (AL.isCreated()) {
      if (soundIdToSource.containsKey(soundId)) {
        int sourceId = soundIdToSource.get(soundId);
        AL10.alSourcef(sourceId, AL10.AL_GAIN, (float)volume);
      }
    }
  }

  public void setSoundLooping(long soundId, boolean looping) {
    if (AL.isCreated()) {
      if (soundIdToSource.containsKey(soundId)) {
        int sourceId = soundIdToSource.get(soundId);
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);
      }
    }
  }

  public void setSoundPitch(long soundId, double pitch) {
    if (soundIdToSource.containsKey(soundId)) {
      int sourceId = soundIdToSource.get(soundId);
      AL10.alSourcef(sourceId, AL10.AL_PITCH, (float)pitch);
    }
  }

  public void setSoundPan(long soundId, double pan, double volume) {
    if (soundIdToSource.containsKey(soundId)) {
      int sourceId = soundIdToSource.get(soundId);
      AL10.alSource3f(sourceId, AL10.AL_POSITION, (float) Math.cos((pan - 1) * Math.PI / 2), 0, (float) Math.sin((pan + 1) * Math.PI / 2));
      AL10.alSourcef(sourceId, AL10.AL_GAIN, (float)volume);
    }
  }

  public void retain(Mp3Sound sound, boolean stop) {
    mostRecetSound++;
    mostRecetSound %= recentSounds.length;
    if (stop) {
      if (recentSounds[mostRecetSound] != null) {
        recentSounds[mostRecetSound].stopAllStreams();
      }
    }
    recentSounds[mostRecetSound] = sound;
  }

  public void forget(Mp3Sound sound) {
    for (int i = 0; i < recentSounds.length; i++) {
      if (recentSounds[i] == sound) recentSounds[i] = null;
    }
  }

  private class MyThread extends Thread {

    public boolean isRun;

    @Override
    public void run() {
      while (isRun) {
        update();
      }
    }

    @Override
    public synchronized void start() {
      isRun = true;
      super.start();
    }
  }

}