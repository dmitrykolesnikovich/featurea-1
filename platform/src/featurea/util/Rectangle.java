package featurea.util;

import featurea.xml.XmlResource;

// no rotation supported specially for camera
// programmer is attended to his own Rectangle implementation for performance reasons
public class Rectangle implements Transform, XmlResource {

  private Vector position = new Vector();
  public final Angle angle = new Angle();
  private double scaleX = 1;
  private double scaleY = 1;
  private boolean isFlipX;
  private boolean isFlipY;
  private double x1;
  private double y1;
  private double x2;
  private double y2;

  @Override
  public void move(double dx, double dy, double dz) {
    position.plus(dx, dy, dz);
  }

  private final Angle rotateAngle = new Angle();

  public final void rotate(double angle) {
    rotateAngle.setValue(angle);
    rotate(rotateAngle);
  }

  @Override
  public void rotate(Angle angle) {
    Vector position = getPosition();
    rotate(position.x, position.y, angle);
  }

  public void rotate(double ox, double oy, Angle angle) {
    if (angle.getValue() != 0) {
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
      position.scaleY(oy, deltaScaleY);
    }
  }

  @Override
  public final void flipX() {
    flipX(ox());
  }

  @Override
  public void flipX(double ox) {
    position.flipX(ox);
    isFlipX = !isFlipX;
  }

  @Override
  public final void flipY() {
    flipY(oy());
  }

  @Override
  public void flipY(double oy) {
    isFlipY = !isFlipY;
  }

  public final Rectangle setTop(double y1) {
    double dy = y1 - top();
    move(0, dy, 0);
    return this;
  }

  public final Rectangle setBottom(double y2) {
    double dy = y2 - bottom();
    move(0, dy, 0);
    return this;
  }

  public final Rectangle setLeft(double x1) {
    double dx = x1 - left();
    move(dx, 0, 0);
    return this;
  }

  public final Rectangle setRight(double x2) {
    double dx = x2 - right();
    move(dx, 0, 0);
    return this;
  }

  public final Rectangle setWidth(double width) {
    double k = width / width();
    scaleX(ox(), k);
    return this;
  }

  public final Rectangle setHeight(double height) {
    double k = height / height();
    scaleY(oy(), k);
    return this;
  }

  public final Vector getPosition() {
    return position;
  }

  public /*final*/ Rectangle setPosition(double x, double y) {
    return setPosition(x, y, position.z);
  }

  public Rectangle setPosition(double x, double y, double z) {
    double dx = x - this.position.x;
    double dy = y - this.position.y;
    double dz = z - this.position.z;
    move(dx, dy, dz);
    return this;
  }

  public final Rectangle setPosition(Vector position) {
    return setPosition(position.x, position.y, position.z);
  }

  public final double getAngle() {
    return angle.getValue();
  }

  public final Rectangle setAngle(double angle) {
    return setAngle(angle, ox(), oy());
  }

  public final Rectangle setAngle(double angle, double ox, double oy) {
    double deltaAngle = angle - this.angle.getValue();
    rotate(ox, oy, new Angle(deltaAngle));
    return this;
  }

  public final double getScaleX() {
    return scaleX;
  }

  public final Rectangle setScaleX(double scaleX) {
    return setScaleX(scaleX, ox());
  }

  public final Rectangle setScaleX(double scaleX, double ox) {
    if (this.scaleX != scaleX) {
      double deltaScaleX = scaleX / this.scaleX;
      scaleX(ox, deltaScaleX);
    }
    return this;
  }

  public final double getScaleY() {
    return scaleY;
  }

  public final Rectangle setScaleY(double scaleY) {
    return setScaleY(scaleY, oy());
  }

  public final Rectangle setScaleY(double scaleY, double oy) {
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


  public final double ox() {
    return (x1 + x2) / 2;
  }

  public final double oy() {
    return (y1 + y2) / 2;
  }

  public final double width() {
    double result = right() - left();
    if (result == 0) {
      System.out.println("breakpoint");
    }
    return result;
  }

  public final double height() {
    double result = bottom() - top();
    if (result == 0) {
      System.out.println("breakpoint");
    }
    return result;
  }

  public double left() {
    return position.x + x1;
  }

  public double top() {
    return position.y + y1;
  }

  public double right() {
    return position.x + x2;
  }

  public double bottom() {
    return position.y + y2;
  }

  public final Rectangle setFlipX(boolean isFlipX) {
    return setFlipX(isFlipX, ox());
  }

  public final Rectangle setFlipX(boolean isFlipX, double ox) {
    if (this.isFlipX != isFlipX) {
      flipX(ox);
    }
    return this;
  }

  public final Rectangle setFlipY(boolean isFlipY) {
    return setFlipY(isFlipY, oy());
  }

  public final Rectangle setFlipY(boolean isFlipY, double originY) {
    if (this.isFlipY != isFlipY) {
      flipY(originY);
    }
    return this;
  }

  public Rectangle setRectangle(double x1, double y1, double x2, double y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    return this;
  }

  @Override
  public Rectangle build() {
    return this;
  }

  public boolean contains(double x, double y) {
    return x > x1 && x < x2 && y > y1 && y < y2;
  }

}
