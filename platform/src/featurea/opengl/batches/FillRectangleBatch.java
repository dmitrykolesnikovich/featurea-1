package featurea.opengl.batches;

import featurea.graphics.Graphics;
import featurea.opengl.Batch;
import featurea.opengl.OpenGL;
import featurea.util.BufferUtil;
import featurea.util.Color;

import java.nio.FloatBuffer;

import static featurea.app.Context.gl;
import static featurea.opengl.OpenGLUtil.COUNT_OF_VERTICES_FOR_DRAWING_TWO_TRIANGLES;

public final class FillRectangleBatch extends Batch {

  private static final int VERTEX_POINTER_SIZE = 2;
  private static final int COLOR_POINTER_SIZE = 4;

  private FloatBuffer vertexPointer;
  private FloatBuffer colorPointer;

  @Override
  public FillRectangleBatch setCapacity(int capacity) {
    super.setCapacity(capacity);
    vertexPointer = BufferUtil.createFloatBuffer(2 * 3 * 2 * capacity);
    colorPointer = BufferUtil.createFloatBuffer(4 * 3 * 2 * capacity);
    return this;
  }

  public void fillRectangle(Graphics graphics, double x1, double y1, double x2, double y2, Color color) {
    fillRectangle(graphics, x1, y1, x2, y2, 0, color);
  }

  public void fillRectangle(Graphics graphics, double x1, double y1, double x2, double y2, double angle, Color color) {
    // todo make use of angle
    putVertexPointer(x1, y1, x2, y2).putColorPointer(color).finish();
  }

  @Override
  public void clear() {
    super.clear();
    vertexPointer.clear();
    colorPointer.clear();
  }

  @Override
  public FillRectangleBatch build() {
    super.build();
    vertexPointer.flip();
    colorPointer.flip();
    return this;
  }

  @Override
  protected void onDraw(Graphics graphics) {
    super.onDraw(graphics);
    gl.glEnableClientState(OpenGL.GL_VERTEX_ARRAY);
    gl.glVertexPointer(VERTEX_POINTER_SIZE, OpenGL.GL_FLOAT, 0, vertexPointer);
    gl.glEnableClientState(OpenGL.GL_COLOR_ARRAY);
    gl.glColorPointer(COLOR_POINTER_SIZE, OpenGL.GL_FLOAT, 0, colorPointer);
    gl.glPushMatrix();
    gl.glLineWidth(1f);
    gl.glDrawArrays(OpenGL.GL_TRIANGLES, 0, COUNT_OF_VERTICES_FOR_DRAWING_TWO_TRIANGLES * size);
    gl.glPopMatrix();
    gl.glDisableClientState(OpenGL.GL_VERTEX_ARRAY);
    gl.glDisableClientState(OpenGL.GL_COLOR_ARRAY);
  }

  public FillRectangleBatch putVertexPointer(double x1, double y1, double x2, double y2) {
    vertexPointer.put((float) x1).put((float) y1);
    vertexPointer.put((float) x1).put((float) y2);
    vertexPointer.put((float) x2).put((float) y2);
    vertexPointer.put((float) x1).put((float) y1);
    vertexPointer.put((float) x2).put((float) y2);
    vertexPointer.put((float) x2).put((float) y1);
    return this;
  }

  public FillRectangleBatch putColorPointer(Color color) {
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    return this;
  }

}
