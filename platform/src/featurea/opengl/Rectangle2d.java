package featurea.opengl;

import featurea.util.Color;
import static featurea.app.Context.gl;

import java.nio.FloatBuffer;

public final class Rectangle2d {

  private final FloatBuffer vertexPointer = Render.createFloatBuffer(2 * 3 * 2);
  private final FloatBuffer colorPointer = Render.createFloatBuffer(4 * 3 * 2);

  private void setVertices(double x1, double y1, double x2, double y2) {
    vertexPointer.clear();
    vertexPointer.put((float)x1).put((float)y1);
    vertexPointer.put((float)x1).put((float)y2);
    vertexPointer.put((float)x2).put((float)y2);
    vertexPointer.put((float)x1).put((float)y1);
    vertexPointer.put((float)x2).put((float)y2);
    vertexPointer.put((float)x2).put((float)y1);
    vertexPointer.flip();
  }

  private void setColor(Color color) {
    colorPointer.clear();
    colorPointer.put((float)color.r).put((float)color.g).put((float)color.b).put((float)color.a);
    colorPointer.put((float)color.r).put((float)color.g).put((float)color.b).put((float)color.a);
    colorPointer.put((float)color.r).put((float)color.g).put((float)color.b).put((float)color.a);
    colorPointer.put((float)color.r).put((float)color.g).put((float)color.b).put((float)color.a);
    colorPointer.put((float)color.r).put((float)color.g).put((float)color.b).put((float)color.a);
    colorPointer.put((float)color.r).put((float)color.g).put((float)color.b).put((float)color.a);
    colorPointer.flip();
  }

  public void render(double x1, double y1, double x2, double y2, double angle, Color color) {
    setVertices(x1, y1, x2, y2);
    setColor(color);
    gl.glEnableClientState(OpenGL.GL_VERTEX_ARRAY);
    gl.glVertexPointer(2, OpenGL.GL_FLOAT, 0, vertexPointer);
    gl.glEnableClientState(OpenGL.GL_COLOR_ARRAY);
    gl.glColorPointer(4, OpenGL.GL_FLOAT, 0, colorPointer);
    gl.glPushMatrix();
    gl.glRotatef((float)angle, 0, 0, 1);
    gl.glLineWidth(1f);
    gl.glDrawArrays(OpenGL.GL_TRIANGLES, 0, 6);
    gl.glPopMatrix();
    gl.glDisableClientState(OpenGL.GL_VERTEX_ARRAY);
    gl.glDisableClientState(OpenGL.GL_COLOR_ARRAY);
  }
}
