package featurea.opengl;

import featurea.app.Context;
import featurea.app.Project;
import featurea.util.Size;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static featurea.app.Context.gl;
import static featurea.opengl.OpenGL.NULL_ID;

public class TexturePart {

  public final Map<String, Texture> textures = new HashMap<>();
  public final String partFile;
  public Size size;
  public int id;

  public TexturePart(String partFile) {
    this.partFile = partFile;
  }

  void load() {
    if (!isLoad()) {
      long t1 = System.currentTimeMillis();
      ByteBuffer temp = ByteBuffer.allocateDirect(4);
      temp.order(ByteOrder.nativeOrder());
      IntBuffer intBuffer = temp.asIntBuffer();
      gl.glGenTextures(1, intBuffer);
      id = intBuffer.get(0);
      if (id == NULL_ID) {
        throw new RuntimeException("Texture is not loaded successfully");
      }
      gl.bind(id);
      size = Context.gl.loadTexture(partFile);
      if (size.width > OpenGLManager.MAX_TEXTURE_SIZE || size.height > OpenGLManager.MAX_TEXTURE_SIZE) {
        throw new IllegalArgumentException("File \"" + partFile + "\" has size = " + size +
            " that is greater than max texture size allowed: " + OpenGLManager.MAX_TEXTURE_SIZE);
      }
      gl.unbind();
      long t2 = System.currentTimeMillis();
      System.out.println("[TexturePart.load]  \"" + partFile + "\": " + (t2 - t1) + " ms, texture id = " + id);

      markAllTexturesInPartAsLoadedForcely();
    }
  }

  private void markAllTexturesInPartAsLoadedForcely() {
    for (Texture texture : textures.values()) {
      texture.setLoad(true);
    }
  }

  void release() {
    if (!hasLoadedTextures()) {
      System.out.println("[TexturePart.release] " + partFile);
      gl.glDeleteTextures(1, new int[]{id}, 0);
      id = NULL_ID;
    }
  }

  public boolean isLoad() {
    return id != NULL_ID;
  }

  private boolean hasLoadedTextures() {
    for (Texture texture : textures.values()) {
      if (texture.isLoad()) {
        return true;
      }
    }
    return false;
  }

  public double getWidth() {
    return (double) size.width;
  }

  public double getHeight() {
    return (double) size.height;
  }

}
