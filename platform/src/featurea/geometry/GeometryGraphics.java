package featurea.geometry;

import featurea.graphics.Graphics;
import featurea.util.Color;

public class GeometryGraphics {

  private static final GeometryGraphics instance = new GeometryGraphics();

  public static GeometryGraphics getInstance() {
    return instance;
  }

  public void drawShape(Shape shape, Color color, Graphics graphics) {
    Bounds bounds = shape.getBounds();
    if (bounds instanceof Circle) {
      Circle circle = (Circle) bounds;
      drawShape(circle, color, graphics);
    } else if (bounds instanceof Polygon) {
      Polygon polygon = (Polygon) bounds;
      if (polygon.isLine) {
        drawPolyline(polygon, color, graphics);
      } else {
        drawPolygon(polygon, color, graphics);
      }
    }
  }

  public void drawShape(Circle circle, Color color, Graphics graphics) {
    graphics.drawCircle(circle.ox(), circle.oy(), circle.width / 2, circle.height / 2, color, circle.angle);
  }

  public void drawPolygon(Polygon polygon, Color color, Graphics graphics) {
    graphics.drawPoints(false, color, null, polygon.points);
  }

  public void drawPolyline(Polygon polygon, Color color, Graphics graphics) {
    graphics.drawPoints(true, color, null, polygon.points);
  }


}
