package featurea.opengl;

import featurea.app.Context;
import featurea.app.MediaPlayer;
import featurea.graphics.Window;
import featurea.util.Color;
import featurea.util.Colors;
import featurea.util.Size;
import featurea.util.Zoom;

import static featurea.app.Context.gl;

public class Render {

  public final MediaPlayer mediaPlayer;
  public final Size size = new Size();
  public final Zoom zoom = new Zoom();
  public final TexturePacker texturePacker = new TexturePacker();
  public final DefaultTextureManager defaultTextureManager = new DefaultTextureManager();
  public Window window;
  public boolean isScreenMode;
  public boolean isOutline;
  public boolean isReleaseTexturesOnPause = true;
  private final Color background = Colors.black;

  public Render(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }

  public TextureManager getTextureManager() {
    if (mediaPlayer.isProduction()) {
      return texturePacker;
    } else {
      return defaultTextureManager;
    }
  }

  public void release(String file) {
    getTextureManager().release(file);
  }

  public void load(String file) throws TextureNotFoundException {
    getTextureManager().load(file);
  }

  public void resize(int width, int height) {
    size.setValue(width, height);
    gl.glViewport(0, 0, (int) size.width, (int) size.height);
    shiftBatch(0, 0);
  }

  public void shiftBatch(double x, double y) {
    gl.glLoadIdentity();
    gl.glOrthof((float) -x, (float) (-x + size.width), (float) (-y + size.height), (float) (-y + 0), -1, 1);
  }

  public void clearBackground() {
    Context.gl.glClear(OpenGL.GL_COLOR_BUFFER_BIT);
  }

  /**/

  public void onCreate() {
    int[] maxTextureSize = new int[1];
    gl.glGetIntegerv(OpenGL.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
    OpenGLManager.MAX_TEXTURE_SIZE = maxTextureSize[0];
    Context.gl.glClearColor((float) background.r, (float) background.g, (float) background.b, (float) background.a);
  }

  public void onStart() {
    if (isReleaseTexturesOnPause) {
      for (Texture texture : mediaPlayer.getResources().getTextures()) {
        mediaPlayer.loader.load(texture.file);
      }
    }
  }

  public void onStop() {
    if (isReleaseTexturesOnPause) {
      invalidateTextures();
    }
  }

  public void invalidateTextures() {
    for (Texture texture : mediaPlayer.getResources().getTextures()) {
      mediaPlayer.loader.release(texture.file);
    }
  }

  public void onDestroy() {
    /*for (Texture texture : getTextures()) {
      texture.release();
    }*/
    texturePacker.packs.clear();
    defaultTextureManager.textures.clear();
  }

}
