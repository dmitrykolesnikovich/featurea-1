package featurea.graphics;

import com.sun.istack.internal.Nullable;
import featurea.app.Context;
import featurea.app.Layer;
import featurea.opengl.Batch;
import featurea.opengl.Texture;
import featurea.opengl.batches.DrawLineAndDrawRectangleBatch;
import featurea.opengl.batches.DrawTextureBatch;
import featurea.opengl.batches.FillRectangleBatch;
import featurea.opengl.batches.FillShapeBatch;
import featurea.util.Angle;
import featurea.util.Color;
import featurea.util.Vector;
import featurea.xml.XmlResource;

import java.util.ArrayList;
import java.util.List;

public class Graphics implements XmlResource {

  private static final Vector EMPTY_VECTOR = new Vector();

  private boolean isDirty;
  private final List<Batch> batches = new ArrayList<>();
  private DrawLineAndDrawRectangleBatch drawLineAndDrawRectangleBach;
  private FillRectangleBatch fillRectangleBatch;
  private FillShapeBatch fillShapeBatch;
  private DrawTextureBatch drawTextureBatch;
  private Layer layer;

  protected boolean isShown() {
    return true;
  }

  public void setDirty(boolean isDirty) {
    this.isDirty = isDirty;
  }

  public boolean isDirty() {
    return isDirty;
  }

  public DrawLineAndDrawRectangleBatch getDrawLineAndDrawRectangleBach() {
    return drawLineAndDrawRectangleBach;
  }

  public Graphics setDrawLineAndDrawRectangleBach(DrawLineAndDrawRectangleBatch drawLineAndDrawRectangleBach) {
    this.drawLineAndDrawRectangleBach = drawLineAndDrawRectangleBach;
    return this;
  }

  public FillRectangleBatch getFillRectangleBatch() {
    return fillRectangleBatch;
  }

  public Graphics setFillRectangleBatch(FillRectangleBatch fillRectangleBatch) {
    this.fillRectangleBatch = fillRectangleBatch;
    return this;
  }

  public FillShapeBatch getFillShapeBatch() {
    return fillShapeBatch;
  }

  public Graphics setFillShapeBatch(FillShapeBatch fillShapeBatch) {
    this.fillShapeBatch = fillShapeBatch;
    return this;
  }

  public DrawTextureBatch getDrawTextureBatch() {
    return drawTextureBatch;
  }

  public Graphics setDrawTextureBatch(DrawTextureBatch drawTextureBatch) {
    this.drawTextureBatch = drawTextureBatch;
    return this;
  }

  @Override
  public Graphics build() {
    validateAttributes();
    batches.clear();
    if (drawLineAndDrawRectangleBach != null) {
      batches.add(drawLineAndDrawRectangleBach);
    }
    if (fillRectangleBatch != null) {
      batches.add(fillRectangleBatch);
    }
    if (fillShapeBatch != null) {
      batches.add(fillShapeBatch);
    }
    if (drawTextureBatch != null) {
      batches.add(drawTextureBatch);
    }
    return this;
  }

  public List<Batch> getBatches() {
    return batches;
  }


  public Layer getLayer() {
    return layer;
  }

  public Graphics setLayer(Layer layer) {
    this.layer = layer;
    return this;
  }

  public boolean containsDrawLine() {
    if (drawLineAndDrawRectangleBach == null) {
      throw new RuntimeException("Batch not found: drawLineAndDrawRectangleBach");
    }
    return !drawLineAndDrawRectangleBach.isDirty();
  }

  public boolean containsDrawRectangle() {
    return containsDrawLine(); // todo separate
  }

  public boolean containsFillRectangle() {
    if (fillRectangleBatch == null) {
      throw new RuntimeException("Batch not found: fillRectangleBatch");
    }
    return !fillRectangleBatch.isDirty();
  }

  public boolean containsFillShape() {
    if (fillShapeBatch == null) {
      throw new RuntimeException("Batch not found: fillShapeBatch");
    }
    return !fillShapeBatch.isDirty();
  }

  public boolean containsDrawTexture() {
    if (drawTextureBatch == null) {
      throw new RuntimeException("Batch not found: drawTextureBatch");
    }
    return !drawTextureBatch.isDirty();
  }

  /**/

  public void drawLine(double x1, double y1, double x2, double y2, Color color) {
    if (containsDrawLine()) {
      throw new RuntimeException("To use drawLine batch you should clear it first: GraphicsBuffer.clearDrawLine()");
    }
    if (layer != null) {
      x1 = layer.toScreenX(x1);
      x2 = layer.toScreenX(x2);
      y1 = layer.toScreenY(y1);
      y2 = layer.toScreenY(y2);
    }
    drawLineAndDrawRectangleBach.drawLine(x1, y1, x2, y2, color);
  }

