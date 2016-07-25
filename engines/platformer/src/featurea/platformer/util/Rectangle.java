package featurea.platformer.util;

import featurea.app.Context;
import featurea.graphics.Graphics;
import featurea.opengl.Texture;
import featurea.util.*;

import java.util.Arrays;
import java.util.Collections;

import static featurea.geometry.Intersector.cut;
import static featurea.geometry.Intersector.hasIntersection;
import static featurea.platformer.config.Engine.verticalOverlapTolerance;

public class Rectangle {

  public double x1;
  public double y1;
  public double x2;
  public double y2;

  private boolean isTile = true;
  private double x3;
  private double y3;
  private double x4;
  private double y4;

  public Rectangle() {
    // no op
  }

  public Rectangle(double x1, double y1, double x2, double y2) {
    setValue(x1, y1, x2, y2);
  }

  public void setValue(String string) {
    String[] tokens = ArrayUtil.split(string);
    this.x1 = Double.valueOf(tokens[0]);
    this.y1 = Double.valueOf(tokens[1]);
    this.x2 = Double.valueOf(tokens[2]);
    this.y2 = Double.valueOf(tokens[3]);
  }

  public double width() {
    return Math.abs((right() - left()));
  }

  public double height() {
    return Math.abs((bottom() - top()));
  }

  public double length() {
    return Math.sqrt(MathUtil.square(y3 - y1) + MathUtil.square(x3 - x1));
  }

  public Vector direction() {
    return new Vector(x3 - x1, y3 - y1);
  }

  public void setValue(double x1, double y1, double x2, double y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;

    //
    x3 = x1;
    y3 = y2;
    x4 = x2;
    y4 = y1;
  }

  public void setValue(Rectangle rectangle) {
    setValue(rectangle.x1, rectangle.y1, rectangle.x2, rectangle.y2);
  }

  public void rotate(double x, double y, Angle angle) {
    if (!isTile()) {
      {
        double[] result = Vector.rotate(x1 + x, y1 + y, x, y, angle);
        x1 = result[0] - x;
        y1 = result[1] - y;
      }
      {
        double[] result = Vector.rotate(x2 + x, y2 + y, x, y, angle);
        x2 = result[0] - x;
        y2 = result[1] - y;
      }
      {
        double[] result = Vector.rotate(x3 + x, y3 + y, x, y, angle);
        x3 = result[0] - x;
        y3 = result[1] - y;
      }
      {
        double[] result = Vector.rotate(x4 + x, y4 + y, x, y, angle);
        x4 = result[0] - x;
        y4 = result[1] - y;
      }
    }
  }

  private boolean isTile() {
    return isTile;
  }

  public void setTile(boolean isTile) {
    this.isTile = isTile;
  }

  public void draw(Graphics graphics, Vector position, Color color) {
    if (isTile()) {
      graphics.drawRectangle(x1, y1, x2, y2, color);
    } else {
      graphics.drawPoints(false, color, position, x1, y1, x4, y4, x2, y2, x3, y3);
    }
  }

  public static void calculateOverlap(Vector result, Rectangle rectangle1, Vector position1, Rectangle rectangle2, Vector position2)
      throws UnsupportedOperationException {
    if (!rectangle1.isTile() && !rectangle2.isTile()) {
      throw new UnsupportedOperationException("Both are not tile"); // todo support this case
    }
    if (rectangle1.isTile() && rectangle2.isTile()) {
      double left1 = position1.x + rectangle1.left();
      double top1 = position1.y + rectangle1.top();
      double right1 = position1.x + rectangle1.right();
      double bottom1 = position1.y + rectangle1.bottom();
      double left2 = position2.x + rectangle2.left();
      double top2 = position2.y + rectangle2.top();
      double right2 = position2.x + rectangle2.right();
      double bottom2 = position2.y + rectangle2.bottom();
      if (left1 < right2 &&
          right1 > left2 &&
          bottom1 > top2 &&
          top1 < bottom2) {
        double leftX = left2 - right1;
        double rightX = right2 - left1;
        double upY = top2 - bottom1;
        double downY = bottom2 - top1;

        double deltaX;
        if (Math.abs(leftX) < Math.abs(rightX)) {
          deltaX = leftX;
        } else {
          deltaX = rightX;
        }

        double deltaY;
        if (Math.abs(upY) < Math.abs(downY)) {
          deltaY = upY;
        } else {
          deltaY = downY;
        }
        result.setValue(deltaX, deltaY);
      }
    } else {
      if (collide(rectangle1, position1, rectangle2, position2, verticalOverlapTolerance)) {
        result.setValue(1, 1); // fake
      }
    }
  }

