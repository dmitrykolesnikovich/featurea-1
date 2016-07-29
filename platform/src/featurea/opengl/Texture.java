package featurea.opengl;

import featurea.opengl.batches.DrawTextureBatch;
import featurea.util.Angle;
import featurea.util.Color;
import featurea.util.Size;

public class Texture {

  public final String file;
  public final TexturePart part;
  private double x1;
  private double y1;
  private double x2;
  private double y2;
  private boolean isLoad;
  private double[] uv = new double[]{0, 0, 0, 1, 1, 1, 1, 0};
  private boolean isFlipX;
  private boolean isFlipY;

  // in common use: size, point1, point2, point3, point4
  private final Size size = new Size();

  /*private*/ Texture(String file, TexturePart part, double x1, double y1, double x2, double y2) {
    this.file = file;
    this.part = part;
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    setRectangle(x1, y1, x2, y2);
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
  }

  private void setRectangle(double x1, double y1, double x2, double y2) {
    double u = x1 / part.size.width;
    double u2 = x2 / part.size.width;
    double v = y1 / part.size.height;
    double v2 = y2 / part.size.height;
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

  public void draw(DrawTextureBatch drawTextureBatch, double x1, double y1, double x2, double y2, Angle angle,
                   double ox, double oy, Color color, boolean isFlipX, boolean isFlipY) {
    flipUV(isFlipX, isFlipY);
    OpenGLUtil.textureRectangle.setValue(x1, y1, x2, y2).rotate(ox, oy, angle);
    drawTextureBatch.putTexCoordPointer(uv).
        putVertexPointer(OpenGLUtil.textureRectangle).
        putColorPointer(color).
        finish();
  }


  public double getWidth() {
    return Math.abs((u2() - u1()) * part.size.width);
  }

  public double getHeight() {
    return Math.abs((v2() - v1()) * part.size.height);
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

  public Size getSize() {
    size.setValue(getWidth(), getHeight());
    return size;
  }

  public void setLoad(boolean isLoad) {
    this.isLoad = isLoad;
  }
}