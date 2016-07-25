package featurea.util;

import featurea.app.Context;

public final class Zoom {

  public double x;
  public double y;
  public double scale = 1;
  public ZoomAnchorHorizontal anchorHorizontal;
  public ZoomAnchorVertical anchorVertical;

  public void scale(double ox, double oy, double scale) {
    double k1 = this.scale;
    double ox1 = this.x;
    double oy1 = this.y;
    double k2 = k1 * scale;
    double ox2 = ox1 + (k1 - k2) * (ox - ox1) / k1;
    double oy2 = oy1 + (k1 - k2) * (oy - oy1) / k1;
    this.scale = k2;
    this.x = ox2;
    this.y = oy2;
  }

  public void move(double dx, double dy) {
    x += dx;
    y += dy;
  }

  public void reset() {
    x = 0;
    y = 0;
    scale = 1;
  }

  public void onLayout(double width, double height) {
    if (width != 0 && height != 0) {
      Size size = Context.getRender().size;
      double windowWidth = size.width;
      double windowHeight = size.height;
      double widthRatio = windowWidth / width;
      double heightRatio = windowHeight / height;
      scale = Math.min(widthRatio, heightRatio);
      if (scale == widthRatio) {
        x = 0;
        y = (windowHeight - height * scale) / 2;
        if (anchorVertical != null) {
          anchorVertical.stretch(this);
        }
      } else {
        x = (windowWidth - width * scale) / 2;
        y = 0;
        if (anchorHorizontal != null) {
          anchorHorizontal.stretch(this);
        }
      }
    }
  }

}
