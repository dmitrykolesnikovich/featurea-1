package featurea.opengl;

import featurea.app.Context;
import featurea.app.MediaPlayer;
import featurea.graphics.Glyph;
import featurea.graphics.Window;
import featurea.util.*;

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
    gl.glViewport(0, 0, width, height);
    gl.glLoadIdentity();
    gl.glOrthof(0, width, height, 0, -1, 1);
  }

  public void setViewport(double x, double y) {
    // no op
  }

  public void drawGlyph(Glyph glyph, double x1, double y1, double x2, double y2, Angle angle, double ox, double oy, Color color, boolean isFlipX, boolean isFlipY) {
    /*Texture texture = mediaPlayer.getResources().getTexture(glyph.font.pngFile);
    if (texture != null) {
      texture.setGlyphToRender(glyph);
      texture.glyphRender.draw(x1, y1, x2, y2, angle, ox, oy, color, isFlipX, isFlipY);
    } else {
      if (!mediaPlayer.isProduction()) {
        mediaPlayer.loader.load(glyph.font.pngFile);
      } else {
        System.out.println("Font not load: " + glyph.font.pngFile);
      }
    }*/
    throw new RuntimeException("Not implemented yet");
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
