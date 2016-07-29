package featurea.opengl.batches;

import featurea.graphics.Graphics;
import featurea.opengl.Batch;
import featurea.opengl.OpenGL;
import featurea.util.BufferUtil;
import featurea.util.Color;

import java.nio.FloatBuffer;

import static featurea.app.Context.gl;
import static featurea.opengl.OpenGLUtil.COUNT_OF_VERTICES_FOR_DRAWING_TWO_TRIANGLES;

public class FillShapeBatch extends Batch {

  private static final int VERTEX_POINTER_SIZE = 2;
  private static final int COLOR_POINTER_SIZE = 4;

  private FloatBuffer vertexPointer;
  private FloatBuffer colorPointer;

  @Override
  public FillShapeBatch setCapacity(int capacity) {
    super.setCapacity(capacity);
    vertexPointer = BufferUtil.createFloatBuffer(2 * 3 * capacity);
    colorPointer = BufferUtil.createFloatBuffer(4 * 3 * capacity);
    return this;
  }

  public void fillShape(double[] points, Color color) {
    int count = getCount(points);
    putVertexPointer(points, count).putColorPointer(color, count).finish();
  }

  @Override
  public void clear() {
    super.clear();
    vertexPointer.clear();
    colorPointer.clear();
  }

  @Override
  public FillShapeBatch build() {
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
    gl.glDrawArrays(OpenGL.GL_TRIANGLES, 0, COUNT_OF_VERTICES_FOR_DRAWING_TWO_TRIANGLES * this.size);
    gl.glPopMatrix();
    gl.glDisableClientState(OpenGL.GL_VERTEX_ARRAY);
    gl.glDisableClientState(OpenGL.GL_COLOR_ARRAY);
  }

  /*draw params*/

  public FillShapeBatch putVertexPointer(double[] points, int count) {
    int first = 2, second = 3;
    for (int i = 0; i < count; i++) {
      vertexPointer.put((float) points[0]).put((float) points[1]);
      vertexPointer.put((float) points[first]).put((float) points[second]);
      first += 2;
      second += 2;
      vertexPointer.put((float) points[first]).put((float) points[second]);
    }
    return this;
  }

  public FillShapeBatch putColorPointer(Color color, int count) {
    for (int i = 0; i < count; i++) {
      colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
      colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
      colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    }
    return this;
  }

  /*private API*/

  private int getCount(double[] points) {
    return points.length / 2 - 2;
  }

}
