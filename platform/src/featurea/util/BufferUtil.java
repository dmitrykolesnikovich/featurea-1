package featurea.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public final class BufferUtil {

  private BufferUtil() {
    // no op
  }

  public static FloatBuffer createFloatBuffer(int size) {
    return ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  }

}
