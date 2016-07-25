package featurea.desktop;

import featurea.app.Context;
import featurea.audio.MusicDriver;
import javazoom.jl.decoder.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Mp3Music implements MusicDriver {

  static private final int bufferSize = 4096 * 10;
  static private final int bufferCount = 3;
  static private final int bytesPerSample = 2;
  static private final byte[] tempBytes = new byte[bufferSize];
  static private final ByteBuffer tempBuffer = BufferUtils.createByteBuffer(bufferSize);
  private final OpenALImpl player;
  private IntBuffer buffers;
  private int sourceID = -1;
  private int format, sampleRate;
  private boolean isLoop, isPlaying;
  private double volume = 1;
  private double pan = 0;
  private double renderedSeconds, secondsPerBuffer;
  protected final String file;
  private Bitstream bitStream;
  private OutputBuffer outputBuffer;
  private MP3Decoder decoder;

  public Mp3Music(OpenALImpl player, String file) {
    this.player = player;
    this.file = file;
    if (player != null) {
      player.music.add(this);
    }
    reset();
  }

  public int read(byte[] buffer) {
    try {
      int totalLength = 0;
      int minRequiredLength = buffer.length - OutputBuffer.BUFFERSIZE * 2;
      while (totalLength <= minRequiredLength) {
        Header header = bitStream.readFrame();
        try {
          decoder.decodeFrame(header, bitStream);
        } catch (Exception skip) {
        }
        bitStream.closeFrame();
        int length = outputBuffer.reset();
        java.lang.System.arraycopy(outputBuffer.getBuffer(), 0, buffer, totalLength, length);
        totalLength += length;
      }
      return totalLength;
    } catch (Throwable ex) {
      reset();
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void play() {
    if (sourceID == -1) {
      sourceID = player.obtainSource(true);
      if (sourceID == -1) return;
      if (buffers == null) {
        buffers = BufferUtils.createIntBuffer(bufferCount);
        AL10.alGenBuffers(buffers);
        if (AL10.alGetError() != AL10.AL_NO_ERROR) throw new RuntimeException("Unable to allocate audio buffers.");
      }
      AL10.alSourcei(sourceID, AL10.AL_LOOPING, AL10.AL_FALSE);
      int filled = 0;
      for (int i = 0; i < bufferCount; i++) {
        int bufferID = buffers.get(i);
        if (!fill(bufferID)) break;
        filled++;
        AL10.alSourceQueueBuffers(sourceID, bufferID);
      }
      if (filled == 0) {
      }
      if (AL10.alGetError() != AL10.AL_NO_ERROR) {
        System.err.println("alGetError = " + AL10.alGetError());
      }
    }
    AL10.alSourcePlay(sourceID);
    isPlaying = true;
    player.reload();
  }

  @Override
  public void stop() {
    if (sourceID == -1) return;
    reset();
    player.freeSource(sourceID);
    sourceID = -1;
    renderedSeconds = 0;
    isPlaying = false;
  }

  @Override
  public void pause() {
    if (sourceID != -1) AL10.alSourcePause(sourceID);
    isPlaying = false;
  }

  @Override
  public boolean isPlay() {
    if (sourceID == -1) return false;
    return isPlaying;
  }

  @Override
  public void setLoop(boolean isLoop) {
    this.isLoop = isLoop;
  }

  @Override
  public boolean isLoop() {
    return isLoop;
  }

  @Override
  public void setVolume(double volume) {
    this.volume = volume;
    if (sourceID != -1) {
      AL10.alSourcef(sourceID, AL10.AL_GAIN, (float) volume);
    }
  }

  @Override
  public double getVolume() {
    return this.volume;
  }

  @Override
  public void release() {
    if (buffers == null) return;
    if (sourceID != -1) {
      reset();
      player.music.remove(this);
      player.freeSource(sourceID);
      sourceID = -1;
    }
    AL10.alDeleteBuffers(buffers);
    buffers = null;
  }

  public synchronized void update() {
    if (sourceID == -1) return;
    boolean end = false;
    int buffers = AL10.alGetSourcei(sourceID, AL10.AL_BUFFERS_PROCESSED);
    while (buffers-- > 0) {
      int bufferID = AL10.alSourceUnqueueBuffers(sourceID);
      if (bufferID == AL10.AL_INVALID_VALUE) break;
      renderedSeconds += secondsPerBuffer;
      if (end) continue;
      if (fill(bufferID))
        AL10.alSourceQueueBuffers(sourceID, bufferID);
      else
        end = true;
    }
    if (end && AL10.alGetSourcei(sourceID, AL10.AL_BUFFERS_QUEUED) == 0) {
      stop();
    }
    if (isPlaying && AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) != AL10.AL_PLAYING) AL10.alSourcePlay(sourceID);
  }

  private synchronized void reset() {
    if (bitStream != null) {
      try {
        bitStream.close();
      } catch (BitstreamException skip) {
      }
    }
    bitStream = new Bitstream(Context.getFiles().getStream(file));
    decoder = new MP3Decoder();
    try {
      Header header = bitStream.readFrame();
      if (header != null) {
        int channels = header.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
        outputBuffer = new OutputBuffer(channels, false);
        decoder.setOutputBuffer(outputBuffer);
        this.format = channels > 1 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16;
        this.sampleRate = header.getSampleRate();
        secondsPerBuffer = (double) bufferSize / bytesPerSample / channels / sampleRate;
      } else {
        throw new RuntimeException("empty mp3");
      }
    } catch (BitstreamException e) {
      e.printStackTrace();
    }
  }

  private boolean fill(int bufferID) {
    tempBuffer.clear();
    int length = read(tempBytes);
    if (length <= 0) {
      if (isLoop) {
        reset();
        renderedSeconds = 0;
        length = read(tempBytes);
        if (length <= 0) return false;
      } else
        return false;
    }
    tempBuffer.put(tempBytes, 0, length).flip();
    AL10.alBufferData(bufferID, format, tempBuffer, sampleRate);
    return true;
  }

  public double getDuration() {
    return 10;
  }
}
