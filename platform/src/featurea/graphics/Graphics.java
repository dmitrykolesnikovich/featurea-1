package featurea.graphics;

import com.sun.istack.internal.Nullable;
import featurea.app.Area;
import featurea.app.Context;
import featurea.app.Layer;
import featurea.app.Projection;
import featurea.opengl.Shader;
import featurea.opengl.Texture;
import featurea.util.Angle;
import featurea.util.Color;
import featurea.util.Vector;

import java.util.List;

public final class Graphics {

  public final Layer layer;

  public Graphics(Layer layer) {
    this.layer = layer;
  }

  public void drawLine(double x1, double y1, double x2, double y2, Color color) {
    x1 = layer.toScreenX(x1);
    x2 = layer.toScreenX(x2);
    y1 = layer.toScreenY(y1);
    y2 = layer.toScreenY(y2);
    Context.getRender().drawLine(x1, y1, x2, y2, color);
  }

  public void drawRectangle(double x1, double y1, double x2, double y2, Color color) {
    x1 = layer.toScreenX(x1);
    x2 = layer.toScreenX(x2);
    y1 = layer.toScreenY(y1);
    y2 = layer.toScreenY(y2);
    Context.getRender().drawRectangle(x1, y1, x2, y2, color);
  }

  public void fillRectangle(double x1, double y1, double x2, double y2, Color color) {
    x1 = layer.toScreenX(x1);
    x2 = layer.toScreenX(x2);
    y1 = layer.toScreenY(y1);
    y2 = layer.toScreenY(y2);
    Context.getRender().fillRectangle(x1, y1, x2, y2, color);
  }

  public final void drawTexture(String file, double x1, double y1, double x2, double y2, Angle angle, double ox, double oy, Color color, boolean flipX, boolean flipY, List<Shader> shaders) {
    x1 = layer.toScreenX(x1);
    y1 = layer.toScreenY(y1);
    x2 = layer.toScreenX(x2);
    y2 = layer.toScreenY(y2);
    ox = layer.toScreenX(ox);
    oy = layer.toScreenY(oy);
    Context.getRender().drawTexture(file, x1, y1, x2, y2, angle, ox, oy, color, flipX, flipY, shaders);
  }


  // devnote: http://stackoverflow.com/questions/662107/how-to-use-gl-repeat-to-repeat-only-a-selection-of-a-texture-atlas-opengl
  public final void drawTile(String file, double x1, double y1, double x2, double y2, Angle angle, double ox, double oy, Color color, boolean flipX, boolean flipY, List<Shader> shaders) {
    Texture texture = Context.getResources().getTexture(file);
    if (texture != null) {
      double width = texture.getWidth();
      double height = texture.getHeight();

      if (x2 > x1) {
        for (double x = x1; x < x2; x += width) {
          if (y2 > y1) {
            for (double y = y1; y < y2; y += height) {
              drawTexture(file, x, y, x + width, y + height, angle, ox, oy, color, flipX, flipY, shaders);
            }
          } else {
            for (double y = y1; y > y2; y -= height) {
              drawTexture(file, x, (y - height), x + width, y, angle, ox, oy, color, flipX, flipY, shaders);
            }
          }
        }
      } else {
        for (double x = x2; x > x1; x -= width) {
          if (y2 > y1) {
            for (double y = y1; y < y2; y += height) {
              drawTexture(file, x - width, y, x, y + height, angle, ox, oy, color, flipX, flipY, shaders);
            }
          } else {
            for (double y = y1; y > y2; y -= height) {
              drawTexture(file, x - width, (y - height), x, y, angle, ox, oy, color, flipX, flipY, shaders);
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

  public void drawCircle(double ox, double oy, double radiusX, double radiusY, Color color, Angle angle) {
    ox = layer.toScreenX(ox);
    oy = layer.toScreenY(oy);
    radiusX = layer.toScreenLength(radiusX);
    radiusY = layer.toScreenLength(radiusY);
    Context.getRender().drawCircle(ox, oy, radiusX, radiusY, color, angle);
    double x2 = ox + radiusX * angle.cos();
    double y2 = oy + radiusY * angle.sin();
    Context.getRender().drawLine(ox, oy, x2, y2, color);
  }

  public void drawProjection(Projection<? extends Area> projection) {
    for (Area area : projection) {
      area.onDraw(this);
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

  private static final Vector EMPTY_VECTOR = new Vector();

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

}
