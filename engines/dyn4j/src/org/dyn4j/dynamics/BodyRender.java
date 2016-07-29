package org.dyn4j.dynamics;

import featurea.graphics.Graphics;
import featurea.util.Angle;
import featurea.util.Colors;
import featurea.util.Vector;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Transform;


public class BodyRender {

  public static void drawRectangle(Graphics graphics, org.dyn4j.geometry.Rectangle rectangle, Transform transform) {
    // temp vars: point1, point2
    final Vector point1 = new Vector();
    final Vector point2 = new Vector();
    final Vector point3 = new Vector();
    final Vector point4 = new Vector();
    final Angle angle = new Angle();
    double x1 = rectangle.getVertices()[0].x;
    double x2 = rectangle.getVertices()[1].x;
    double x3 = rectangle.getVertices()[2].x;
    double x4 = rectangle.getVertices()[3].x;
    double y1 = rectangle.getVertices()[0].y;
    double y2 = rectangle.getVertices()[1].y;
    double y3 = rectangle.getVertices()[2].y;
    double y4 = rectangle.getVertices()[3].y;

    point1.setValue(x1, y1);
    point2.setValue(x2, y2);
    point3.setValue(x3, y3);
    point4.setValue(x4, y4);

    double degree = transform.getRotation() / Math.PI * 180;
    if (degree != 0) {
      angle.setValue(degree);
      Vector.rotate(point1, 0, 0, angle);
      Vector.rotate(point2, 0, 0, angle);
      Vector.rotate(point3, 0, 0, angle);
      Vector.rotate(point4, 0, 0, angle);
      x1 = point1.x;
      y1 = point1.y;
      x2 = point2.x;
      y2 = point2.y;
      x3 = point3.x;
      y3 = point3.y;
      x4 = point4.x;
      y4 = point4.y;
    }
    graphics.drawPoints(false, Colors.blue, new Vector(transform.getTranslationX(), transform.getTranslationY()),
        x1, y1, x2, y2, x3, y3, x4, y4);
  }

  public static void drawCircle(Graphics graphics, Circle circle, Transform transform) {
    throw new RuntimeException("Not implemented yet");
  }

}
