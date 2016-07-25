package featurea.geometry;

import featurea.util.Angle;
import featurea.util.Vector;
import featurea.xml.XmlResource;

public class Shape implements Bounds, XmlResource {

  /*private*/ Polygon polygon;
  /*private*/ Circle circle;
  private Bounds bounds;
  private Vector position = new Vector();
  public final Angle angle = new Angle();
  private double scaleX = 1;
  private double scaleY = 1;
  private boolean isFlipX;
  private boolean isFlipY;

  public final Shape setLine(double... points) {
    for (int i = 0; i < points.length; i += 2) {
      points[i] += position.x;
      points[i + 1] += position.y;
    }
    this.polygon = new Polygon(points).setLine(true);
    this.circle = null;
    updateCurrentGeometry();
    return this;
  }

  public final Shape setPolygon(double... points) {
    for (int i = 0; i < points.length; i += 2) {
      points[i] += position.x;
      points[i + 1] += position.y;
    }
    this.polygon = new Polygon(points).setLine(false);
    this.circle = null;
    updateCurrentGeometry();
    return this;
  }

  public final Shape setCircle(double x1, double y1, double x2, double y2) {
    this.polygon = null;
    x1 += position.x;
    x2 += position.x;
    y1 += position.y;
    y2 += position.y;
    this.circle = new Circle(x1, y1, x2, y2);
    updateCurrentGeometry();
    return this;
  }

  @Override
  public void move(double dx, double dy, double dz) {
    if (dx == 0 && dy == 0 && dz == 0) {
      return;
    }
    if (bounds != null) {
      bounds.move(dx, dy, dz);
    }
    position.plus(dx, dy, dz);
  }

  private final Angle rotateAngle = new Angle();

  public final void rotate(double angle) {
    rotateAngle.setValue(angle);
    rotate(rotateAngle);
  }

  @Override
  public final void rotate(Angle angle) {
    Vector position = getPosition();
    rotate(position.x, position.y, angle);
  }

  public void rotate(double ox, double oy, Angle angle) {
    if (angle.getValue() != 0) {
      if (bounds != null) {
        if (bounds instanceof Circle) {
          Circle circle = (Circle) bounds;
          circle.rotate(ox, oy, angle);
        }
        if (bounds instanceof Polygon) {
          Polygon polygon = (Polygon) bounds;
          polygon.rotate(ox, oy, angle);
        }
      }
      this.angle.plus(angle.getValue());
      this.position.rotate(ox, oy, angle);
    }
  }

  @Override
  public final void scaleX(double deltaScaleX) {
    scaleX(ox(), deltaScaleX);
  }

  @Override
  public void scaleX(double ox, double deltaScaleX) {
    if (deltaScaleX != 1) {
      this.scaleX *= deltaScaleX;
      if (bounds != null) {
        bounds.scaleX(ox, deltaScaleX);
      }
      position.scaleX(ox, deltaScaleX);
    }
  }

  @Override
  public final void scaleY(double deltaScaleY) {
    scaleY(oy(), deltaScaleY);
  }

  @Override
  public void scaleY(double oy, double deltaScaleY) {
    if (deltaScaleY != 1) {
      this.scaleY *= deltaScaleY;
      if (bounds != null) {
        bounds.scaleY(oy, deltaScaleY);
      }
      position.scaleY(oy, deltaScaleY);
    }
  }

  @Override
  public final void flipX() {
    flipX(ox());
  }

  @Override
  public void flipX(double ox) {
    if (bounds != null) {
      bounds.flipX(ox);
    }
    position.flipX(ox);
    isFlipX = !isFlipX;
  }

  @Override
  public final void flipY() {
    flipY(oy());
  }

  @Override
  public void flipY(double oy) {
    if (bounds != null) {
      bounds.flipY(oy);
    }
    isFlipY = !isFlipY;
  }


  public Shape setRectangle(double x1, double y1, double x2, double y2) {
    return setPolygon(x1, y1, x2, y1, x2, y2, x1, y2);
  }

  @Override
  public boolean contains(double x, double y) {
    if (bounds != null) {
      return bounds.contains(x, y);
    } else {
      return false;
    }
  }

  public final boolean contains(Shape shape) {
    return contains(shape.ox(), shape.oy());
  }

  public final boolean intersects(Shape shape) {
    return intersects(shape, 0);
  }

  @Override
  public boolean intersects(Shape shape, double tolerance) {
    if (left() > shape.right() || right() < shape.left() || bottom() < shape.top() || top() > shape.bottom()) {
      return false;
    }
    if (bounds != null) {
      return bounds.intersects(shape, tolerance);
    } else {
      return false;
    }
  }

  public final Shape setTop(double y1) {
    double dy = y1 - top();
    move(0, dy, 0);
    return this;
  }

  public final Shape setBottom(double y2) {
    double dy = y2 - bottom();
    move(0, dy, 0);
    return this;
  }

  public final Shape setLeft(double x1) {
    double dx = x1 - left();
    move(dx, 0, 0);
    return this;
  }

  public final Shape setRight(double x2) {
    double dx = x2 - right();
    move(dx, 0, 0);
    return this;
  }

