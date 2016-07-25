package featurea.opengl;

import featurea.util.Color;

import java.nio.FloatBuffer;
import static featurea.app.Context.gl;

public final class Line2d {

  private final FloatBuffer vertexPointer = Render.createFloatBuffer(2 * 2);
  private final FloatBuffer colorPointer = Render.createFloatBuffer(2 * 4);

  public void setValue(double x1, double y1, double x2, double y2) {
    vertexPointer.clear();
    vertexPointer.put(0, (float)x1);
    vertexPointer.put(1, (float)y1);
    vertexPointer.put(2, (float)x2);
    vertexPointer.put(3, (float)y2);
    vertexPointer.flip();
  }

  public void setColor(Color color) {
    colorPointer.clear();
    colorPointer.put(0, (float)color.r);
    colorPointer.put(1, (float)color.g);
    colorPointer.put(2, (float)color.b);
    colorPointer.put(3, (float)color.a);
    colorPointer.put(4, (float)color.r);
    colorPointer.put(5, (float)color.g);
    colorPointer.put(6, (float)color.b);
    colorPointer.put(7, (float)color.a);
    colorPointer.flip();
  }

  public void render() {
    gl.glEnableClientState(OpenGL.GL_VERTEX_ARRAY);
    gl.glVertexPointer(2, OpenGL.GL_FLOAT, 0, vertexPointer);
    gl.glEnableClientState(OpenGL.GL_COLOR_ARRAY);
    gl.glColorPointer(4, OpenGL.GL_FLOAT, 0, colorPointer);
    gl.glLineWidth(1f);
    gl.glDrawArrays(OpenGL.GL_LINES, 0, 2);
    gl.glDisableClientState(OpenGL.GL_VERTEX_ARRAY);
    gl.glDisableClientState(OpenGL.GL_COLOR_ARRAY);
  }

}