  public double left() {
    if (isTile()) {
      return x1;
    } else {
      return Collections.min(Arrays.asList(x1, x2, x3, x4));
    }
  }

  public double right() {
    if (isTile()) {
      return x2;
    } else {
      return Collections.max(Arrays.asList(x1, x2, x3, x4));
    }
  }

  public double top() {
    if (isTile()) {
      return y1;
    } else {
      return Collections.min(Arrays.asList(y1, y2, y3, y4));
    }
  }

  public double bottom() {
    if (isTile()) {
      return y2;
    } else {
      return Collections.max(Arrays.asList(y1, y2, y3, y4));
    }
  }

  public double ox() {
    return (left() + right()) / 2;
  }

  public double oy() {
    return (top() + bottom()) / 2;
  }

  public void drawTile(Graphics graphics, String file, Vector position, Angle angle) {
    Texture texture = Context.getResources().getTexture(file);
    if (texture != null) {
      Vector direction = direction();
      double width = texture.getWidth();
      double height = texture.getHeight();
      int count = (int) (length() / height + 0.5);
      for (int i = 0; i < count; i++) {
        double x = position.x + i * width * direction.cos();
        double y = position.y + i * height * direction.sin();
        graphics.drawTexture(file, x - width / 2, y - height / 2, x + width / 2, y + height / 2, angle, x, y, null, false, false, null);
      }
    } else {
      if (!Context.isProduction()) {
        Context.getLoader().load(file);
      } else {
        throw new RuntimeException("Texture not load: " + file);
      }
    }
  }

  /**/

  private static boolean collide(Rectangle polygon1, Vector position1, Rectangle polygon2, Vector position2, double tolerance) {
    double[] points1 = new double[]{position1.x + polygon1.x1, position1.y + polygon1.y1, position1.x + polygon1.x4, position1.y + polygon1.y4, position1.x + polygon1.x2, position1.y + polygon1.y2, position1.x + polygon1.x3, position1.y + polygon1.y3};
    double[] points2 = new double[]{position2.x + polygon2.x1, position2.y + polygon2.y1, position2.x + polygon2.x4, position2.y + polygon2.y4, position2.x + polygon2.x2, position2.y + polygon2.y2, position2.x + polygon2.x3, position2.y + polygon2.y3};
    for (int i = 0, iNext = i + 1; i < points1.length / 2; i++, iNext = i + 1) {
      if (iNext >= points1.length / 2) {
        iNext = 0;
      }
      double x1 = points1[i * 2];
      double y1 = points1[i * 2 + 1];
      double x2 = points1[iNext * 2];
      double y2 = points1[iNext * 2 + 1];
      x1 = cut(x1, polygon1.ox(), tolerance);
      y1 = cut(y1, polygon1.oy(), tolerance);
      x2 = cut(x2, polygon1.ox(), tolerance);
      y2 = cut(y2, polygon1.oy(), tolerance);
      for (int j = 0, jNext = j + 1; j < points2.length / 2; j++, jNext = j + 1) {
        if (jNext >= points2.length / 2) {
          jNext = 0;
        }
        double x3 = points2[j * 2];
        double y3 = points2[j * 2 + 1];
        double x4 = points2[jNext * 2];
        double y4 = points2[jNext * 2 + 1];
        x3 = cut(x3, polygon2.ox(), tolerance);
        y3 = cut(y3, polygon2.oy(), tolerance);
        x4 = cut(x4, polygon2.ox(), tolerance);
        y4 = cut(y4, polygon2.oy(), tolerance);
        if (hasIntersection(x1, y1, x2, y2, x3, y3, x4, y4)) {
          return true;
        }
      }
    }
    return false;
  }


}