  public final Shape setWidth(double width) {
    double k = width / width();
    scaleX(ox(), k);
    return this;
  }

  public final Shape setHeight(double height) {
    double k = height / height();
    scaleY(oy(), k);
    return this;
  }

  public final Vector getPosition() {
    return position;
  }

  public /*final*/ Shape setPosition(double x, double y) {
    return setPosition(x, y, position.z);
  }

  public final Shape setPosition(double x, double y, double z) {
    double dx = x - this.position.x;
    double dy = y - this.position.y;
    double dz = z - this.position.z;
    move(dx, dy, dz);
    return this;
  }

  public final Shape setPosition(Vector position) {
    return setPosition(position.x, position.y, position.z);
  }

  public final double getAngle() {
    return angle.getValue();
  }

  public final Shape setAngle(double angle) {
    return setAngle(angle, ox(), oy());
  }

  public final Shape setAngle(double angle, double ox, double oy) {
    double deltaAngle = angle - this.angle.getValue();
    rotate(ox, oy, new Angle(deltaAngle));
    return this;
  }

  public final double getScaleX() {
    return scaleX;
  }

  public final Shape setScaleX(double scaleX) {
    return setScaleX(scaleX, ox());
  }

  public final Shape setScaleX(double scaleX, double ox) {
    if (this.scaleX != scaleX) {
      double deltaScaleX = scaleX / this.scaleX;
      scaleX(ox, deltaScaleX);
    }
    return this;
  }

  public final double getScaleY() {
    return scaleY;
  }

  public final Shape setScaleY(double scaleY) {
    return setScaleY(scaleY, oy());
  }

  public final Shape setScaleY(double scaleY, double oy) {
    if (this.getScaleY() != scaleY) {
      double deltaScaleY = scaleY / this.scaleY;
      scaleY(oy, deltaScaleY);
    }
    return this;
  }

  public final void setScale(double scale) {
    setScaleX(scale);
    setScaleY(scale);
  }

  public boolean isFlipX() {
    return isFlipX;
  }

  public boolean isFlipY() {
    return isFlipY;
  }

  public /*final*/ Shape setShape(Shape shape) {
    this.isFlipX = shape.isFlipX;
    this.isFlipY = shape.isFlipY;
    this.position = new Vector(shape.position);
    this.angle.setValue(shape.angle);
    this.scaleX = shape.scaleX;
    this.scaleY = shape.scaleY;
    if (shape.polygon != null) {
      this.polygon = new Polygon(shape.polygon);
    }
    if (shape.circle != null) {
      this.circle = new Circle(shape.circle);
    }
    move(ox(), oy(), oz());
    scaleX(ox(), getScaleX());
    scaleY(oy(), getScaleY());
    rotate(angle);
    updateCurrentGeometry();
    return this;
  }

  public final double ox() {
    return position.x;
  }

  public final double oz() {
    return position.z;
  }

  public final double oy() {
    return position.y;
  }

  public final double width() {
    if (circle != null) {
      return circle.width;
    }
    return right() - left();
  }

  public final double height() {
    if (circle != null) {
      return circle.height;
    }
    return bottom() - top();
  }

  public double left() {
    if (bounds instanceof Polygon) {
      Polygon polygon = (Polygon) bounds;
      return polygon.left();
    } else if (bounds instanceof Circle) {
      Circle circle = (Circle) bounds;
      return circle.left();
    } else {
      return 0;
    }

  }

  public double top() {
    if (bounds instanceof Polygon) {
      Polygon polygon = (Polygon) bounds;
      return polygon.top();
    } else if (bounds instanceof Circle) {
      Circle circle = (Circle) bounds;
      return circle.top();
    } else {
      return 0;
    }
  }

  public double right() {
    if (bounds instanceof Polygon) {
      Polygon polygon = (Polygon) bounds;
      return polygon.right();
    } else if (bounds instanceof Circle) {
      Circle circle = (Circle) bounds;
      return circle.right();
    } else {
      return 0;
    }
  }

  public double bottom() {
    if (bounds instanceof Polygon) {
      Polygon polygon = (Polygon) bounds;
      return polygon.bottom();
    } else if (bounds instanceof Circle) {
      Circle circle = (Circle) bounds;
      return circle.bottom();
    } else {
      return 0;
    }
  }

  private void updateCurrentGeometry() {
    if (circle != null) {
      bounds = circle;
    } else if (polygon != null) {
      bounds = polygon;
    } else {
      bounds = null;
    }
  }

  public final Shape setFlipX(boolean isFlipX) {
    return setFlipX(isFlipX, ox());
  }

  public final Shape setFlipX(boolean isFlipX, double ox) {
    if (this.isFlipX != isFlipX) {
      flipX(ox);
    }
    return this;
  }

  public final Shape setFlipY(boolean isFlipY) {
    return setFlipY(isFlipY, oy());
  }

  public final Shape setFlipY(boolean isFlipY, double originY) {
    if (this.isFlipY != isFlipY) {
      flipY(originY);
    }
    return this;
  }

  public final Bounds getBounds() {
    return bounds;
  }

  public final void move(Vector vector) {
    move(vector.x, vector.y, vector.z);
  }

  @Override
  public Shape build() {
    return this;
  }

}
