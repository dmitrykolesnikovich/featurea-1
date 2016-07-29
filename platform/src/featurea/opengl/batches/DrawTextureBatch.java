package featurea.opengl.batches;

import featurea.app.Context;
import featurea.graphics.Graphics;
import featurea.opengl.Batch;
import featurea.opengl.OpenGL;
import featurea.opengl.Texture;
import featurea.opengl.TextureRectangle;
import featurea.util.*;

import java.nio.FloatBuffer;

import static featurea.app.Context.gl;
import static featurea.opengl.OpenGL.GL_FLOAT;
import static featurea.opengl.OpenGL.GL_TRIANGLES;
import static featurea.opengl.OpenGLUtil.COUNT_OF_VERTICES_FOR_DRAWING_TWO_TRIANGLES;

public class DrawTextureBatch extends Batch {

  private static final int VERTEX_POINTER_COUNT = 2;
  private static final int COLOR_POINTER_COUNT = 4;
  private static final int TEXTURE_COORD_POINTER_COUNT = 2;

  private int currentTextureId = OpenGL.NULL_ID;
  public FloatBuffer vertexPointer;
  public FloatBuffer colorPointer;
  public FloatBuffer texCoordPointer;

  @Override
  public DrawTextureBatch setCapacity(int capacity) {
    super.setCapacity(capacity);
    this.vertexPointer = BufferUtil.createFloatBuffer(2 * 3 * 2 * capacity);
    this.colorPointer = BufferUtil.createFloatBuffer(4 * 3 * 2 * capacity);
    this.texCoordPointer = BufferUtil.createFloatBuffer(2 * 3 * 2 * capacity);
    return this;
  }

  public void drawTexture(String file, double x1, double y1, double x2, double y2, Angle angle,
                          double ox, double oy, Color color, boolean isFlipX, boolean isFlipY) {
    if (containsPessimistically(x1, y1, x2, y2)) {
      if (file != null) {
        Texture texture = Context.getResources().getTexture(file);
        if (texture != null) {
          texture.draw(this, x1, y1, x2, y2, angle, ox, oy, color, isFlipX, isFlipY);
          currentTextureId = texture.getId();
        } else {
          if (!Context.isProduction()) {
            Context.getLoader().load(file);
          } else {
            System.err.println("Texture not load: " + file);
          }
        }
      }
      Context.getPerformance().drawTextureCount++;
    } else {
      // no op
    }
  }

  @Override
  public void clear() {
    super.clear();
    this.texCoordPointer.clear();
    this.vertexPointer.clear();
    this.colorPointer.clear();
  }

  @Override
  public DrawTextureBatch build() {
    super.build();
    texCoordPointer.flip();
    vertexPointer.flip();
    colorPointer.flip();
    return this;
  }

  @Override
  protected void onDraw(Graphics graphics) {
    super.onDraw(graphics);
    gl.bind(currentTextureId);
    gl.glPushMatrix();
    gl.glVertexPointer(VERTEX_POINTER_COUNT, GL_FLOAT, 0, vertexPointer);
    gl.glColorPointer(COLOR_POINTER_COUNT, GL_FLOAT, 0, colorPointer);
    gl.glTexCoordPointer(TEXTURE_COORD_POINTER_COUNT, GL_FLOAT, 0, texCoordPointer);
    gl.glDrawArrays(GL_TRIANGLES, 0, COUNT_OF_VERTICES_FOR_DRAWING_TWO_TRIANGLES * size);
    gl.glPopMatrix();
    gl.unbind();
  }

  public DrawTextureBatch putVertexPointer(TextureRectangle textureRectangle) {
    return putVertexPointer(textureRectangle.point1, textureRectangle.point2, textureRectangle.point3, textureRectangle.point4);
  }

  public DrawTextureBatch putVertexPointer(Vector point1, Vector point2, Vector point3, Vector point4) {
    double x1 = point1.x;
    double y1 = point1.y;
    double x2 = point2.x;
    double y2 = point2.y;
    double x3 = point3.x;
    double y3 = point3.y;
    double x4 = point4.x;
    double y4 = point4.y;
    vertexPointer.put((float) x1).put((float) y1);
    vertexPointer.put((float) x4).put((float) y4);
    vertexPointer.put((float) x3).put((float) y3);
    vertexPointer.put((float) x1).put((float) y1);
    vertexPointer.put((float) x3).put((float) y3);
    vertexPointer.put((float) x2).put((float) y2);
    return this;
  }

  public DrawTextureBatch putColorPointer(Color color) {
    if (color == null) {
      color = Colors.white;
    }
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    return this;
  }

  public DrawTextureBatch putTexCoordPointer(double[] uv) {
    texCoordPointer.put((float) uv[0]).put((float) uv[1]);
    texCoordPointer.put((float) uv[2]).put((float) uv[3]);
    texCoordPointer.put((float) uv[4]).put((float) uv[5]);
    texCoordPointer.put((float) uv[0]).put((float) uv[1]);
    texCoordPointer.put((float) uv[4]).put((float) uv[5]);
    texCoordPointer.put((float) uv[6]).put((float) uv[7]);
    return this;
  }

  /*private API*/

  private boolean containsPessimistically(double x1, double y1, double x2, double y2) {
    Size size = Context.getRender().size;
    double ox = (x1 + x2) / 2;
    double oy = (y1 + y2) / 2;
    double max = Math.max(x2 - x1, y2 - y1);
    return ox - max < size.width && ox + max > 0 && oy - max < size.height && oy + max > 0;
  }

}