  public void drawRectangle(double x1, double y1, double x2, double y2, Color color) {
    if (containsDrawRectangle()) {
      throw new RuntimeException("To use drawRectangle batch you should clear it first: GraphicsBuffer.clearDrawRectangle()");
    }
    if (layer != null) {
      x1 = layer.toScreenX(x1);
      x2 = layer.toScreenX(x2);
      y1 = layer.toScreenY(y1);
      y2 = layer.toScreenY(y2);
    }
    drawLineAndDrawRectangleBach.drawRectangle(x1, y1, x2, y2, color);
  }

  public void fillRectangle(double x1, double y1, double x2, double y2, Color color) {
    if (containsFillRectangle()) {
      throw new RuntimeException("To use fillRectangle batch you should clear it first: GraphicsBuffer.clearFillRectangle()");
    }
    if (layer != null) {
      x1 = layer.toScreenX(x1);
      x2 = layer.toScreenX(x2);
      y1 = layer.toScreenY(y1);
      y2 = layer.toScreenY(y2);
    }
    fillRectangleBatch.fillRectangle(x1, y1, x2, y2, color);
  }

  public final void drawTexture(String file, double x1, double y1, double x2, double y2, Angle angle, double ox, double oy, Color color, boolean flipX, boolean flipY) {
    if (containsDrawTexture()) {
      throw new RuntimeException("To use drawTexture batch you should clear it first: GraphicsBuffer.clearDrawTexture()");
    }
    if (layer != null) {
      x1 = layer.toScreenX(x1);
      y1 = layer.toScreenY(y1);
      x2 = layer.toScreenX(x2);
      y2 = layer.toScreenY(y2);
      ox = layer.toScreenX(ox);
      oy = layer.toScreenY(oy);
    }
    drawTextureBatch.drawTexture(file, x1, y1, x2, y2, angle, ox, oy, color, flipX, flipY);
  }

  // devnote: http://stackoverflow.com/questions/662107/how-to-use-gl-repeat-to-repeat-only-a-selection-of-a-texture-atlas-opengl
  public final void drawTile(String file, double x1, double y1, double x2, double y2, Angle angle, double ox, double oy, Color color, boolean flipX, boolean flipY) {
    Texture texture = Context.getResources().getTexture(file);
    if (texture != null) {
      double width = texture.getWidth();
      double height = texture.getHeight();

      if (x2 > x1) {
        for (double x = x1; x < x2; x += width) {
          if (y2 > y1) {
            for (double y = y1; y < y2; y += height) {
              drawTexture(file, x, y, x + width, y + height, angle, ox, oy, color, flipX, flipY);
            }
          } else {
            for (double y = y1; y > y2; y -= height) {
              drawTexture(file, x, (y - height), x + width, y, angle, ox, oy, color, flipX, flipY);
            }
          }
        }
      } else {
        for (double x = x2; x > x1; x -= width) {
          if (y2 > y1) {
            for (double y = y1; y < y2; y += height) {
              drawTexture(file, x - width, y, x, y + height, angle, ox, oy, color, flipX, flipY);
            }
          } else {
            for (double y = y1; y > y2; y -= height) {
              drawTexture(file, x - width, (y - height), x, y, angle, ox, oy, color, flipX, flipY);
            }
          }
        }
      }
    } else {
      if (!Context.isProduction()) {
        Context.getLoader().load(file);
      } else {
        System.out.println("Texture not load: " + file);
      }
    }
  }

  public void drawText(Text text, double ox, double oy, Angle angle, double scaleX, double scaleY, boolean isFlipX, boolean isFlipY) {
    if (text.font != null) {
      if (text.font.pngFile != null) {
        if (text.string != null) {
          List<Text.Letter> letters = text.onLayoutLetters(ox, oy, scaleX, scaleY);
          for (Text.Letter letter : letters) {
            letter.drawOnLayer(this, angle, ox, oy, letter.color, isFlipX, isFlipY);
          }
        }
      } else {
        if (!Context.isProduction()) {
          text.font.setFntFile(text.font.fntFile);
        } else {
          System.out.println("breakpoint");
        }
      }
    }
  }

  public void drawPoints(boolean isLine, Color color, @Nullable Vector position, double... points) {
    if (position == null) {
      position = EMPTY_VECTOR;
    }
    for (int i = 0; i < points.length; i += 2) {
      if (isLine && i + 2 >= points.length) {
      } else {
        double x1 = position.x + points[i];
        double y1 = position.y + points[i + 1];
        double x2 = position.x + points[(i + 2) % points.length];
        double y2 = position.y + points[(i + 3) % points.length];
        drawLine(x1, y1, x2, y2, color);
      }
    }
  }

  private void validateAttributes() {
    if (layer == null) {
      throw new IllegalStateException("layer == null");
    }
  }

}
