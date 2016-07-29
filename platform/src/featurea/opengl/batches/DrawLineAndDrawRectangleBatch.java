package featurea.opengl.batches;

import featurea.graphics.Graphics;
import featurea.opengl.Batch;
import featurea.opengl.OpenGL;
import featurea.util.BufferUtil;
import featurea.util.Color;

import java.nio.FloatBuffer;

import static featurea.app.Context.gl;
import static featurea.opengl.OpenGLUtil.COUNT_OF_VERTICES_TO_DRAW_ONE_LINE;

public final class DrawLineAndDrawRectangleBatch extends Batch {

  private static final int VERTEX_POINTER_SIZE = 2;
  private static final int COLOR_POINTER_SIZE = 4;

  private FloatBuffer vertexPointer;
  private FloatBuffer colorPointer;

  @Override
  public DrawLineAndDrawRectangleBatch setCapacity(int capacity) {
    super.setCapacity(capacity);
    vertexPointer = BufferUtil.createFloatBuffer(2 * 2 * capacity);
    colorPointer = BufferUtil.createFloatBuffer(4 * 2 * capacity);
    return this;
  }

  public void drawLine(double x1, double y1, double x2, double y2, Color color) {
    putVertexPointer(x1, y1, x2, y2).putColorPointer(color).finish();
  }

  // todo improve drawRectangle method
  public void drawRectangle(double x1, double y1, double x2, double y2, Color color) {
    drawLine(x1, y1, x2, y1, color);
    drawLine(x2, y1, x2, y2, color);
    drawLine(x2, y2, x1, y2, color);
    drawLine(x1, y2, x1, y1, color);
  }

  @Override
  public void clear() {
    super.clear();
    vertexPointer.clear();
    colorPointer.clear();
  }

  @Override
  public DrawLineAndDrawRectangleBatch build() {
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
    gl.glLineWidth(1f);
    gl.glDrawArrays(OpenGL.GL_LINES, 0, COUNT_OF_VERTICES_TO_DRAW_ONE_LINE * size);
    gl.glDisableClientState(OpenGL.GL_VERTEX_ARRAY);
    gl.glDisableClientState(OpenGL.GL_COLOR_ARRAY);
  }

  public DrawLineAndDrawRectangleBatch putVertexPointer(double x1, double y1, double x2, double y2) {
    vertexPointer.put(0, (float) x1);
    vertexPointer.put(1, (float) y1);
    vertexPointer.put(2, (float) x2);
    vertexPointer.put(3, (float) y2);
    return this;
  }

  public DrawLineAndDrawRectangleBatch putColorPointer(Color color) {
    colorPointer.put(0, (float) color.r);
    colorPointer.put(1, (float) color.g);
    colorPointer.put(2, (float) color.b);
    colorPointer.put(3, (float) color.a);
    colorPointer.put(4, (float) color.r);
    colorPointer.put(5, (float) color.g);
    colorPointer.put(6, (float) color.b);
    colorPointer.put(7, (float) color.a);
    return this;
  }

}
