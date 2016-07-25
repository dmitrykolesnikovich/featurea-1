package featurea.geometry;

import featurea.util.Angle;
import featurea.util.Vector;

import static featurea.util.MathUtil.square;

public class Circle implements Bounds {

  private final Vector position = new Vector();
  public double width;
  public double height;
  public final Angle angle = new Angle();

  public Circle(Circle circle) {
    this.position.x = circle.position.x;
    this.position.y = circle.position.y;
    this.width = circle.width;
    this.height = circle.height;
    this.angle.setValue(circle.angle);
  }

  public Circle(double x1, double y1, double x2, double y2) {
    this.position.x = (x1 + x2) / 2;
    this.position.y = (y1 + y2) / 2;
    this.width = Math.abs(x2 - x1);
    this.height = Math.abs(y2 - y1);
  }

  @Override
  public boolean intersects(Shape shape, double tolerance) {
    if (shape.polygon != null) {
      return Intersector.collide(shape.polygon, this, tolerance);
    }
    if (shape.circle != null) {
      return Intersector.collide(shape.circle, this, tolerance);
    }
    return false;
  }

  public boolean contains(double x, double y) {
    double A = width / 2;
    double B = height / 2;
    x -= position.x;
    y -= position.y;
    return (square((x * angle.cos() + y * angle.sin()) / A) + square((x * angle.sin() + y * angle.cos()) / B)) <= 1;
  }

  @Override
  public void move(double dx, double dy, double dz) {
    position.x += dx;
    position.y += dy;
  }

  @Override
  public void rotate(Angle angle) {
    rotate(position.x, position.y, angle);
  }

  public void rotate(double ox, double oy, Angle angle) {
    double[] xy = Vector.rotate(this.position.x, this.position.y, ox, oy, angle);
    this.position.x = xy[0];
    this.position.y = xy[1];
    this.angle.plus(angle.getValue());
  }

  @Override
  public void scaleX(double deltaScaleX) {
    scaleX(position.x, deltaScaleX);
  }

  @Override
  public void scaleX(double ox, double deltaScaleX) {
    double dx = (this.position.x - ox);
    dx *= deltaScaleX;
    this.position.x = ox + dx;
    width *= deltaScaleX;
  }

  @Override
  public void scaleY(double deltaScaleY) {
    scaleY(position.y, deltaScaleY);
  }

  @Override
  public void scaleY(double oy, double deltaScaleY) {
    double dy = (this.position.y - oy);
    dy *= deltaScaleY;
    this.position.y = oy + dy;
    height *= deltaScaleY;
  }

  @Override
  public void flipX() {
    flipX(position.x);
  }

  @Override
  public void flipX(double ox) {
    this.position.x += 2 * (ox - this.position.x);
  }

  @Override
  public void flipY() {
    flipX(position.y);
  }

  @Override
  public void flipY(double oy) {
    this.position.y += 2 * (oy - this.position.y);
  }

  public double ox() {
    return position.x;
  }

  public double oy() {
    return position.y;
  }

  public double left() {
    return position.x - width / 2;
  }

  public double right() {
    return position.x + width / 2;
  }

  public double top() {
    return position.y - height / 2;
  }

  public double bottom() {
    return position.y + height / 2;
  }

}
