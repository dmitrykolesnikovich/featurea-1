package featurea.app;

import featurea.util.*;

// zoomAnchorHorizontal = top, center, bottom
// zoomAnchorVertical = left, center, right
// resizeAnchorHorizontal = left, center, right
// resizeAnchorVertical = top, center, bottom

public class Camera extends Rectangle {

  public final Zoom zoom = new Zoom();
  public final Resize resize = new Resize();

  public void onLayout(Size size) {
    if (resize.anchorHorizontal != null) {
      if (width() != size.width) {
        resize.anchorHorizontal.resize(this);
      }
    }
    if (resize.anchorVertical != null) {
      if (height() != size.height) {
        resize.anchorVertical.resize(this);
      }
    }
    zoom.onLayout(width(), height());
  }

  public Camera setZoomAnchorHorizontal(ZoomAnchorHorizontal zoomAnchorHorizontal) {
    zoom.anchorHorizontal = zoomAnchorHorizontal;
    return this;
  }

  public Camera setZoomAnchorVertical(ZoomAnchorVertical zoomAnchorVertical) {
    zoom.anchorVertical = zoomAnchorVertical;
    return this;
  }

  public Camera setResizeAnchorHorizontal(ResizeAnchorHorizontal resizeAnchorHorizontal) {
    resize.anchorHorizontal = resizeAnchorHorizontal;
    return this;
  }

  public Camera setResizeAnchorVertical(ResizeAnchorVertical resizeAnchorVertical) {
    resize.anchorVertical = resizeAnchorVertical;
    return this;
  }

}
