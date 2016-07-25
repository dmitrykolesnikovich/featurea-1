package featurea.opengl;

import featurea.app.Context;
import featurea.app.MediaPlayer;
import featurea.graphics.Glyph;
import featurea.util.*;

import java.nio.FloatBuffer;
import java.util.List;

public class Texture {

  public final String file;
  public final TexturePart part;
  private double x1;
  private double y1;
  private double x2;
  private double y2;
  boolean isLoad;
  public final Texture glyphRender;
  public final FloatBuffer vertexPointer = Render.createFloatBuffer(2 * 3 * 2 * 2);
  public final FloatBuffer colorPointer = Render.createFloatBuffer(4 * 3 * 2 * 2);
  public final FloatBuffer texCoordPointer = Render.createFloatBuffer(2 * 3 * 2 * 2);
  private double[] uv = new double[]{0, 0, 0, 1, 1, 1, 1, 0};
  private boolean isFlipX;
  private boolean isFlipY;

  /*private*/ Texture(String file, TexturePart part, double x1, double y1, double x2, double y2) {
    this.file = file;
    this.part = part;
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    this.glyphRender = new Texture(this);
    setRectangle(x1, y1, x2, y2);
    setColor(Colors.white);
  }

  /*private*/ Texture(Texture texture) {
    this.file = texture.file;
    this.part = texture.part;
    this.x1 = texture.x1;
    this.y1 = texture.y1;
    this.x2 = texture.x2;
    this.y2 = texture.y2;
    this.isLoad = texture.isLoad;
    this.glyphRender = texture.glyphRender;
    setRectangle(x1, y1, x2, y2);
    setColor(Colors.white);
  }

  /*private*/ void load() {
    if (!isLoad) {
      part.load();
      isLoad = true;
    }
  }

  /*private*/ void release() {
    isLoad = false;
    part.release();
  }

  public void setVertices(Vector point1, Vector point2, Vector point3, Vector point4) {
    double x1 = (double) point1.x;
    double y1 = (double) point1.y;
    double x2 = (double) point2.x;
    double y2 = (double) point2.y;
    double x3 = (double) point3.x;
    double y3 = (double) point3.y;
    double x4 = (double) point4.x;
    double y4 = (double) point4.y;

    vertexPointer.clear();
    vertexPointer.put((float) x1).put((float) y1);
    vertexPointer.put((float) x4).put((float) y4);
    vertexPointer.put((float) x3).put((float) y3);
    vertexPointer.put((float) x1).put((float) y1);
    vertexPointer.put((float) x3).put((float) y3);
    vertexPointer.put((float) x2).put((float) y2);
    vertexPointer.flip();
  }

  /*private*/ void setColor(Color color) {
    if (color == null) {
      color = Colors.white;
    }
    colorPointer.clear();
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.put((float) color.r).put((float) color.g).put((float) color.b).put((float) color.a);
    colorPointer.flip();
  }

  public void setUV(double u, double v, double u2, double v2) {
    this.isFlipX = false;
    this.isFlipY = false;
    uv[0] = u;
    uv[1] = v;
    uv[2] = u;
    uv[3] = v2;
    uv[4] = u2;
    uv[5] = v2;
    uv[6] = u2;
    uv[7] = v;
    applyTexCoordPointer(uv);
  }

  private void setRectangle(double x1, double y1, double x2, double y2) {
    double u = (double) ((x1) / part.size.width);
    double u2 = (double) ((x2) / part.size.width);
    double v = (double) ((y1) / part.size.height);
    double v2 = (double) ((y2) / part.size.height);
    setUV(u, v, u2, v2);
  }

  private void flipUV(boolean isFlipX, boolean isFlipY) {
    if (this.isFlipX != isFlipX) {
      this.isFlipX = isFlipX;
      uv = new double[]{
          uv[6], uv[7],
          uv[4], uv[5],
          uv[2], uv[3],
          uv[0], uv[1],
      };
    }
    if (this.isFlipY != isFlipY) {
      this.isFlipY = isFlipY;
      uv = new double[]{
          uv[2], uv[3],
          uv[0], uv[1],
          uv[6], uv[7],
          uv[4], uv[5],
      };
    }
  }

  // temp vars: point1, point2
  private final Vector point1 = new Vector();
  private final Vector point2 = new Vector();
  private final Vector point3 = new Vector();
  private final Vector point4 = new Vector();

  public void draw(double x1, double y1, double x2, double y2, Angle angle, double ox, double oy,
                   Color color, boolean isFlipX, boolean isFlipY, List<Shader> shaders) {

    // todo make use of shaders
    Render render = Context.getRender();
    if (render.part != part || render.isFull()) {
      render.unbind();
      render.bind(part);
    }

    // tx coords
    flipUV(isFlipX, isFlipY);
    applyTexCoordPointer(uv);

    // vertex coords
    point1.setValue(x1, y1);
    point2.setValue(x2, y1);
    point3.setValue(x2, y2);
    point4.setValue(x1, y2);
    if (angle != null && angle.getValue() != 0) {
      Vector.rotate(point1, ox, oy, angle);
      Vector.rotate(point2, ox, oy, angle);
      Vector.rotate(point3, ox, oy, angle);
      Vector.rotate(point4, ox, oy, angle);
    }
    setVertices(point1, point2, point3, point4);

    // color coords
    setColor(color);

    // draw
    render.add(texCoordPointer, vertexPointer, colorPointer);
  }

  /*private*/ void applyTexCoordPointer(double[] uv) {
    texCoordPointer.clear();
    texCoordPointer.put((float) uv[0]).put((float) uv[1]);
    texCoordPointer.put((float) uv[2]).put((float) uv[3]);
    texCoordPointer.put((float) uv[4]).put((float) uv[5]);
    texCoordPointer.put((float) uv[0]).put((float) uv[1]);
    texCoordPointer.put((float) uv[4]).put((float) uv[5]);
    texCoordPointer.put((float) uv[6]).put((float) uv[7]);
    texCoordPointer.flip();
  }

  public double getWidth() {
    return (double) Math.abs((u2() - u1()) * part.size.width);
  }

  public double getHeight() {
    return (double) Math.abs((v2() - v1()) * part.size.height);
  }

  public double u1() {
    return uv[0];
  }

  public double v1() {
    return uv[1];
  }

  public double u2() {
    return uv[4];
  }

  public double v2() {
    return uv[5];
  }

  public boolean isLoad() {
    return isLoad;
  }

  public int getId() {
    return part.id;
  }

  public void setGlyphToRender(Glyph glyph) {
    double glyph_u1 = u1() + (u2() - u1()) * glyph.u;
    double glyph_u2 = u1() + (u2() - u1()) * glyph.u2;
    double glyph_v1 = v1() + (v2() - v1()) * glyph.v;
    double glyph_v2 = v1() + (v2() - v1()) * glyph.v2;
    this.glyphRender.setUV(glyph_u1, glyph_v1, glyph_u2, glyph_v2);
  }

  private final Size size = new Size();

  public Size getSize() {
    size.setValue(getWidth(), getHeight());
    return size;
  }

}