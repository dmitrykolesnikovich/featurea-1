package featurea.opengl;

import featurea.util.BufferUtil;

import java.nio.FloatBuffer;

import static featurea.app.Context.gl;
import static featurea.opengl.OpenGL.GL_FLOAT;
import static featurea.opengl.OpenGL.GL_TRIANGLES;
import static featurea.opengl.OpenGLManager.*;

public class Batch {

  public Batch() {
    setBatchSize(1_000);
  }

  private int batchSize;
  private int count;
  private FloatBuffer vertexPointer;
  private FloatBuffer colorPointer;
  private FloatBuffer texCoordPointer;

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
    vertexPointer = BufferUtil.createFloatBuffer(2 * 3 * 2 * batchSize);
    colorPointer = BufferUtil.createFloatBuffer(4 * 3 * 2 * batchSize);
    texCoordPointer = BufferUtil.createFloatBuffer(2 * 3 * 2 * batchSize);
  }

  private void flip() {
    texCoordPointer.flip();
    vertexPointer.flip();
    colorPointer.flip();
  }

  public void add(FloatBuffer texCoordPointer, FloatBuffer vertexPointer, FloatBuffer colorPointer) {
    this.texCoordPointer.put(texCoordPointer);
    this.vertexPointer.put(vertexPointer);
    this.colorPointer.put(colorPointer);
    count++;
  }

  public void flush() {
    flip();
    gl.glPushMatrix();
    gl.glVertexPointer(VERTEX_POINTER_COUNT, GL_FLOAT, 0, vertexPointer);
    gl.glColorPointer(COLOR_POINTER_COUNT, GL_FLOAT, 0, colorPointer);
    gl.glTexCoordPointer(TEXTURE_COORD_POINTER_COUNT, GL_FLOAT, 0, texCoordPointer);
    gl.glDrawArrays(GL_TRIANGLES, 0, COUNT_OF_VERTICES_FOR_DRAWING_TWO_TRIANGLES * count);
    gl.glPopMatrix();
    clear();
  }

  private void clear() {
    this.texCoordPointer.clear();
    this.vertexPointer.clear();
    this.colorPointer.clear();
    count = 0;
  }

  public boolean isFull() {
    return count == batchSize;
  }

  public int getBatchSize() {
    return batchSize;
  }

}
