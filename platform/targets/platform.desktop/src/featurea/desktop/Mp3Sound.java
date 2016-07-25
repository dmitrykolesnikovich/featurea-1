package featurea.desktop;

import featurea.app.Context;
import featurea.audio.SoundDriver;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.MP3Decoder;
import javazoom.jl.decoder.OutputBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Mp3Sound implements SoundDriver {
  private int bufferID = -1;
  private final OpenALImpl audioFactory;
  private double duration = 1;

  public Mp3Sound(OpenALImpl player, String file) {
    this.audioFactory = player;
    ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
    Bitstream bitstream = new Bitstream(Context.getFiles().getStream(file));
    MP3Decoder decoder = new MP3Decoder();
    try {
      OutputBuffer outputBuffer = null;
      int sampleRate = -1, channels = -1;
      while (true) {
        Header header = bitstream.readFrame();
        if (header == null) break;
        if (outputBuffer == null) {
          channels = header.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
          outputBuffer = new OutputBuffer(channels, false);
          decoder.setOutputBuffer(outputBuffer);
          sampleRate = header.getSampleRate();
        }
        try {
          decoder.decodeFrame(header, bitstream);
        } catch (Exception ignored) {
        }
        bitstream.closeFrame();
        output.write(outputBuffer.getBuffer(), 0, outputBuffer.reset());
      }
      bitstream.close();
      setup(output.toByteArray(), channels, sampleRate);
    } catch (Throwable ex) {
      throw new RuntimeException("Error reading audio data.", ex);
    }
  }

  @Override
  public long playNewStream() {
    return playNewStream(1);
  }

  @Override
  public long playNewStream(double volume) {
    int sourceID = audioFactory.obtainSource(false);
    if (sourceID == -1) {
      audioFactory.retain(this, true);
      sourceID = audioFactory.obtainSource(false);
    } else {
      audioFactory.retain(this, false);
    }
    if (sourceID == -1) return -1;
    long soundId = audioFactory.getSoundId(sourceID);
    AL10.alSourcei(sourceID, AL10.AL_BUFFER, bufferID);
    AL10.alSourcei(sourceID, AL10.AL_LOOPING, AL10.AL_FALSE);
    AL10.alSourcef(sourceID, AL10.AL_GAIN, (float)volume);
    AL10.alSourcePlay(sourceID);
    return soundId;
  }

  @Override
  public long loopNewStream() {
    return loopNewStream(1);
  }

  @Override
  public long loopNewStream(double volume) {
    int sourceID = audioFactory.obtainSource(false);
    if (sourceID == -1) {
      return -1;
    }
    long soundId = audioFactory.getSoundId(sourceID);
    AL10.alSourcei(sourceID, AL10.AL_BUFFER, bufferID);
    AL10.alSourcei(sourceID, AL10.AL_LOOPING, AL10.AL_TRUE);
    AL10.alSourcef(sourceID, AL10.AL_GAIN, (float)volume);
    AL10.alSourcePlay(sourceID);
    return soundId;
  }

  @Override
  public void stopAllStreams() {
    audioFactory.stopSourcesWithBuffer(bufferID);
  }

  @Override
  public void releaseAllStreams() {
    if (bufferID != -1) {
      audioFactory.freeBuffer(bufferID);
      AL10.alDeleteBuffers(bufferID);
      bufferID = -1;
      audioFactory.forget(this);
    }
  }

  @Override
  public void stopStream(long streamId) {
    audioFactory.stopSound(streamId);
  }

  @Override
  public void pauseAllStreams() {
    audioFactory.pauseSourcesWithBuffer(bufferID);
  }

  @Override
  public void pauseStream(long streamId) {
    audioFactory.pauseSound(streamId);
  }

  @Override
  public void resumeAllStreams() {
    audioFactory.resumeSourcesWithBuffer(bufferID);
  }

  @Override
  public void resumeStream(long streamId) {
    audioFactory.resumeSound(streamId);
  }

  @Override
  public void setVolumeStream(long streamId, double volume) {
    audioFactory.setSoundGain(streamId, volume);
  }

  @Override
  public void setLoopStream(long streamId, boolean isLoop) {
    audioFactory.setSoundLooping(streamId, isLoop);
  }

  protected void setup(byte[] pcm, int channels, int sampleRate) {
    int bytes = pcm.length - (pcm.length % (channels > 1 ? 4 : 2));
    int samples = bytes / (2 * channels);
    duration = samples / (double) sampleRate;
    ByteBuffer buffer = (ByteBuffer) BufferUtils.createByteBuffer(bytes).put(pcm, 0, bytes).flip();
    if (bufferID == -1) {
      bufferID = AL10.alGenBuffers();
      AL10.alBufferData(bufferID, channels > 1 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16, buffer.asShortBuffer(), sampleRate);
    }
  }
}
