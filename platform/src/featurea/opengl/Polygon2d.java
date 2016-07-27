package featurea.opengl;

import featurea.util.BufferUtil;
import featurea.util.Color;

import java.nio.FloatBuffer;
import static featurea.app.Context.gl;

public class Polygon2d {

  private double[] points;
  private FloatBuffer vertexPointer;
  private FloatBuffer colorPointer;

  public void setVertices(double[] points) {
    this.points = points;
    vertexPointer = BufferUtil.createFloatBuffer(2 * 3 * count());
    vertexPointer.clear();
    int first = 2, second = 3;
    for (int i = 0; i < count(); i++) {
      vertexPointer.put((float)points[0]).put((float)points[1]);
      vertexPointer.put((float)points[first]).put((float)points[second]);
      first += 2;
      second += 2;
      vertexPointer.put((float)points[first]).put((float)points[second]);
    }
    vertexPointer.flip();
  }

  private void setColor(Color color) {
    colorPointer = BufferUtil.createFloatBuffer(4 * 3 * count());
    colorPointer.clear();
    for (int i = 0; i < count(); i++) {
      colorPointer.put((float)color.r).put((float)color.g).put((float)color.b).put((float)color.a);
      colorPointer.put((float)color.r).put((float)color.g).put((float)color.b).put((float)color.a);
      colorPointer.put((float)color.r).put((float)color.g).put((float)color.b).put((float)color.a);
    }
    colorPointer.flip();
  }

  private int count() {
    return points.length / 2 - 2;
  }

  public void render(double[] points, Color color) {
    setVertices(points);
    render(color);
  }

  public void render(Color color) {
    setColor(color);
    gl.glEnableClientState(OpenGL.GL_VERTEX_ARRAY);
    gl.glVertexPointer(2, OpenGL.GL_FLOAT, 0, vertexPointer);
    gl.glEnableClientState(OpenGL.GL_COLOR_ARRAY);
    gl.glColorPointer(4, OpenGL.GL_FLOAT, 0, colorPointer);
    gl.glPushMatrix();
    gl.glLineWidth(1f);
    gl.glDrawArrays(OpenGL.GL_TRIANGLES, 0, 6);
    gl.glPopMatrix();
    gl.glDisableClientState(OpenGL.GL_VERTEX_ARRAY);
    gl.glDisableClientState(OpenGL.GL_COLOR_ARRAY);
  }
}
